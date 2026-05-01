package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
}

