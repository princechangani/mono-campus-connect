package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {
}

