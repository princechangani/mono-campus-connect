package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "attendance_sessions")
public class AttendanceSession extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "session_public_id")
    private UUID sessionPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "course_assignment_id")
    private Long courseAssignmentId;

    @Column(name = "slot_id")
    private Long slotId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "session_date")
    private LocalDate sessionDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "topic_covered")
    private String topicCovered;

    @Column(name = "session_type")
    private String sessionType;

    @Column(name = "conducted_by")
    private Long conductedBy;
}

