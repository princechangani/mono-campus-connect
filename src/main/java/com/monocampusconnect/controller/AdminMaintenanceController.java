package com.monocampusconnect.controller;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.AttendanceRecordCanonical;
import com.monocampusconnect.model.postgres.CourseCanonical;
import com.monocampusconnect.model.postgres.ExamCanonical;
import com.monocampusconnect.model.postgres.Mark;
import com.monocampusconnect.model.postgres.TimetableSlotCanonical;
import com.monocampusconnect.repository.postgres.AttendanceRecordCanonicalRepository;
import com.monocampusconnect.repository.postgres.CourseCanonicalRepository;
import com.monocampusconnect.repository.postgres.ExamCanonicalRepository;
import com.monocampusconnect.repository.postgres.MarkRepository;
import com.monocampusconnect.repository.postgres.TimetableSlotCanonicalRepository;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/maintenance")
public class AdminMaintenanceController {

    private final CourseCanonicalRepository courseRepository;
    private final ExamCanonicalRepository examRepository;
    private final MarkRepository markRepository;
    private final AttendanceRecordCanonicalRepository attendanceRepository;
    private final TimetableSlotCanonicalRepository timetableRepository;
    private final JdbcTemplate jdbcTemplate;
    private final MongoTemplate mongoTemplate;

    public AdminMaintenanceController(CourseCanonicalRepository courseRepository,
                                      ExamCanonicalRepository examRepository,
                                      MarkRepository markRepository,
                                      AttendanceRecordCanonicalRepository attendanceRepository,
                                      TimetableSlotCanonicalRepository timetableRepository,
                                      JdbcTemplate jdbcTemplate,
                                      MongoTemplate mongoTemplate) {
        this.courseRepository = courseRepository;
        this.examRepository = examRepository;
        this.markRepository = markRepository;
        this.attendanceRepository = attendanceRepository;
        this.timetableRepository = timetableRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.mongoTemplate = mongoTemplate;
    }

    private UUID currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    @PostMapping("/backfill-canonical-ids")
    public ResponseEntity<Map<String, Object>> backfillCanonicalIds() {
        UUID tenantId = currentTenantId();
        int exams = backfillExams(tenantId);
        int marks = backfillMarks(tenantId);
        int attendance = backfillAttendance(tenantId);
        int timetable = backfillTimetable(tenantId);
        int materials = backfillMaterials(tenantId.toString());

        return ResponseEntity.ok(Map.of(
                "examsUpdated", exams,
                "marksUpdated", marks,
                "attendanceUpdated", attendance,
                "timetableUpdated", timetable,
                "materialsUpdated", materials
        ));
    }

    private int backfillExams(UUID tenantId) {
        int updated = 0;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "select exam_id, course_code from exams where tenant_id = ? and course_id is null and course_code is not null",
                tenantId);
        for (Map<String, Object> row : rows) {
            Long examId = ((Number) row.get("exam_id")).longValue();
            String courseCode = String.valueOf(row.get("course_code"));
            Optional<Long> courseId = resolveCourseId(tenantId, courseCode);
            if (courseId.isEmpty()) continue;
            jdbcTemplate.update("update exams set course_id = ? where exam_id = ?", courseId.get(), examId);
            updated++;
        }
        return updated;
    }

    private int backfillMarks(UUID tenantId) {
        int updated = 0;
        List<Mark> marks = markRepository.findAll().stream()
                .filter(m -> tenantId.equals(m.getTenantId()))
                .filter(m -> m.getCourseId() == null && m.getExamId() != null)
                .toList();
        for (Mark m : marks) {
            examRepository.findById(m.getExamId()).ifPresent(exam -> {
                m.setCourseId(exam.getCourseId());
            });
            if (m.getCourseId() != null) updated++;
        }
        if (!marks.isEmpty()) markRepository.saveAll(marks);
        return updated;
    }

    private int backfillAttendance(UUID tenantId) {
        int updated = 0;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "select attendance_record_id, course_code from attendance_records where tenant_id = ? and course_id is null and course_code is not null",
                tenantId);
        for (Map<String, Object> row : rows) {
            Long recordId = ((Number) row.get("attendance_record_id")).longValue();
            String courseCode = String.valueOf(row.get("course_code"));
            Optional<Long> courseId = resolveCourseId(tenantId, courseCode);
            if (courseId.isEmpty()) continue;
            jdbcTemplate.update("update attendance_records set course_id = ? where attendance_record_id = ?",
                    courseId.get(), recordId);
            updated++;
        }
        return updated;
    }

    private int backfillTimetable(UUID tenantId) {
        int updated = 0;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "select slot_id, course_code from timetable_slots where tenant_id = ? and course_id is null and course_code is not null",
                tenantId);
        for (Map<String, Object> row : rows) {
            Long slotId = ((Number) row.get("slot_id")).longValue();
            String courseCode = String.valueOf(row.get("course_code"));
            Optional<Long> courseId = resolveCourseId(tenantId, courseCode);
            if (courseId.isEmpty()) continue;
            jdbcTemplate.update("update timetable_slots set course_id = ? where slot_id = ?",
                    courseId.get(), slotId);
            updated++;
        }
        return updated;
    }

    private int backfillMaterials(String tenantId) {
        int updated = 0;
        Query query = new Query();
        query.addCriteria(Criteria.where("tenant_id").is(tenantId)
                .and("course_id").exists(false)
                .and("course_code").exists(true));
        List<Document> docs = mongoTemplate.find(query, Document.class, "study_materials");
        UUID tenantUuid = UUID.fromString(tenantId);
        for (Document d : docs) {
            String courseCode = d.getString("course_code");
            Optional<Long> courseId = resolveCourseId(tenantUuid, courseCode);
            if (courseId.isEmpty()) continue;
            Query byId = new Query(Criteria.where("_id").is(d.getObjectId("_id")));
            Update update = new Update().set("course_id", courseId.get());
            mongoTemplate.updateFirst(byId, update, "study_materials");
            updated++;
        }
        return updated;
    }

    private Optional<Long> resolveCourseId(UUID tenantId, String courseCode) {
        if (courseCode == null || courseCode.isBlank()) return Optional.empty();
        return courseRepository.findByTenantIdAndCodeAndIsDeletedFalse(tenantId, courseCode.toUpperCase())
                .map(CourseCanonical::getCourseId);
    }
}
