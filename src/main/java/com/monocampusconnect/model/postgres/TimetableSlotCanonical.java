package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "timetable_slots")
public class TimetableSlotCanonical extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long slotId;

    @Column(name = "slot_public_id")
    private UUID slotPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "course_assignment_id")
    private Long courseAssignmentId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "slot_type")
    private String slotType;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "recurrence")
    private String recurrence;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "faculty_name")
    private String facultyName;

    @Column(name = "faculty_code")
    private String facultyCode;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "semester_number")
    private String semesterNumber;

    @Column(name = "time_slot")
    private String timeSlot;

    @Column(name = "day_of_week_label")
    private String dayOfWeekLabel;
}
