package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.ProgramCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramCourseRepository extends JpaRepository<ProgramCourse, Long> {
}

