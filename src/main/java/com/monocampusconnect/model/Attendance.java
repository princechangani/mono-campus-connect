package com.monocampusconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "attendance_records")
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_record_id")
    private Long attendanceId;

    @Column(name = "attendance_record_public_id", nullable = false, unique = true, updatable = false)
    private UUID attendancePublicId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttendanceStatus status;

    @Column(name = "marked_by", nullable = false)
    private Long markedBy;

    @Column(name = "marked_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date markedAt;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    // Legacy compatibility fields for current request DTOs.
    @Transient
    private String courseCode;

    @Transient
    private Date sessionDate;

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        if (createdAt == null) createdAt = now;
        if (markedAt == null) markedAt = now;
        updatedAt = now;
        if (attendancePublicId == null) {
            attendancePublicId = UUID.randomUUID();
        }
    }

    @PreUpdate
    protected void onUpdateAudit() {
        updatedAt = new Date();
    }

    public Long getId() {
        return this.attendanceId;
    }

    public void setId(Long id) {
        this.attendanceId = id;
    }

    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, EXCUSED
    }
}
