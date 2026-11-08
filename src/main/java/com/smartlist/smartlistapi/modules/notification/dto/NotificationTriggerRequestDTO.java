package com.smartlist.smartlistapi.modules.notification.dto;

import jakarta.validation.constraints.NotNull;

public record NotificationTriggerRequestDTO(
    @NotNull(message = "A latitude é obrigatória")
    Double currentLatitude,

    @NotNull(message = "A longitude é obrigatória")
    Double currentLongitude
) {}