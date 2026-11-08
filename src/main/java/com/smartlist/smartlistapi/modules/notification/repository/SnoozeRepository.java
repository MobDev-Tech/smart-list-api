package com.smartlist.smartlistapi.modules.notification.repository;

import com.smartlist.smartlistapi.modules.notification.domain.SnoozeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SnoozeRepository extends JpaRepository<SnoozeEntity, Long> {

    Optional<SnoozeEntity> findByDeviceId(String deviceId);
}