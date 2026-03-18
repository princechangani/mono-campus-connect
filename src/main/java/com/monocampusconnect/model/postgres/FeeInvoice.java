package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "fee_invoices")
public class FeeInvoice extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "invoice_public_id")
    private UUID invoicePublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "fee_structure_id")
    private Long feeStructureId;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "late_fee")
    private BigDecimal lateFee;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "status")
    private String status;
}

