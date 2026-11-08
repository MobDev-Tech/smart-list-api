package com.smartlist.smartlistapi.modules.geofence.service;

import com.smartlist.smartlistapi.common.exception.ResourceNotFoundException;
import com.smartlist.smartlistapi.modules.geofence.domain.ResidenceEntity;
import com.smartlist.smartlistapi.modules.geofence.dto.GeofenceCheckRequestDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.GeofenceCheckResponseDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.ResidenceRequestDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.ResidenceResponseDTO;
import com.smartlist.smartlistapi.modules.geofence.repository.ResidenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResidenceService {

    private static final double EARTH_RADIUS_METERS = 6371000.0;
    private static final int FIXED_MONITORING_RADIUS_METERS = 150; // Raio fixo de 150m para o MVP

    private final ResidenceRepository repository;

    @Transactional(readOnly = true, noRollbackFor = ResourceNotFoundException.class)
    public ResidenceResponseDTO getResidence(String deviceId) {
        ResidenceEntity entity = repository.findByDeviceId(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Residência ainda não configurada para este dispositivo."));
        return ResidenceResponseDTO.fromEntity(entity);
    }

    @Transactional
    public ResidenceResponseDTO saveOrUpdateResidence(String deviceId, ResidenceRequestDTO dto) {
        ResidenceEntity entity = repository.findByDeviceId(deviceId)
                .orElseGet(() -> ResidenceEntity.builder().deviceId(deviceId).build());

        entity.setAddress(dto.address());
        entity.setComplement(dto.complement());
        entity.setLatitude(dto.latitude());
        entity.setLongitude(dto.longitude());
        entity.setRadiusMeters(FIXED_MONITORING_RADIUS_METERS); // Força sempre 150m
        entity.setGeofenceActive(dto.geofenceActive() != null ? dto.geofenceActive() : true);

        return ResidenceResponseDTO.fromEntity(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public GeofenceCheckResponseDTO checkGeofenceStatus(String deviceId, GeofenceCheckRequestDTO dto) {
        ResidenceEntity residence = repository.findByDeviceId(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Residência não configurada para este dispositivo para cálculo de perímetro."));

        double distance = calculateHaversineDistance(
                dto.currentLatitude(), dto.currentLongitude(),
                residence.getLatitude(), residence.getLongitude()
        );

        // Valida contra o raio fixo de 150 metros
        boolean isInside = distance <= FIXED_MONITORING_RADIUS_METERS;
        String message = isInside 
                ? "Dispositivo dentro do perímetro residencial (150m)" 
                : String.format("Afastamento de %.0fm da residência detectado (limite: 150m)", distance);

        return new GeofenceCheckResponseDTO(
                isInside,
                Math.round(distance * 100.0) / 100.0,
                FIXED_MONITORING_RADIUS_METERS,
                message
        );
    }

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