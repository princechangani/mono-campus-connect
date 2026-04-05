package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.ExamCanonical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExamCanonicalRepository extends JpaRepository<ExamCanonical, Long> {
    Optional<ExamCanonical> findByExamCodeAndTenantId(String examCode, UUID tenantId);
}
