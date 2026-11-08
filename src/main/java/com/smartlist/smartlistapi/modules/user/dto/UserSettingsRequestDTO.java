package com.smartlist.smartlistapi.modules.user.dto;

import com.smartlist.smartlistapi.modules.user.domain.LocationPermissionEnum;
import com.smartlist.smartlistapi.modules.user.domain.NotificationPermissionEnum;
import jakarta.validation.constraints.NotNull;

public record UserSettingsRequestDTO(
    @NotNull(message = "O parâmetro de lembrete ao sair de casa é obrigatório")
    Boolean remindOnLeave,

    @NotNull(message = "O parâmetro de vibração é obrigatório")
    Boolean vibrateOnNotify,

    @NotNull(message = "O status da permissão de notificação é obrigatório")
    NotificationPermissionEnum notificationPermission,

    @NotNull(message = "O status da permissão de localização é obrigatório")
    LocationPermissionEnum locationPermission
) {}