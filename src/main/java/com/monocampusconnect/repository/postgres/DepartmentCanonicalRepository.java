package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.DepartmentCanonical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentCanonicalRepository extends JpaRepository<DepartmentCanonical, Long> {
    List<DepartmentCanonical> findByTenantIdAndIsDeletedFalseOrderByCreatedAtDesc(UUID tenantId);
    Optional<DepartmentCanonical> findByDepartmentPublicIdAndTenantIdAndIsDeletedFalse(UUID departmentPublicId, UUID tenantId);
    boolean existsByTenantIdAndCodeAndIsDeletedFalse(UUID tenantId, String code);
}
