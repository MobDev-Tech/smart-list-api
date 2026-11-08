package com.smartlist.smartlistapi.modules.notification.dto;

import com.smartlist.smartlistapi.modules.notification.domain.SnoozeEntity;
import com.smartlist.smartlistapi.modules.notification.domain.SnoozeOptionEnum;

import java.time.LocalDateTime;

public record SnoozeResponseDTO(
    boolean active,
    SnoozeOptionEnum option,
    LocalDateTime snoozedUntil,
    String message
) {
    public static SnoozeResponseDTO fromEntity(SnoozeEntity entity, String message) {
        return new SnoozeResponseDTO(
            entity.isActive(),
            entity.getOption(),
            entity.getSnoozedUntil(),
            message
        );
    }
}