package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.CourseAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseAssignmentRepository extends JpaRepository<CourseAssignment, Long> {
}

