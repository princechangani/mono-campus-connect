package com.monocampusconnect.repository;

import com.monocampusconnect.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result> findByStudentIdAndExamCode(String studentId, String examCode);
    List<Result> findByStudentId(String studentId);
    List<Result> findByExamCode(String examCode);
    List<Result> findByCourseCode(String courseCode);
    List<Result> findByStatus(String status);
    List<Result> findByExamId(Long examId);
    // Tenant-scoped
    List<Result> findByTenantId(UUID tenantId);
    List<Result> findByTenantIdAndStudentId(UUID tenantId, String studentId);
    long countByTenantId(UUID tenantId);
}
