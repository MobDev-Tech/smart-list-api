package com.smartlist.smartlistapi.modules.geofence.service;

import com.smartlist.smartlistapi.common.exception.ResourceNotFoundException;
import com.smartlist.smartlistapi.modules.geofence.domain.ResidenceEntity;
import com.smartlist.smartlistapi.modules.geofence.dto.GeofenceCheckRequestDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.GeofenceCheckResponseDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.ResidenceRequestDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.ResidenceResponseDTO;
import com.smartlist.smartlistapi.modules.geofence.repository.ResidenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResidenceService {

    private static final Logger logger = LoggerFactory.getLogger(ResidenceService.class);

    private static final double EARTH_RADIUS_METERS = 6371000.0;
    private static final int DEFAULT_MONITORING_RADIUS_METERS = 150;

    private final ResidenceRepository repository;

    /**
     * Recupera a residência cadastrada para o dispositivo.
     */
    @Transactional(readOnly = true, noRollbackFor = ResourceNotFoundException.class)
    public ResidenceResponseDTO getResidence(String deviceId) {
        ResidenceEntity entity = repository.findByDeviceId(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Residência ainda não configurada para este dispositivo."));
        return ResidenceResponseDTO.fromEntity(entity);
    }

    /**
     * Cria ou atualiza os dados residenciais e as configurações de geofence do dispositivo.
     */
    @Transactional
    public ResidenceResponseDTO saveOrUpdateResidence(String deviceId, ResidenceRequestDTO dto) {
        var existingResidence = repository.findByDeviceId(deviceId);
        ResidenceEntity entity = existingResidence
                .orElseGet(() -> ResidenceEntity.builder().deviceId(deviceId).build());

        entity.setAddress(dto.address());
        entity.setComplement(dto.complement());
        entity.setLatitude(dto.latitude());
        entity.setLongitude(dto.longitude());
        entity.setRadiusMeters(dto.radiusMeters() != null ? dto.radiusMeters() : DEFAULT_MONITORING_RADIUS_METERS);
        entity.setGeofenceActive(dto.geofenceActive() != null ? dto.geofenceActive() : true);

        ResidenceEntity savedResidence = repository.save(entity);
        // Identifica criação/atualização e estado do perímetro sem registrar endereço ou coordenadas.
        logger.info("residence_{} id={} geofenceActive={} radiusMeters={}",
                existingResidence.isPresent() ? "updated" : "created",
                savedResidence.getId(),
                savedResidence.isGeofenceActive(),
                savedResidence.getRadiusMeters());
        return ResidenceResponseDTO.fromEntity(savedResidence);
    }

    /**
     * Verifica se a posição atual está dentro do raio residencial configurado para o dispositivo.
     */
    @Transactional(readOnly = true)
    public GeofenceCheckResponseDTO checkGeofenceStatus(String deviceId, GeofenceCheckRequestDTO dto) {
        ResidenceEntity residence = repository.findByDeviceId(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Residência não configurada para este dispositivo para cálculo de perímetro."));

        double distance = calculateHaversineDistance(
                dto.currentLatitude(), dto.currentLongitude(),
                residence.getLatitude(), residence.getLongitude()
        );

        int radiusMeters = residence.getRadiusMeters();
        boolean isInside = distance <= radiusMeters;
        // Registra a decisão e a distância arredondada para ajudar a investigar o limite do perímetro.
        logger.info("geofence_check insidePerimeter={} distanceMeters={} radiusMeters={}",
                isInside, Math.round(distance), radiusMeters);
        String message = isInside 
                ? String.format("Dispositivo dentro do perímetro residencial (%dm)", radiusMeters)
                : String.format("Afastamento de %.0fm da residência detectado (limite: %dm)", distance, radiusMeters);

        return new GeofenceCheckResponseDTO(
                isInside,
                Math.round(distance * 100.0) / 100.0,
                radiusMeters,
                message
        );
    }

    /**
     * Calcula a distância em metros entre duas coordenadas geográficas pela fórmula de Haversine.
     */
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METERS * c;
    }
}
