package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {
}

