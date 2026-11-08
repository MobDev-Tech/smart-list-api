package com.smartlist.smartlistapi.modules.geofence.dto;

import com.smartlist.smartlistapi.modules.geofence.domain.ResidenceEntity;

public record ResidenceResponseDTO(
    Long id,
    String address,
    String complement,
    Double latitude,
    Double longitude,
    Integer radiusMeters,
    boolean geofenceActive
) {
    public static ResidenceResponseDTO fromEntity(ResidenceEntity entity) {
        return new ResidenceResponseDTO(
            entity.getId(),
            entity.getAddress(),
            entity.getComplement(),
            entity.getLatitude(),
            entity.getLongitude(),
            entity.getRadiusMeters(),
            entity.isGeofenceActive()
        );
    }
}