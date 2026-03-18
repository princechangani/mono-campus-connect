package com.monocampusconnect.repository;

import com.monocampusconnect.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result> findByTenantIdAndStudentIdAndExamId(UUID tenantId, Long studentId, Long examId);
    List<Result> findByTenantId(UUID tenantId);
    List<Result> findByTenantIdAndStudentId(UUID tenantId, Long studentId);
    List<Result> findByTenantIdAndExamId(UUID tenantId, Long examId);
    List<Result> findByTenantIdAndCourseId(UUID tenantId, Long courseId);
    long countByTenantId(UUID tenantId);
}
