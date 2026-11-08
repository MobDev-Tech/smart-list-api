package com.smartlist.smartlistapi.modules.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false, unique = true)
    private String deviceId;

    @Column(name = "remind_on_leave", nullable = false)
    private boolean remindOnLeave;

    @Column(name = "vibrate_on_notify", nullable = false)
    private boolean vibrateOnNotify;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_permission", nullable = false)
    private NotificationPermissionEnum notificationPermission;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_permission", nullable = false)
    private LocationPermissionEnum locationPermission;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        this.updatedAt = LocalDateTime.now();
    }
}