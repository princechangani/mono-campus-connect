package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.ExamSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long> {
}

