package com.smartlist.smartlistapi.modules.shoppinglist.dto;

import com.smartlist.smartlistapi.modules.shoppinglist.domain.ItemCategoryEnum;
import com.smartlist.smartlistapi.modules.shoppinglist.domain.ShoppingItemEntity;

import java.time.LocalDateTime;

public record ShoppingItemResponseDTO(
    Long id,
    String name,
    String quantity,
    ItemCategoryEnum category,
    boolean completed,
    LocalDateTime createdAt
) {
    public static ShoppingItemResponseDTO fromEntity(ShoppingItemEntity item) {
        return new ShoppingItemResponseDTO(
            item.getId(),
            item.getName(),
            item.getQuantity(),
            item.getCategory(),
            item.isCompleted(),
            item.getCreatedAt()
        );
    }
}