package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

