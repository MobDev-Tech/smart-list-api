package com.smartlist.smartlistapi.modules.geofence.controller;

import com.smartlist.smartlistapi.common.dto.ApiErrorResponse;
import com.smartlist.smartlistapi.modules.geofence.dto.GeofenceCheckRequestDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.GeofenceCheckResponseDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.ResidenceRequestDTO;
import com.smartlist.smartlistapi.modules.geofence.dto.ResidenceResponseDTO;
import com.smartlist.smartlistapi.modules.geofence.service.ResidenceService;
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
@RequestMapping("/api/v1/residence")
@RequiredArgsConstructor
@Tag(name = "Residência e geofence", description = "Configuração da residência e verificação do perímetro")
public class ResidenceController {

    private final ResidenceService service;

    @GetMapping
    @Operation(summary = "Consultar residência configurada")
    @ApiResponse(responseCode = "404", description = "Residência ainda não configurada",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<ResidenceResponseDTO> getResidence() {
        return ResponseEntity.ok(service.getResidence());
    }

    @PostMapping
    @Operation(summary = "Criar ou atualizar residência")
    @ApiResponse(responseCode = "400", description = "Dados inválidos",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<ResidenceResponseDTO> saveOrUpdateResidence(@Valid @RequestBody ResidenceRequestDTO dto) {
        return ResponseEntity.ok(service.saveOrUpdateResidence(dto));
    }

    @PostMapping("/check-geofence")
    @Operation(summary = "Verificar se o dispositivo está no perímetro residencial")
    @ApiResponse(responseCode = "400", description = "Coordenadas inválidas",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Residência ainda não configurada",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<GeofenceCheckResponseDTO> checkGeofence(@Valid @RequestBody GeofenceCheckRequestDTO dto) {
        return ResponseEntity.ok(service.checkGeofenceStatus(dto));
    }
}