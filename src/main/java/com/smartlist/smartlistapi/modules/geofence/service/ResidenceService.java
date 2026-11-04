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

    private static final double EARTH_RADIUS_METERS = 6371000.0; // Raio da Terra em metros

    private final ResidenceRepository repository;

    @Transactional(readOnly = true, noRollbackFor = ResourceNotFoundException.class)
    public ResidenceResponseDTO getResidence() {
        ResidenceEntity entity = repository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ResourceNotFoundException("Residência ainda não configurada."));
        return ResidenceResponseDTO.fromEntity(entity);
    }

    @Transactional
    public ResidenceResponseDTO saveOrUpdateResidence(ResidenceRequestDTO dto) {
        ResidenceEntity entity = repository.findFirstByOrderByIdAsc()
                .orElseGet(ResidenceEntity::new);

        entity.setAddress(dto.address());
        entity.setComplement(dto.complement());
        entity.setLatitude(dto.latitude());
        entity.setLongitude(dto.longitude());
        entity.setRadiusMeters(dto.radiusMeters());
        entity.setGeofenceActive(dto.geofenceActive() != null ? dto.geofenceActive() : true);

        return ResidenceResponseDTO.fromEntity(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public GeofenceCheckResponseDTO checkGeofenceStatus(GeofenceCheckRequestDTO dto) {
        ResidenceEntity residence = repository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ResourceNotFoundException("Residência não configurada para cálculo de perímetro."));

        double distance = calculateHaversineDistance(
                dto.currentLatitude(), dto.currentLongitude(),
                residence.getLatitude(), residence.getLongitude()
        );

        boolean isInside = distance <= residence.getRadiusMeters();
        String message = isInside 
                ? "Dispositivo dentro do perímetro residencial" 
                : String.format("Afastamento de %.0fm da residência detectado", distance);

        return new GeofenceCheckResponseDTO(
                isInside,
                Math.round(distance * 100.0) / 100.0,
                residence.getRadiusMeters(),
                message
        );
    }

    // Fórmula de Haversine para calcular distância real em metros entre dois pontos (Lat, Lon)
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