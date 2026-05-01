package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.TenantCanonical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantCanonicalRepository extends JpaRepository<TenantCanonical, Long> {
    List<TenantCanonical> findByIsDeletedFalseOrderByCreatedAtDesc();
    Optional<TenantCanonical> findByTenantPublicIdAndIsDeletedFalse(UUID tenantPublicId);
    boolean existsBySlugAndIsDeletedFalse(String slug);
}

