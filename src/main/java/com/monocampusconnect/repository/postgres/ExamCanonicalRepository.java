package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.ExamCanonical;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamCanonicalRepository extends JpaRepository<ExamCanonical, Long> {
}

