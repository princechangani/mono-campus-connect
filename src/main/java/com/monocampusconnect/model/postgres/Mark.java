package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "marks")
public class Mark extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mark_id")
    private Long markId;

    @Column(name = "mark_public_id")
    private UUID markPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "marks_obtained")
    private BigDecimal marksObtained;

    @Column(name = "total_marks")
    private BigDecimal totalMarks;

    @Column(name = "grade")
    private String grade;

    @Column(name = "grade_points")
    private BigDecimal gradePoints;

    @Column(name = "is_absent")
    private Boolean isAbsent;

    @Column(name = "is_withheld")
    private Boolean isWithheld;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "entered_by")
    private Long enteredBy;

    @Column(name = "status")
    private String status;
}
