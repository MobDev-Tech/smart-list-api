package com.smartlist.smartlistapi.modules.shoppinglist.service;

import com.smartlist.smartlistapi.common.exception.ResourceNotFoundException;
import com.smartlist.smartlistapi.modules.shoppinglist.domain.ShoppingItemEntity;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingItemRequestDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingItemResponseDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingListGroupedDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.repository.ShoppingItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingItemService {

    private final ShoppingItemRepository repository;

    @Transactional(readOnly = true)
    public ShoppingListGroupedDTO getGroupedItems(String deviceId) {
        List<ShoppingItemResponseDTO> pending = repository.findByDeviceIdAndCompletedFalseOrderByCreatedAtDesc(deviceId)
                .stream()
                .map(ShoppingItemResponseDTO::fromEntity)
                .toList();

        List<ShoppingItemResponseDTO> completed = repository.findByDeviceIdAndCompletedTrueOrderByUpdatedAtDesc(deviceId)
                .stream()
                .map(ShoppingItemResponseDTO::fromEntity)
                .toList();

        return new ShoppingListGroupedDTO(
                pending.size(),
                pending,
                completed.size(),
                completed
        );
    }

    @Transactional
    public ShoppingItemResponseDTO createItem(String deviceId, ShoppingItemRequestDTO dto) {
        ShoppingItemEntity item = ShoppingItemEntity.builder()
                .deviceId(deviceId)
                .name(dto.name())
                .quantity(dto.quantity())
                .category(dto.category())
                .build();

        return ShoppingItemResponseDTO.fromEntity(repository.save(item));
    }

    @Transactional
    public ShoppingItemResponseDTO updateItem(String deviceId, Long id, ShoppingItemRequestDTO dto) {
        ShoppingItemEntity item = findEntityByIdAndDeviceId(id, deviceId);
        item.setName(dto.name());
        item.setQuantity(dto.quantity());
        item.setCategory(dto.category());

        return ShoppingItemResponseDTO.fromEntity(repository.save(item));
    }

    @Transactional
    public ShoppingItemResponseDTO toggleItemCompletion(String deviceId, Long id) {
        ShoppingItemEntity item = findEntityByIdAndDeviceId(id, deviceId);
        item.setCompleted(!item.isCompleted());

        return ShoppingItemResponseDTO.fromEntity(repository.save(item));
    }

    @Transactional
    public void deleteItem(String deviceId, Long id) {
        ShoppingItemEntity item = findEntityByIdAndDeviceId(id, deviceId);
        repository.delete(item);
    }

    @Transactional
    public void clearCompletedItems(String deviceId) {
        repository.deleteByDeviceIdAndCompletedTrue(deviceId);
    }

    private ShoppingItemEntity findEntityByIdAndDeviceId(Long id, String deviceId) {
        return repository.findByIdAndDeviceId(id, deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com o ID " + id + " para este dispositivo."));
    }
}