package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {
}

