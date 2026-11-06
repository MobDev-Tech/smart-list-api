package com.smartlist.smartlistapi.modules.shoppinglist.service;

import com.smartlist.smartlistapi.common.exception.ResourceNotFoundException;
import com.smartlist.smartlistapi.modules.shoppinglist.domain.ItemCategoryEnum;
import com.smartlist.smartlistapi.modules.shoppinglist.domain.ShoppingItemEntity;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingItemRequestDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingItemResponseDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.dto.ShoppingListGroupedDTO;
import com.smartlist.smartlistapi.modules.shoppinglist.repository.ShoppingItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do serviço de itens da lista de compras")
class ShoppingItemServiceTest {

    private static final String DEVICE_ID = "device-1";

    @Mock
    private ShoppingItemRepository repository;

    @InjectMocks
    private ShoppingItemService service;

    @Test
    @DisplayName("Deve agrupar itens pendentes e concluídos")
    void dadaListaComItensPendentesEConcluidos_quandoAgrupada_entaoRetornarCadaGrupo() {
        ShoppingItemEntity pending = item(1L, "Leite", false);
        ShoppingItemEntity completed = item(2L, "Pão", true);
        when(repository.findByDeviceIdAndCompletedFalseOrderByCreatedAtDesc(DEVICE_ID)).thenReturn(List.of(pending));
        when(repository.findByDeviceIdAndCompletedTrueOrderByUpdatedAtDesc(DEVICE_ID)).thenReturn(List.of(completed));

        ShoppingListGroupedDTO result = service.getGroupedItems(DEVICE_ID);

        assertEquals(1, result.pendingCount());
        assertEquals("Leite", result.pendingItems().getFirst().name());
        assertEquals(1, result.completedCount());
        assertEquals("Pão", result.completedItems().getFirst().name());
    }

    @Test
    @DisplayName("Deve salvar um item e retornar os dados informados")
    void dadaRequisicaoValida_quandoItemCriado_entaoSalvarERetornarSeusDados() {
        when(repository.save(any(ShoppingItemEntity.class))).thenAnswer(invocation -> {
            ShoppingItemEntity saved = invocation.getArgument(0);
            saved.setId(5L);
            return saved;
        });
        ShoppingItemRequestDTO request = new ShoppingItemRequestDTO(
                "Arroz", "2 kg", ItemCategoryEnum.MERCEARIA);

        ShoppingItemResponseDTO result = service.createItem(DEVICE_ID, request);

        assertEquals(5L, result.id());
        assertEquals("Arroz", result.name());
        assertEquals("2 kg", result.quantity());
        assertEquals(ItemCategoryEnum.MERCEARIA, result.category());
        verify(repository).save(any(ShoppingItemEntity.class));
    }

    @Test
    @DisplayName("Deve atualizar um item existente")
    void dadoItemExistente_quandoAtualizado_entaoPersistirAsAlteracoes() {
        ShoppingItemEntity item = item(3L, "Leite", false);
        when(repository.findByIdAndDeviceId(3L, DEVICE_ID)).thenReturn(Optional.of(item));
        when(repository.save(item)).thenReturn(item);

        ShoppingItemResponseDTO result = service.updateItem(
                DEVICE_ID, 3L, new ShoppingItemRequestDTO("Leite integral", "1 L", ItemCategoryEnum.LATICINIOS));

        assertEquals("Leite integral", result.name());
        assertEquals("1 L", result.quantity());
        assertEquals(ItemCategoryEnum.LATICINIOS, result.category());
        verify(repository).save(item);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar item inexistente")
    void dadoItemInexistente_quandoAtualizado_entaoLancarExcecao() {
        when(repository.findByIdAndDeviceId(42L, DEVICE_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateItem(
                DEVICE_ID, 42L, new ShoppingItemRequestDTO("Leite", "1 L", ItemCategoryEnum.LATICINIOS)));
        verify(repository, never()).save(any(ShoppingItemEntity.class));
    }

    @Test
    @DisplayName("Deve alternar o estado de conclusão do item")
    void dadoItemPendente_quandoConclusaoAlternada_entaoMarcarComoConcluido() {
        ShoppingItemEntity item = item(3L, "Leite", false);
        when(repository.findByIdAndDeviceId(3L, DEVICE_ID)).thenReturn(Optional.of(item));
        when(repository.save(item)).thenReturn(item);

        ShoppingItemResponseDTO result = service.toggleItemCompletion(DEVICE_ID, 3L);

        assertTrue(result.completed());
        verify(repository).save(item);
    }

    @Test
    @DisplayName("Deve excluir o item encontrado")
    void dadoItemExistente_quandoExcluido_entaoRemoverDoRepositorio() {
        ShoppingItemEntity item = item(3L, "Leite", false);
        when(repository.findByIdAndDeviceId(3L, DEVICE_ID)).thenReturn(Optional.of(item));

        service.deleteItem(DEVICE_ID, 3L);

        verify(repository).delete(item);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir item inexistente")
    void dadoItemInexistente_quandoExcluido_entaoLancarExcecao() {
        when(repository.findByIdAndDeviceId(42L, DEVICE_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteItem(DEVICE_ID, 42L));
        verify(repository, never()).delete(any(ShoppingItemEntity.class));
    }

    @Test
    @DisplayName("Deve remover todos os itens concluídos")
    void dadosItensConcluidos_quandoLimpezaExecutada_entaoRemoverTodos() {
        service.clearCompletedItems(DEVICE_ID);

        verify(repository).deleteByDeviceIdAndCompletedTrue(DEVICE_ID);
    }

    private ShoppingItemEntity item(Long id, String name, boolean completed) {
        return ShoppingItemEntity.builder()
                .id(id)
                .deviceId(DEVICE_ID)
                .name(name)
                .quantity("1")
                .category(ItemCategoryEnum.MERCADO)
                .completed(completed)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
