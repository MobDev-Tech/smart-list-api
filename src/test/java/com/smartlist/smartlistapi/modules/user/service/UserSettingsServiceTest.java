package com.smartlist.smartlistapi.modules.user.service;

import com.smartlist.smartlistapi.common.exception.ResourceNotFoundException;
import com.smartlist.smartlistapi.modules.geofence.dto.ResidenceResponseDTO;
import com.smartlist.smartlistapi.modules.geofence.service.ResidenceService;
import com.smartlist.smartlistapi.modules.user.domain.LocationPermissionEnum;
import com.smartlist.smartlistapi.modules.user.domain.NotificationPermissionEnum;
import com.smartlist.smartlistapi.modules.user.domain.UserSettingsEntity;
import com.smartlist.smartlistapi.modules.user.dto.SystemDiagnosticResponseDTO;
import com.smartlist.smartlistapi.modules.user.dto.UserSettingsRequestDTO;
import com.smartlist.smartlistapi.modules.user.dto.UserSettingsResponseDTO;
import com.smartlist.smartlistapi.modules.user.repository.UserSettingsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do serviço de configurações do usuário")
class UserSettingsServiceTest {

    private static final String DEVICE_ID = "device-1";

    @Mock
    private UserSettingsRepository repository;

    @Mock
    private ResidenceService residenceService;

    @InjectMocks
    private UserSettingsService service;

    @Test
    @DisplayName("Deve criar configurações padrão quando não existem")
    void dadasConfiguracoesInexistentes_quandoConsultadas_entaoCriarValoresPadrao() {
        when(repository.findByDeviceId(DEVICE_ID)).thenReturn(Optional.empty());
        when(repository.save(any(UserSettingsEntity.class))).thenAnswer(invocation -> {
            UserSettingsEntity saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        UserSettingsResponseDTO result = service.getSettings(DEVICE_ID);

        assertEquals(1L, result.id());
        assertTrue(result.remindOnLeave());
        assertTrue(result.vibrateOnNotify());
        assertEquals(NotificationPermissionEnum.PERMITIDO, result.notificationPermission());
        assertEquals(LocationPermissionEnum.SEMPRE_PERMITIDA, result.locationPermission());
    }

    @Test
    @DisplayName("Deve atualizar e salvar as configurações existentes")
    void dadasConfiguracoesExistentes_quandoAtualizadas_entaoPersistirAlteracoes() {
        UserSettingsEntity settings = settings();
        when(repository.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(settings));
        when(repository.save(settings)).thenReturn(settings);
        UserSettingsRequestDTO request = new UserSettingsRequestDTO(
                false, false, NotificationPermissionEnum.NEGADO, LocationPermissionEnum.NEGADO);

        UserSettingsResponseDTO result = service.updateSettings(DEVICE_ID, request);

        assertFalse(result.remindOnLeave());
        assertFalse(result.vibrateOnNotify());
        assertEquals(NotificationPermissionEnum.NEGADO, result.notificationPermission());
        assertEquals(LocationPermissionEnum.NEGADO, result.locationPermission());
        verify(repository).save(settings);
    }

    @Test
    @DisplayName("Deve indicar que o sistema está pronto com permissões e residência configuradas")
    void dadasPermissoesEResidenciaConfiguradas_quandoDiagnosticoConsultado_entaoIndicarSistemaPronto() {
        when(repository.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(settings()));
        when(residenceService.getResidence(DEVICE_ID)).thenReturn(
                new ResidenceResponseDTO(1L, "Rua das Flores", null, -23.5, -46.6, 150, true));

        SystemDiagnosticResponseDTO result = service.getDiagnostic(DEVICE_ID);

        assertTrue(result.isSystemReady());
        assertTrue(result.isResidenceConfigured());
        assertEquals("Monitoramento Geofence Ativo", result.residenceStatusMessage());
    }

    @Test
    @DisplayName("Deve indicar que o sistema não está pronto sem residência configurada")
    void dadaResidenciaNaoConfigurada_quandoDiagnosticoConsultado_entaoIndicarSistemaNaoPronto() {
        when(repository.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(settings()));
        when(residenceService.getResidence(DEVICE_ID)).thenThrow(
                new ResourceNotFoundException("Residência ainda não configurada."));

        SystemDiagnosticResponseDTO result = service.getDiagnostic(DEVICE_ID);

        assertFalse(result.isSystemReady());
        assertFalse(result.isResidenceConfigured());
        assertEquals("Residência não configurada", result.residenceStatusMessage());
    }

    @Test
    @DisplayName("Deve informar quando o geofence está desativado")
    void dadaResidenciaComGeofenceDesativado_quandoDiagnosticoConsultado_entaoInformarMonitoramentoDesativado() {
        when(repository.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(settings()));
        when(residenceService.getResidence(DEVICE_ID)).thenReturn(
                new ResidenceResponseDTO(1L, "Rua das Flores", null, -23.5, -46.6, 150, false));

        SystemDiagnosticResponseDTO result = service.getDiagnostic(DEVICE_ID);

        assertTrue(result.isResidenceConfigured());
        assertTrue(result.isSystemReady());
        assertEquals("Monitoramento Desativado", result.residenceStatusMessage());
    }

    private UserSettingsEntity settings() {
        return UserSettingsEntity.builder()
                .id(1L)
                .deviceId(DEVICE_ID)
                .remindOnLeave(true)
                .vibrateOnNotify(true)
                .notificationPermission(NotificationPermissionEnum.PERMITIDO)
                .locationPermission(LocationPermissionEnum.SEMPRE_PERMITIDA)
                .build();
    }
}
