package com.smartlist.smartlistapi.modules.geofence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResidenceRequestDTO(
    @NotBlank(message = "O endereço é obrigatório")
    String address,

    String complement,

    @NotNull(message = "A latitude é obrigatória")
    Double latitude,

    @NotNull(message = "A longitude é obrigatória")
    Double longitude,

    Boolean geofenceActive
) {}