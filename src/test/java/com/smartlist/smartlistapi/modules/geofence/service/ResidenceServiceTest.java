package com.smartlist.smartlistapi.modules.geofence.service;

import com.smartlist.smartlistapi.common.exception.ResourceNotFoundException;
import com.smartlist.smartlistapi.modules.geofence.domain.ResidenceEntity;
import com.smartlist.smartlistapi.modules.geofence.dto.GeofenceCheckRequestDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.GeofenceCheckResponseDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.ResidenceRequestDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.ResidenceResponseDTO;
import com.smartlist.smartlistapi.modules.geofence.repository.ResidenceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do serviço de residência")
class ResidenceServiceTest {

    private static final String DEVICE_ID = "device-1";

    @Mock
    private ResidenceRepository repository;

    @InjectMocks
    private ResidenceService service;

    @Test
    @DisplayName("Deve retornar a residência configurada")
    void dadaResidenciaConfigurada_quandoConsultada_entaoRetornarSeusDados() {
        ResidenceEntity residence = residence();
        when(repository.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(residence));

        ResidenceResponseDTO result = service.getResidence(DEVICE_ID);

        assertEquals(1L, result.id());
        assertEquals("Rua das Flores", result.address());
        assertEquals(150, result.radiusMeters());
        assertTrue(result.geofenceActive());
    }

    @Test
    @DisplayName("Deve lançar exceção quando não há residência configurada")
    void dadaResidenciaNaoConfigurada_quandoConsultada_entaoLancarExcecao() {
        when(repository.findByDeviceId(DEVICE_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getResidence(DEVICE_ID));
    }

    @Test
    @DisplayName("Deve salvar residência nova e ativar geofence por padrão")
    void dadaNovaResidencia_quandoSalvaSemEstadoExplicito_entaoAtivarGeofencePorPadrao() {
        when(repository.findByDeviceId(DEVICE_ID)).thenReturn(Optional.empty());
        when(repository.save(any(ResidenceEntity.class))).thenAnswer(invocation -> {
            ResidenceEntity saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });
        ResidenceRequestDTO request = new ResidenceRequestDTO(
                "Rua das Flores", "Casa 2", -23.5, -46.6, null);

        ResidenceResponseDTO result = service.saveOrUpdateResidence(DEVICE_ID, request);

        assertEquals("Rua das Flores", result.address());
        assertEquals("Casa 2", result.complement());
        assertEquals(150, result.radiusMeters());
        assertTrue(result.geofenceActive());
        verify(repository).save(any(ResidenceEntity.class));
    }

    @Test
    @DisplayName("Deve atualizar residência existente e manter geofence desativado")
    void dadaResidenciaExistente_quandoAtualizadaComGeofenceDesativado_entaoPreservarEstadoDesativado() {
        ResidenceEntity residence = residence();
        when(repository.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(residence));
        when(repository.save(residence)).thenReturn(residence);
        ResidenceRequestDTO request = new ResidenceRequestDTO(
                "Avenida Central", null, -22.9, -43.2, false);

        ResidenceResponseDTO result = service.saveOrUpdateResidence(DEVICE_ID, request);

        assertEquals("Avenida Central", result.address());
        assertEquals(-22.9, result.latitude());
        assertEquals(150, result.radiusMeters());
        assertFalse(result.geofenceActive());
        verify(repository).save(residence);
    }

    @Test
    @DisplayName("Deve indicar que o dispositivo está dentro do perímetro")
    void dadaResidenciaConfigurada_quandoCoordenadasCorrespondem_entaoIndicarDentroDoPerimetro() {
        when(repository.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(residence()));

        GeofenceCheckResponseDTO result = service.checkGeofenceStatus(
                DEVICE_ID, new GeofenceCheckRequestDTO(-23.5, -46.6));

        assertTrue(result.insidePerimeter());
        assertEquals(0.0, result.distanceFromHomeMeters());
        assertEquals(150, result.allowedRadiusMeters());
        assertEquals("Dispositivo dentro do perímetro residencial (150m)", result.statusMessage());
    }

    @Test
    @DisplayName("Deve calcular a distância e indicar que o dispositivo está fora do perímetro")
    void dadaResidenciaConfigurada_quandoCoordenadasEstaoDistantes_entaoCalcularDistanciaEIndicarFora() {
        when(repository.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(residence()));

        GeofenceCheckResponseDTO result = service.checkGeofenceStatus(
                DEVICE_ID, new GeofenceCheckRequestDTO(-23.5, -46.61));

        assertFalse(result.insidePerimeter());
        assertEquals(1019.72, result.distanceFromHomeMeters(), 0.1);
        assertEquals(150, result.allowedRadiusMeters());
        assertEquals("Afastamento de 1020m da residência detectado (limite: 150m)", result.statusMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao verificar geofence sem residência configurada")
    void dadaResidenciaNaoConfigurada_quandoGeofenceVerificado_entaoLancarExcecao() {
        when(repository.findByDeviceId(DEVICE_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                service.checkGeofenceStatus(DEVICE_ID, new GeofenceCheckRequestDTO(-23.5, -46.6)));
    }

    private ResidenceEntity residence() {
        return ResidenceEntity.builder()
                .id(1L)
                .deviceId(DEVICE_ID)
                .address("Rua das Flores")
                .complement("Casa 2")
                .latitude(-23.5)
                .longitude(-46.6)
                .radiusMeters(150)
                .geofenceActive(true)
                .build();
    }
}
