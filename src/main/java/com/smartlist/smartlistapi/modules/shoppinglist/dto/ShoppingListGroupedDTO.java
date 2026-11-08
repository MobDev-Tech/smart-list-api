package com.smartlist.smartlistapi.modules.shoppinglist.dto;

import java.util.List;

public record ShoppingListGroupedDTO(
    long pendingCount,
    List<ShoppingItemResponseDTO> pendingItems,
    long completedCount,
    List<ShoppingItemResponseDTO> completedItems
) {}