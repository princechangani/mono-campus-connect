package com.monocampusconnect.repository;

import com.monocampusconnect.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByTenantIdAndUserIdOrderByCreatedAtDesc(UUID tenantId, Long userId);
    List<Notification> findByTenantIdAndUserIdAndReadFalse(UUID tenantId, Long userId);
    long countByTenantIdAndUserIdAndReadFalse(UUID tenantId, Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.read = true WHERE n.tenantId = :tenantId AND n.userId = :userId")
    void markAllReadForUser(@Param("tenantId") UUID tenantId, @Param("userId") Long userId);
}

