package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}

