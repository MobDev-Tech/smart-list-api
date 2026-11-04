package com.smartlist.smartlistapi.modules.notification.service;

import com.smartlist.smartlistapi.modules.geofence.dto.GeofenceCheckResponseDTO;
import com.smartlist.smartlistapi.modules.geofence.service.ResidenceService;
import com.smartlist.smartlistapi.modules.notification.domain.SnoozeEntity;
import com.smartlist.smartlistapi.modules.notification.domain.SnoozeOptionEnum;
import com.smartlist.smartlistapi.modules.notification.dto.NotificationTriggerRequestDTO;
import com.smartlist.smartlistapi.modules.notification.dto.NotificationTriggerResponseDTO;
import com.smartlist.smartlistapi.modules.notification.dto.SnoozeRequestDTO;
import com.smartlist.smartlistapi.modules.notification.dto.SnoozeResponseDTO;
import com.smartlist.smartlistapi.modules.notification.repository.SnoozeRepository;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingItemResponseDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingListGroupedDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.service.ShoppingItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do serviço de notificações")
class NotificationServiceTest {

    @Mock
    private SnoozeRepository snoozeRepository;

    @Mock
    private ResidenceService residenceService;

    @Mock
    private ShoppingItemService shoppingItemService;

    @InjectMocks
    private NotificationService service;

