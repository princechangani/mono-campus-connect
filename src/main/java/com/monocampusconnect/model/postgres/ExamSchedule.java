package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "exam_schedules")
public class ExamSchedule extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_schedule_id")
    private Long examScheduleId;

    @Column(name = "exam_schedule_public_id")
    private UUID examSchedulePublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "exam_date")
    private LocalDate examDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "invigilator_id")
    private Long invigilatorId;
}

