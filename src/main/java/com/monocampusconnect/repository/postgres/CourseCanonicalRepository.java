package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.CourseCanonical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseCanonicalRepository extends JpaRepository<CourseCanonical, Long> {
    List<CourseCanonical> findByTenantIdAndIsDeletedFalseOrderByCreatedAtDesc(UUID tenantId);
    List<CourseCanonical> findByTenantIdAndDepartmentIdAndIsDeletedFalseOrderByCreatedAtDesc(UUID tenantId, Long departmentId);
    Optional<CourseCanonical> findByCoursePublicIdAndTenantIdAndIsDeletedFalse(UUID coursePublicId, UUID tenantId);
    Optional<CourseCanonical> findByTenantIdAndCodeAndIsDeletedFalse(UUID tenantId, String code);
    boolean existsByTenantIdAndCodeAndIsDeletedFalse(UUID tenantId, String code);
}
