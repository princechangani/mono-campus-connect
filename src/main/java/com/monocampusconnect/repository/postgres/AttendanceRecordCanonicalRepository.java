package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.AttendanceRecordCanonical;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRecordCanonicalRepository extends JpaRepository<AttendanceRecordCanonical, Long> {
}

