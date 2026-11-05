package com.smartlist.smartlistapi.modules.user.service;

import com.smartlist.smartlistapi.modules.geofence.dto.ResidenceResponseDTO;
import com.smartlist.smartlistapi.modules.geofence.service.ResidenceService;
import com.smartlist.smartlistapi.modules.user.domain.LocationPermissionEnum;
import com.smartlist.smartlistapi.modules.user.domain.NotificationPermissionEnum;
import com.smartlist.smartlistapi.modules.user.domain.UserSettingsEntity;
import com.smartlist.smartlistapi.modules.user.dto.SystemDiagnosticResponseDTO;
import com.smartlist.smartlistapi.modules.user.dto.UserSettingsRequestDTO;
import com.smartlist.smartlistapi.modules.user.dto.UserSettingsResponseDTO;
import com.smartlist.smartlistapi.modules.user.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final UserSettingsRepository repository;
    private final ResidenceService residenceService;

    @Transactional(readOnly = true)
    public UserSettingsResponseDTO getSettings(String deviceId) {
        UserSettingsEntity entity = repository.findByDeviceId(deviceId)
                .orElseGet(() -> createDefaultSettings(deviceId));
        return UserSettingsResponseDTO.fromEntity(entity);
    }

    @Transactional
    public UserSettingsResponseDTO updateSettings(String deviceId, UserSettingsRequestDTO dto) {
        UserSettingsEntity entity = repository.findByDeviceId(deviceId)
                .orElseGet(() -> createDefaultSettings(deviceId));

        entity.setRemindOnLeave(dto.remindOnLeave());
        entity.setVibrateOnNotify(dto.vibrateOnNotify());
        entity.setNotificationPermission(dto.notificationPermission());
        entity.setLocationPermission(dto.locationPermission());

        return UserSettingsResponseDTO.fromEntity(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public SystemDiagnosticResponseDTO getDiagnostic(String deviceId) {
        UserSettingsEntity settings = repository.findByDeviceId(deviceId)
                .orElseGet(() -> createDefaultSettings(deviceId));

        boolean residenceConfigured = false;
        String statusMessage = "Residência não configurada";

        try {
            ResidenceResponseDTO residence = residenceService.getResidence(deviceId);
            if (residence != null) {
                residenceConfigured = true;
                statusMessage = residence.geofenceActive() 
                        ? "Monitoramento Geofence Ativo" 
                        : "Monitoramento Desativado";
            }
        } catch (Exception ignored) {
            // Se a residência ainda não foi cadastrada para este dispositivo, mantemos a mensagem padrão
        }

        boolean isReady = settings.getNotificationPermission() == NotificationPermissionEnum.PERMITIDO
                && settings.getLocationPermission() == LocationPermissionEnum.SEMPRE_PERMITIDA
                && residenceConfigured;

        return new SystemDiagnosticResponseDTO(
                isReady,
                settings.getNotificationPermission(),
                settings.getLocationPermission(),
                residenceConfigured,
                statusMessage
        );
    }

    private UserSettingsEntity createDefaultSettings(String deviceId) {
        UserSettingsEntity defaultSettings = UserSettingsEntity.builder()
                .deviceId(deviceId)
                .remindOnLeave(true)
                .vibrateOnNotify(true)
                .notificationPermission(NotificationPermissionEnum.PERMITIDO)
                .locationPermission(LocationPermissionEnum.SEMPRE_PERMITIDA)
                .build();
        return repository.save(defaultSettings);
    }
}