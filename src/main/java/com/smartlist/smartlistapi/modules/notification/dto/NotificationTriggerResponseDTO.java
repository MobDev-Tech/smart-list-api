package com.smartlist.smartlistapi.modules.notification.dto;

import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingItemResponseDTO;

import java.util.List;

public record NotificationTriggerResponseDTO(
    boolean shouldNotify,
    String reason,
    long pendingItemsCount,
    List<ShoppingItemResponseDTO> pendingItemsSummary
) {}