package com.monocampusconnect.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "events_id")
    private Long eventId;

    @Column(name = "events_public_id", nullable = false, unique = true, updatable = false)
    private UUID eventsPublicId;

    @Column(name = "tenant_id")
    private UUID tenantId;

    private String title;
    private String description;
    
    @Lob
    private byte[] imageContent;
    
    private String postedBy;

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

    public Long getId() {
        return this.eventId;
    }

    public void setId(Long id) {
        this.eventId = id;
    }

    @PrePersist
    protected void onCreatePublicId() {
        if (eventsPublicId == null) {
            eventsPublicId = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = new Date();
        }
        if (updatedAt == null) {
            updatedAt = new Date();
        }
    }

    @PreUpdate
    protected void onUpdateTimestamps() {
        updatedAt = new Date();
    }
}
