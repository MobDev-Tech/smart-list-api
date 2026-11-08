package com.smartlist.smartlistapi.modules.user.dto;

import com.smartlist.smartlistapi.modules.user.domain.LocationPermissionEnum;
import com.smartlist.smartlistapi.modules.user.domain.NotificationPermissionEnum;

public record SystemDiagnosticResponseDTO(
    boolean isSystemReady,
    NotificationPermissionEnum notificationPermission,
    LocationPermissionEnum locationPermission,
    boolean isResidenceConfigured,
    String residenceStatusMessage
) {}