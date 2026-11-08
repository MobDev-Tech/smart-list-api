package com.smartlist.smartlistapi.modules.shoppinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.smartlist.smartlistapi.modules.shoppinglist.domain.ShoppingItemEntity;

import java.util.List;
import java.util.Optional;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItemEntity, Long> {

    List<ShoppingItemEntity> findByDeviceIdAndCompletedFalseOrderByCreatedAtDesc(String deviceId);

    List<ShoppingItemEntity> findByDeviceIdAndCompletedTrueOrderByUpdatedAtDesc(String deviceId);

    Optional<ShoppingItemEntity> findByIdAndDeviceId(Long id, String deviceId);

    void deleteByDeviceIdAndCompletedTrue(String deviceId);
}