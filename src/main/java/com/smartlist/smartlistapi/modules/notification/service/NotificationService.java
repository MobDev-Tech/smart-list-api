package com.smartlist.smartlistapi.modules.notification.service;

import com.smartlist.smartlistapi.modules.geofence.dto.GeofenceCheckRequestDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.GeofenceCheckResponseDTO;
import com.smartlist.smartlistapi.modules.geofence.service.ResidenceService;
import com.smartlist.smartlistapi.modules.notification.domain.SnoozeEntity;
import com.smartlist.smartlistapi.modules.notification.domain.SnoozeOptionEnum;
import com.smartlist.smartlistapi.modules.notification.dto.*;
import com.smartlist.smartlistapi.modules.notification.repository.SnoozeRepository;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingListGroupedDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.service.ShoppingItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final SnoozeRepository snoozeRepository;
    private final ResidenceService residenceService;
    private final ShoppingItemService shoppingItemService;

    /**
     * Ativa a pausa de lembretes do dispositivo pela opção selecionada.
     */
    @Transactional
    public SnoozeResponseDTO applySnooze(String deviceId, SnoozeRequestDTO dto) {
        SnoozeEntity snooze = snoozeRepository.findByDeviceId(deviceId)
                .orElseGet(() -> SnoozeEntity.builder().deviceId(deviceId).build());

        snooze.setActive(true);
        snooze.setOption(dto.option());
        snooze.setSnoozedUntil(calculateSnoozeUntil(dto.option()));

        snoozeRepository.save(snooze);
        // Registra a opção escolhida e até quando os lembretes ficarão pausados.
        logger.info("snooze_activated option={} until={}", snooze.getOption(), snooze.getSnoozedUntil());

        String message = dto.option() == SnoozeOptionEnum.INDEFINITE
                ? "Lembretes pausados até reativação manual."
                : "Lembretes pausados temporariamente.";

        return SnoozeResponseDTO.fromEntity(snooze, message);
    }

    /**
     * Cancela a pausa de lembretes e reativa o monitoramento para o dispositivo.
     */
    @Transactional
    public SnoozeResponseDTO cancelSnooze(String deviceId) {
        SnoozeEntity snooze = snoozeRepository.findByDeviceId(deviceId)
                .orElseGet(() -> SnoozeEntity.builder().deviceId(deviceId).build());

        snooze.setActive(false);
        snooze.setOption(null);
        snooze.setSnoozedUntil(null);

        snoozeRepository.save(snooze);
        // Registra que a pausa foi cancelada e os lembretes foram reativados.
        logger.info("snooze_cancelled");

        return SnoozeResponseDTO.fromEntity(snooze, "Monitoramento de lembretes reativado.");
    }

    /**
     * Avalia os itens pendentes, a pausa de lembretes e a geofence para decidir se notifica.
     */
    @Transactional(readOnly = true)
    public NotificationTriggerResponseDTO evaluateNotificationTrigger(String deviceId, NotificationTriggerRequestDTO dto) {
        // 1. Checa itens pendentes na lista EXCLUSIVAS deste celular
        ShoppingListGroupedDTO groupedItems = shoppingItemService.getGroupedItems(deviceId);
        if (groupedItems.pendingCount() == 0) {
            // Explica a decisão de não notificar quando não há itens pendentes.
            logger.info("notification_decision shouldNotify=false reason=no_pending_items pendingCount=0");
            return new NotificationTriggerResponseDTO(
                    false,
                    "Nenhum item pendente na lista de compras.",
                    0,
                    List.of()
            );
        }

        // 2. Checa estado de Soneca EXCLUSIVO deste celular
        SnoozeEntity snooze = snoozeRepository.findByDeviceId(deviceId).orElse(null);
        if (snooze != null && snooze.isActive()) {
            if (snooze.getOption() == SnoozeOptionEnum.INDEFINITE) {
                // Explica a decisão de não notificar devido à pausa manual indefinida.
                logger.info("notification_decision shouldNotify=false reason=indefinite_snooze pendingCount={}",
                        groupedItems.pendingCount());
                return new NotificationTriggerResponseDTO(
                        false,
                        "Notificações estão pausadas manualmente (Modo Soneca Indefinido).",
                        groupedItems.pendingCount(),
                        List.of()
                );
            }

            if (snooze.getSnoozedUntil() != null && LocalDateTime.now().isBefore(snooze.getSnoozedUntil())) {
                // Explica a decisão de não notificar e informa o fim da pausa temporária.
                logger.info("notification_decision shouldNotify=false reason=temporary_snooze until={} pendingCount={}",
                        snooze.getSnoozedUntil(), groupedItems.pendingCount());
                return new NotificationTriggerResponseDTO(
                        false,
                        "Modo soneca ativo até: " + snooze.getSnoozedUntil(),
                        groupedItems.pendingCount(),
                        List.of()
                );
            }
        }

        // 3. Checa Geofence (posição atual vs residência cadastrada para este celular)
        GeofenceCheckResponseDTO geofence = residenceService.checkGeofenceStatus(
                deviceId,
                new GeofenceCheckRequestDTO(dto.currentLatitude(), dto.currentLongitude())
        );

        if (geofence.insidePerimeter()) {
            // Explica a decisão de não notificar porque o dispositivo está dentro do perímetro.
            logger.info("notification_decision shouldNotify=false reason=inside_perimeter pendingCount={}",
                    groupedItems.pendingCount());
            return new NotificationTriggerResponseDTO(
                    false,
                    "Dispositivo ainda está dentro do raio residencial.",
                    groupedItems.pendingCount(),
                    List.of()
            );
        }

        // 4. Tudo certo -> Disparar Notificação!
        // Registra que os itens pendentes e a saída do perímetro habilitaram a notificação.
        logger.info("notification_decision shouldNotify=true reason=outside_perimeter_with_pending_items pendingCount={}",
                groupedItems.pendingCount());
        return new NotificationTriggerResponseDTO(
                true,
                "Afastamento detectado com itens pendentes na lista!",
                groupedItems.pendingCount(),
                groupedItems.pendingItems()
        );
    }

    /**
     * Calcula o fim da pausa conforme a opção; pausas indefinidas não têm horário de término.
     */
    private LocalDateTime calculateSnoozeUntil(SnoozeOptionEnum option) {
        LocalDateTime now = LocalDateTime.now();
        return switch (option) {
            case MINUTES_30 -> now.plusMinutes(30);
            case HOUR_1 -> now.plusHours(1);
            case TOMORROW_MORNING -> LocalDate.now().plusDays(1).atTime(8, 0);
            case INDEFINITE -> null;
        };
    }
}