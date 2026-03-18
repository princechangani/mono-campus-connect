package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
}

