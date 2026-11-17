package com.smartlist.smartlistapi.modules.shoppinglist.service;

import com.smartlist.smartlistapi.common.exception.ResourceNotFoundException;
import com.smartlist.smartlistapi.modules.shoppinglist.domain.ShoppingItemEntity;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingItemRequestDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingItemResponseDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingListGroupedDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.repository.ShoppingItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingItemService {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingItemService.class);

    private final ShoppingItemRepository repository;

    /**
     * Busca os itens do dispositivo e os separa entre pendentes e concluídos.
     */
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

    /**
     * Cria um item de compra associado ao dispositivo informado.
     */
    @Transactional
    public ShoppingItemResponseDTO createItem(String deviceId, ShoppingItemRequestDTO dto) {
        ShoppingItemEntity item = ShoppingItemEntity.builder()
                .deviceId(deviceId)
                .name(dto.name())
                .quantity(dto.quantity())
                .category(dto.category())
                .build();

        ShoppingItemEntity savedItem = repository.save(item);
        // Registra o ID criado sem expor o conteúdo do item ou o dispositivo associado.
        logger.info("shopping_item_created itemId={}", savedItem.getId());
        return ShoppingItemResponseDTO.fromEntity(savedItem);
    }

    /**
     * Atualiza nome, quantidade e categoria de um item pertencente ao dispositivo.
     */
    @Transactional
    public ShoppingItemResponseDTO updateItem(String deviceId, Long id, ShoppingItemRequestDTO dto) {
        ShoppingItemEntity item = findEntityByIdAndDeviceId(id, deviceId);
        item.setName(dto.name());
        item.setQuantity(dto.quantity());
        item.setCategory(dto.category());

        ShoppingItemEntity savedItem = repository.save(item);
        // Registra qual item foi atualizado para permitir rastrear a alteração.
        logger.info("shopping_item_updated itemId={}", savedItem.getId());
        return ShoppingItemResponseDTO.fromEntity(savedItem);
    }

    /**
     * Inverte o estado de conclusão de um item pertencente ao dispositivo.
     */
    @Transactional
    public ShoppingItemResponseDTO toggleItemCompletion(String deviceId, Long id) {
        ShoppingItemEntity item = findEntityByIdAndDeviceId(id, deviceId);
        item.setCompleted(!item.isCompleted());

        ShoppingItemEntity savedItem = repository.save(item);
        // Registra a mudança de conclusão e o novo estado do item.
        logger.info("shopping_item_completion_changed itemId={} completed={}",
                savedItem.getId(), savedItem.isCompleted());
        return ShoppingItemResponseDTO.fromEntity(savedItem);
    }

    /**
     * Exclui um item pertencente ao dispositivo informado.
     */
    @Transactional
    public void deleteItem(String deviceId, Long id) {
        ShoppingItemEntity item = findEntityByIdAndDeviceId(id, deviceId);
        repository.delete(item);
        // Confirma no log a exclusão do item identificado.
        logger.info("shopping_item_deleted itemId={}", id);
    }

    /**
     * Exclui todos os itens concluídos do dispositivo informado.
     */
    @Transactional
    public void clearCompletedItems(String deviceId) {
        repository.deleteByDeviceIdAndCompletedTrue(deviceId);
        // Indica que a limpeza dos itens concluídos foi executada, sem registrar o dispositivo.
        logger.info("completed_shopping_items_cleared");
    }

    /**
     * Busca um item pelo ID e dispositivo, lançando exceção se não for encontrado.
     */
    private ShoppingItemEntity findEntityByIdAndDeviceId(Long id, String deviceId) {
        return repository.findByIdAndDeviceId(id, deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com o ID " + id + " para este dispositivo."));
    }
}