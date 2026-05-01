package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.FeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {
}

