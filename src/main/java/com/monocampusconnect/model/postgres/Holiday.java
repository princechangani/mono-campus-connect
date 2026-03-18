package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "holidays")
public class Holiday extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holiday_id")
    private Long holidayId;

    @Column(name = "holiday_public_id")
    private UUID holidayPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "title")
    private String title;

    @Column(name = "holiday_date")
    private LocalDate holidayDate;

    @Column(name = "holiday_type")
    private String holidayType;

    @Column(name = "applies_to")
    private String appliesTo;

    @Column(name = "description")
    private String description;
}

