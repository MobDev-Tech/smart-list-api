package com.smartlist.smartlistapi.modules.shoppinglist.dto;

import com.smartlist.smartlistapi.modules.shoppinglist.domain.ItemCategoryEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShoppingItemRequestDTO(
    @NotBlank(message = "O nome do produto é obrigatório")
    String name,

    String quantity,

    @NotNull(message = "A categoria é obrigatória")
    ItemCategoryEnum category
) {}