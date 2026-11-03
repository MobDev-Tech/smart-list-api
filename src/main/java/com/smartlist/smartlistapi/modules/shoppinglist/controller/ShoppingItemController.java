package com.smartlist.smartlistapi.modules.shoppinglist.controller;

import com.smartlist.smartlistapi.common.dto.ApiErrorResponse;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingItemRequestDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingItemResponseDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingListGroupedDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.service.ShoppingItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shopping-items")
@RequiredArgsConstructor
@Tag(name = "Lista de compras", description = "Gerenciamento de itens da lista de compras")
public class ShoppingItemController {

    private final ShoppingItemService service;

    @GetMapping
    @Operation(summary = "Listar itens agrupados por situação")
    public ResponseEntity<ShoppingListGroupedDTO> getAllItemsGrouped() {
        return ResponseEntity.ok(service.getGroupedItems());
    }

    @PostMapping
    @Operation(summary = "Adicionar item à lista")
    @ApiResponse(responseCode = "400", description = "Dados do item inválidos",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<ShoppingItemResponseDTO> createItem(@Valid @RequestBody ShoppingItemRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createItem(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item da lista")
    @ApiResponse(responseCode = "400", description = "Dados do item inválidos",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Item não encontrado",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<ShoppingItemResponseDTO> updateItem(@PathVariable Long id, @Valid @RequestBody ShoppingItemRequestDTO dto) {
        return ResponseEntity.ok(service.updateItem(id, dto));
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Alternar conclusão do item")
    @ApiResponse(responseCode = "404", description = "Item não encontrado",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<ShoppingItemResponseDTO> toggleItem(@PathVariable Long id) {
        return ResponseEntity.ok(service.toggleItemCompletion(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover item da lista")
    @ApiResponse(responseCode = "404", description = "Item não encontrado",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        service.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/completed")
    @Operation(summary = "Remover todos os itens concluídos")
    public ResponseEntity<Void> clearCompleted() {
        service.clearCompletedItems();
        return ResponseEntity.noContent().build();
    }
}