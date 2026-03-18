package com.monocampusconnect.repository;

import com.monocampusconnect.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Optional<Exam> findByExamCodeAndTenantId(String examCode, UUID tenantId);
    List<Exam> findByTenantId(UUID tenantId);
    List<Exam> findByTenantIdAndType(UUID tenantId, Exam.ExamType type);
    List<Exam> findByTenantIdAndTypeAndStartDateAfter(UUID tenantId, Exam.ExamType type, Date startDate);
    List<Exam> findByTenantIdAndTypeAndEndDateBefore(UUID tenantId, Exam.ExamType type, Date endDate);
    List<Exam> findByTenantIdAndStartDateBetween(UUID tenantId, Date startDate, Date endDate);
    List<Exam> findByTenantIdAndStartDateBefore(UUID tenantId, Date date);
    List<Exam> findByTenantIdAndEndDateAfter(UUID tenantId, Date date);
    List<Exam> findByTenantIdAndStartDateAfter(UUID tenantId, Date date);
    List<Exam> findByTenantIdAndEndDateBefore(UUID tenantId, Date date);
    long countByTenantId(UUID tenantId);
}
