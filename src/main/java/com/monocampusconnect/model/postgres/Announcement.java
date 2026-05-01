package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "announcements")
public class Announcement extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcement_id")
    private Long announcementId;

    @Column(name = "announcement_public_id")
    private UUID announcementPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "posted_by")
    private String postedBy;

    @Column(name = "audience")
    private String audience;

    @Column(name = "audience_ref_id")
    private Long audienceRefId;

    @Column(name = "priority")
    private String priority;

    @Column(name = "pinned_until")
    private LocalDate pinnedUntil;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;
}
