package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "exams")
public class ExamCanonical extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "exam_public_id")
    private UUID examPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "academic_year_id")
    private Long academicYearId;

    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "course_id")
    private Long courseId;

    // Frontend compatibility fields
    @Column(name = "exam_code")
    private String examCode;

    @Column(name = "name")
    private String name;

    @Column(name = "exam_type")
    private String examType;

    @Column(name = "description")
    private String description;

    @Column(name = "semester_number")
    private Integer semesterNumber;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_marks")
    private BigDecimal totalMarks;

    @Column(name = "passing_marks")
    private BigDecimal passingMarks;

    @Column(name = "enrolled_student_ids", columnDefinition = "text")
    private String enrolledStudentIds;
}
