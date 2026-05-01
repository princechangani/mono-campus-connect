package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "leave_applications")
public class LeaveApplication extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private Long leaveId;

    @Column(name = "leave_public_id")
    private UUID leavePublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "applicant_user_id")
    private Long applicantUserId;

    @Column(name = "applicant_type")
    private String applicantType;

    @Column(name = "leave_type")
    private String leaveType;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "document_url")
    private String documentUrl;

    @Column(name = "status")
    private String status;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @Column(name = "reviewed_at")
    private OffsetDateTime reviewedAt;

    @Column(name = "reviewer_remarks")
    private String reviewerRemarks;
}

