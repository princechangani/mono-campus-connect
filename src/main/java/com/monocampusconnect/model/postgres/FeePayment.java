package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "fee_payments")
public class FeePayment extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "payment_public_id")
    private UUID paymentPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(name = "gateway_response")
    private String gatewayResponse;

    @Column(name = "receipt_number")
    private String receiptNumber;

    @Column(name = "collected_by")
    private Long collectedBy;

    @Column(name = "status")
    private String status;
}

