package com.smartlist.smartlistapi.modules.notification.controller;

import com.smartlist.smartlistapi.common.dto.ApiErrorResponse;
import com.smartlist.smartlistapi.modules.notification.dto.NotificationTriggerRequestDTO;
import com.smartlist.smartlistapi.modules.notification.dto.NotificationTriggerResponseDTO;
import com.smartlist.smartlistapi.modules.notification.dto.SnoozeRequestDTO;
import com.smartlist.smartlistapi.modules.notification.dto.SnoozeResponseDTO;
import com.smartlist.smartlistapi.modules.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notificações", description = "Controle de lembretes e avaliação dos gatilhos de notificação")
public class NotificationController {

    private final NotificationService service;

    @PostMapping("/snooze")
    @Operation(summary = "Pausar lembretes")
    @ApiResponse(responseCode = "400", description = "Opção de pausa inválida",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<SnoozeResponseDTO> applySnooze(
            @RequestHeader("X-Device-ID") String deviceId,
            @Valid @RequestBody SnoozeRequestDTO dto) {
        return ResponseEntity.ok(service.applySnooze(deviceId, dto));
    }

    @DeleteMapping("/snooze")
    @Operation(summary = "Reativar lembretes")
    public ResponseEntity<SnoozeResponseDTO> cancelSnooze(
            @RequestHeader("X-Device-ID") String deviceId) {
        return ResponseEntity.ok(service.cancelSnooze(deviceId));
    }

    @PostMapping("/trigger-check")
    @Operation(summary = "Avaliar se uma notificação deve ser disparada")
    @ApiResponse(responseCode = "400", description = "Coordenadas inválidas",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Residência ainda não configurada",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<NotificationTriggerResponseDTO> checkTrigger(
            @RequestHeader("X-Device-ID") String deviceId,
            @Valid @RequestBody NotificationTriggerRequestDTO dto) {
        return ResponseEntity.ok(service.evaluateNotificationTrigger(deviceId, dto));
    }
}