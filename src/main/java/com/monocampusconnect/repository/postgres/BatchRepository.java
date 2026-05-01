package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<Batch, Long> {
}

