package com.smartlist.smartlistapi.modules.user.dto;

import com.smartlist.smartlistapi.modules.user.domain.LocationPermissionEnum;
import com.smartlist.smartlistapi.modules.user.domain.NotificationPermissionEnum;
import com.smartlist.smartlistapi.modules.user.domain.UserSettingsEntity;

public record UserSettingsResponseDTO(
    Long id,
    boolean remindOnLeave,
    boolean vibrateOnNotify,
    NotificationPermissionEnum notificationPermission,
    LocationPermissionEnum locationPermission
) {
    public static UserSettingsResponseDTO fromEntity(UserSettingsEntity entity) {
        return new UserSettingsResponseDTO(
            entity.getId(),
            entity.isRemindOnLeave(),
            entity.isVibrateOnNotify(),
            entity.getNotificationPermission(),
            entity.getLocationPermission()
        );
    }
}