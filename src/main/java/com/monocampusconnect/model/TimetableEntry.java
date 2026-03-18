package com.monocampusconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "timetable_slots")
@Data
public class TimetableEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long timetableEntryId;

    @Column(name = "slot_public_id", nullable = false, unique = true, updatable = false)
    private UUID timetableEntriesPublicId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "course_assignment_id", nullable = false)
    private Long courseAssignmentId;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    @Column(name = "slot_type")
    private String slotType;

    @Column(name = "effective_from", nullable = false)
    private Date effectiveFrom;

    @Column(name = "effective_to")
    private Date effectiveTo;

    @Column(name = "recurrence")
    private String recurrence;

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

    // Legacy compatibility fields for current DTO/controller contract.
    @Transient
    private String dayOfWeekText;

    @Transient
    private String timeSlot;

    @Transient
    private String courseCode;

    @Transient
    private String courseName;

    @Transient
    private String facultyId;

    @Transient
    private String facultyName;

    @Transient
    private String roomNumber;

    @Transient
    private String semester;

    public String getDayOfWeekText() {
        return dayOfWeekText;
    }

    public void setDayOfWeekText(String dayOfWeekText) {
        this.dayOfWeekText = dayOfWeekText;
    }

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
        if (timetableEntriesPublicId == null) {
            timetableEntriesPublicId = UUID.randomUUID();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public Long getId() {
        return this.timetableEntryId;
    }

    public void setId(Long id) {
        this.timetableEntryId = id;
    }
}
