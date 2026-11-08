package com.smartlist.smartlistapi.modules.user.controller;

import com.smartlist.smartlistapi.common.dto.ApiErrorResponse;
import com.smartlist.smartlistapi.modules.user.dto.SystemDiagnosticResponseDTO;
import com.smartlist.smartlistapi.modules.user.dto.UserSettingsRequestDTO;
import com.smartlist.smartlistapi.modules.user.dto.UserSettingsResponseDTO;
import com.smartlist.smartlistapi.modules.user.service.UserSettingsService;
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
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
@Tag(name = "Configurações", description = "Preferências e diagnóstico do aplicativo")
public class UserSettingsController {

    private final UserSettingsService service;

    @GetMapping
    @Operation(summary = "Consultar configurações")
    public ResponseEntity<UserSettingsResponseDTO> getSettings(
            @RequestHeader("X-Device-ID") String deviceId) {
        return ResponseEntity.ok(service.getSettings(deviceId));
    }

    @PutMapping
    @Operation(summary = "Atualizar configurações")
    @ApiResponse(responseCode = "400", description = "Dados das configurações inválidos",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<UserSettingsResponseDTO> updateSettings(
            @RequestHeader("X-Device-ID") String deviceId,
            @Valid @RequestBody UserSettingsRequestDTO dto) {
        return ResponseEntity.ok(service.updateSettings(deviceId, dto));
    }

    @GetMapping("/diagnostic")
    @Operation(summary = "Consultar diagnóstico do sistema")
    public ResponseEntity<SystemDiagnosticResponseDTO> getDiagnostic(
            @RequestHeader("X-Device-ID") String deviceId) {
        return ResponseEntity.ok(service.getDiagnostic(deviceId));
    }
}