package com.smartlist.smartlistapi.modules.geofence.dto;

public record GeofenceCheckResponseDTO(
    boolean insidePerimeter,
    double distanceFromHomeMeters,
    int allowedRadiusMeters,
    String statusMessage
) {}