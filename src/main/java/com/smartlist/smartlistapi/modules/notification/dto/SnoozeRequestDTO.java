package com.smartlist.smartlistapi.modules.notification.dto;

import com.smartlist.smartlistapi.modules.notification.domain.SnoozeOptionEnum;
import jakarta.validation.constraints.NotNull;

public record SnoozeRequestDTO(
    @NotNull(message = "A opção de soneca é obrigatória")
    SnoozeOptionEnum option
) {}