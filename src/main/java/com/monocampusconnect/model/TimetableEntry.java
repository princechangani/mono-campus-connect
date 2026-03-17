package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "timetable_entries")
@Data
public class TimetableEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_entries_id")
    private Long timetableEntryId;

    @Column(name = "timetable_entries_public_id", nullable = false, unique = true, updatable = false)
    private UUID timetableEntriesPublicId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String dayOfWeek; // MONDAY, TUESDAY, etc.

    @Column(nullable = false)
    private String timeSlot; // e.g., "09:00-10:00"

    @Column(nullable = false)
    private String courseCode;

    private String courseName;

    private String facultyId;
    private String facultyName;

    private String roomNumber;

    @Column(nullable = false)
    private String semester;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
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
