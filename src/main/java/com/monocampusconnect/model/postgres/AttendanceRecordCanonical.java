package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "attendance_records")
public class AttendanceRecordCanonical extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_record_id")
    private Long attendanceRecordId;

    @Column(name = "attendance_record_public_id")
    private UUID attendanceRecordPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "status")
    private String status;

    @Column(name = "marked_by")
    private Long markedBy;

    @Column(name = "marked_at")
    private OffsetDateTime markedAt;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "session_date")
    private OffsetDateTime sessionDate;
}
