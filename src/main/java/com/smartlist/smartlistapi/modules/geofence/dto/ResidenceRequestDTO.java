package com.smartlist.smartlistapi.modules.geofence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ResidenceRequestDTO(
    @NotBlank(message = "O endereço é obrigatório")
    String address,

    String complement,

    @NotNull(message = "A latitude é obrigatória")
    Double latitude,

    @NotNull(message = "A longitude é obrigatória")
    Double longitude,

    Boolean geofenceActive,

    @Min(value = 100, message = "O raio mínimo é 100 metros")
    @Max(value = 300, message = "O raio máximo é 300 metros")
    Integer radiusMeters
) {}
