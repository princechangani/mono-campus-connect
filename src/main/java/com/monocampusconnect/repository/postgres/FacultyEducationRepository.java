package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.FacultyEducation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyEducationRepository extends JpaRepository<FacultyEducation, Long> {
}
