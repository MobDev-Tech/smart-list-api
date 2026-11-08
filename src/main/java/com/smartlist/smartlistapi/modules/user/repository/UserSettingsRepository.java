package com.smartlist.smartlistapi.modules.user.repository;

import com.smartlist.smartlistapi.modules.user.domain.UserSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserSettingsRepository extends JpaRepository<UserSettingsEntity, Long> {

    Optional<UserSettingsEntity> findByDeviceId(String deviceId);
}