    @Test
    @DisplayName("Deve definir prazo de trinta minutos para a soneca")
    void dadaOpcaoDeTrintaMinutos_quandoSonecaAplicada_entaoDefinirPrazoDeTrintaMinutos() {
        when(snoozeRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.empty());
        when(snoozeRepository.save(any(SnoozeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        LocalDateTime before = LocalDateTime.now();

        SnoozeResponseDTO result = service.applySnooze(new SnoozeRequestDTO(SnoozeOptionEnum.MINUTES_30));

        LocalDateTime after = LocalDateTime.now();
        assertTrue(result.active());
        assertEquals(SnoozeOptionEnum.MINUTES_30, result.option());
        assertNotNull(result.snoozedUntil());
        assertFalse(result.snoozedUntil().isBefore(before.plusMinutes(30)));
        assertFalse(result.snoozedUntil().isAfter(after.plusMinutes(30)));
        assertEquals("Lembretes pausados temporariamente.", result.message());
        verify(snoozeRepository).save(any(SnoozeEntity.class));
    }

    @Test
    @DisplayName("Deve aplicar soneca indefinida sem definir prazo")
    void dadaOpcaoIndefinida_quandoSonecaAplicada_entaoNaoDefinirPrazo() {
        when(snoozeRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.empty());
        when(snoozeRepository.save(any(SnoozeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SnoozeResponseDTO result = service.applySnooze(new SnoozeRequestDTO(SnoozeOptionEnum.INDEFINITE));

        assertTrue(result.active());
        assertEquals(SnoozeOptionEnum.INDEFINITE, result.option());
        assertNull(result.snoozedUntil());
        assertEquals("Lembretes pausados até reativação manual.", result.message());
    }

    @Test
    @DisplayName("Deve cancelar a soneca e reativar o monitoramento")
    void dadaSonecaAtiva_quandoCancelada_entaoReativarMonitoramento() {
        SnoozeEntity snooze = SnoozeEntity.builder()
                .id(1L)
                .active(true)
                .option(SnoozeOptionEnum.INDEFINITE)
                .snoozedUntil(LocalDateTime.now().plusDays(1))
                .build();
        when(snoozeRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(snooze));
        when(snoozeRepository.save(snooze)).thenReturn(snooze);

        SnoozeResponseDTO result = service.cancelSnooze();

        assertFalse(result.active());
        assertNull(result.option());
        assertNull(result.snoozedUntil());
        assertEquals("Monitoramento de lembretes reativado.", result.message());
        verify(snoozeRepository).save(snooze);
    }

    @Test
    @DisplayName("Não deve notificar quando não há itens pendentes")
    void dadaListaSemItensPendentes_quandoGatilhoAvaliado_entaoNaoNotificar() {
        when(shoppingItemService.getGroupedItems()).thenReturn(groupedItems(0));

        NotificationTriggerResponseDTO result = service.evaluateNotificationTrigger(request());

        assertFalse(result.shouldNotify());
        assertEquals(0, result.pendingItemsCount());
        assertEquals("Nenhum item pendente na lista de compras.", result.reason());
        verify(snoozeRepository, never()).findFirstByOrderByIdAsc();
        verify(residenceService, never()).checkGeofenceStatus(any());
    }

    @Test
    @DisplayName("Não deve notificar durante uma soneca indefinida")
    void dadaSonecaIndefinidaAtiva_quandoGatilhoAvaliado_entaoNaoNotificar() {
        when(shoppingItemService.getGroupedItems()).thenReturn(groupedItems(2));
        when(snoozeRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(
                SnoozeEntity.builder().active(true).option(SnoozeOptionEnum.INDEFINITE).build()));

        NotificationTriggerResponseDTO result = service.evaluateNotificationTrigger(request());

        assertFalse(result.shouldNotify());
        assertEquals(2, result.pendingItemsCount());
        assertTrue(result.reason().contains("pausadas manualmente"));
        verify(residenceService, never()).checkGeofenceStatus(any());
    }

    @Test
    @DisplayName("Não deve notificar durante uma soneca temporária")
    void dadaSonecaTemporariaAtiva_quandoGatilhoAvaliado_entaoNaoNotificar() {
        LocalDateTime deadline = LocalDateTime.now().plusMinutes(10);
        when(shoppingItemService.getGroupedItems()).thenReturn(groupedItems(2));
        when(snoozeRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(
                SnoozeEntity.builder()
                        .active(true)
                        .option(SnoozeOptionEnum.HOUR_1)
                        .snoozedUntil(deadline)
                        .build()));

        NotificationTriggerResponseDTO result = service.evaluateNotificationTrigger(request());

        assertFalse(result.shouldNotify());
        assertEquals(2, result.pendingItemsCount());
        assertTrue(result.reason().contains(deadline.toString()));
        verify(residenceService, never()).checkGeofenceStatus(any());
    }

    @Test
    @DisplayName("Não deve notificar quando o dispositivo está no perímetro")
    void dadoDispositivoDentroDoPerimetro_quandoGatilhoAvaliado_entaoNaoNotificar() {
        when(shoppingItemService.getGroupedItems()).thenReturn(groupedItems(2));
        when(snoozeRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.empty());
        when(residenceService.checkGeofenceStatus(any())).thenReturn(
                new GeofenceCheckResponseDTO(true, 50, 150, "Dentro"));

        NotificationTriggerResponseDTO result = service.evaluateNotificationTrigger(request());

        assertFalse(result.shouldNotify());
        assertEquals(2, result.pendingItemsCount());
        assertEquals("Dispositivo ainda está dentro do raio residencial.", result.reason());
    }

    @Test
    @DisplayName("Deve notificar quando o dispositivo está fora e há itens pendentes")
    void dadosItensPendentesEDispositivoForaDoPerimetro_quandoGatilhoAvaliado_entaoNotificar() {
        ShoppingItemResponseDTO pendingItem = new ShoppingItemResponseDTO(
                1L, "Leite", "1 L", null, false, null);
        ShoppingListGroupedDTO items = new ShoppingListGroupedDTO(1, List.of(pendingItem), 0, List.of());
        when(shoppingItemService.getGroupedItems()).thenReturn(items);
        when(snoozeRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.empty());
        when(residenceService.checkGeofenceStatus(any())).thenReturn(
                new GeofenceCheckResponseDTO(false, 500, 150, "Fora"));

        NotificationTriggerResponseDTO result = service.evaluateNotificationTrigger(request());

        assertTrue(result.shouldNotify());
        assertEquals("Afastamento detectado com itens pendentes na lista!", result.reason());
        assertEquals(1, result.pendingItemsCount());
        assertEquals(List.of(pendingItem), result.pendingItemsSummary());
    }

    @Test
    @DisplayName("Deve definir a soneca para amanhã às oito da manhã")
    void dadaOpcaoAmanhaDeManha_quandoSonecaAplicada_entaoDefinirPrazoAsOitoHoras() {
        when(snoozeRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.empty());
        when(snoozeRepository.save(any(SnoozeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SnoozeResponseDTO result = service.applySnooze(
                new SnoozeRequestDTO(SnoozeOptionEnum.TOMORROW_MORNING));

        assertEquals(LocalDate.now().plusDays(1).atTime(8, 0), result.snoozedUntil());
    }

    private NotificationTriggerRequestDTO request() {
        return new NotificationTriggerRequestDTO(-23.5, -46.6);
    }

    private ShoppingListGroupedDTO groupedItems(long count) {
        List<ShoppingItemResponseDTO> pending = count == 0 ? List.of() : List.of(
                new ShoppingItemResponseDTO(1L, "Leite", "1 L", null, false, null),
                new ShoppingItemResponseDTO(2L, "Pão", "1 un.", null, false, null));
        return new ShoppingListGroupedDTO(count, pending, 0, List.of());
    }
}
