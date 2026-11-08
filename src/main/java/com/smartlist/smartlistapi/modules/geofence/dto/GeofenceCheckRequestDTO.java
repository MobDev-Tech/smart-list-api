package com.smartlist.smartlistapi.modules.geofence.dto;

import jakarta.validation.constraints.NotNull;

public record GeofenceCheckRequestDTO(
    @NotNull(message = "A latitude atual é obrigatória")
    Double currentLatitude,

    @NotNull(message = "A longitude atual é obrigatória")
    Double currentLongitude
) {}