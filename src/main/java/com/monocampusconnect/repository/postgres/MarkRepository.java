package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.Mark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkRepository extends JpaRepository<Mark, Long> {
}

