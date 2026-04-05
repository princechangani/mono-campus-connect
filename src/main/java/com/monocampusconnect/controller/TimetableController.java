package com.monocampusconnect.controller;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.CourseCanonical;
import com.monocampusconnect.model.postgres.TimetableSlotCanonical;
import com.monocampusconnect.repository.postgres.CourseCanonicalRepository;
import com.monocampusconnect.repository.postgres.TimetableSlotCanonicalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

    private final TimetableSlotCanonicalRepository timetableRepository;
    private final CourseCanonicalRepository courseRepository;

    public TimetableController(TimetableSlotCanonicalRepository timetableRepository,
                               CourseCanonicalRepository courseRepository) {
        this.timetableRepository = timetableRepository;
        this.courseRepository = courseRepository;
    }

    private UUID currentTenantId() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        UUID tenantId = currentTenantId();
        List<Map<String, Object>> items = timetableRepository.findAll().stream()
                .filter(t -> tenantId.equals(t.getTenantId()))
                .filter(t -> t.getIsDeleted() == null || !t.getIsDeleted())
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> payload) {
        TimetableSlotCanonical slot = new TimetableSlotCanonical();
        slot.setTenantId(currentTenantId());
        if (!payload.containsKey("courseCode")) throw new ApiException("courseCode is required", 400);
        applyPayload(slot, payload);
        slot.setCreatedAt(OffsetDateTime.now());
        TimetableSlotCanonical saved = timetableRepository.save(slot);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        UUID tenantId = currentTenantId();
        TimetableSlotCanonical slot = timetableRepository.findById(id)
                .orElseThrow(() -> new ApiException("Timetable entry not found", 404));
        if (!tenantId.equals(slot.getTenantId())) throw new ApiException("Forbidden", 403);
        applyPayload(slot, payload);
        slot.setUpdatedAt(OffsetDateTime.now());
        return ResponseEntity.ok(toDto(timetableRepository.save(slot)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        UUID tenantId = currentTenantId();
        TimetableSlotCanonical slot = timetableRepository.findById(id)
                .orElseThrow(() -> new ApiException("Timetable entry not found", 404));
        if (!tenantId.equals(slot.getTenantId())) throw new ApiException("Forbidden", 403);
        slot.setIsDeleted(Boolean.TRUE);
        slot.setDeletedAt(OffsetDateTime.now());
        timetableRepository.save(slot);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }

    private void applyPayload(TimetableSlotCanonical slot, Map<String, Object> payload) {
        if (payload.containsKey("dayOfWeek")) {
            String day = String.valueOf(payload.get("dayOfWeek"));
            slot.setDayOfWeekLabel(day);
            slot.setDayOfWeek(dayToInt(day));
        }
        if (payload.containsKey("timeSlot")) {
            String ts = String.valueOf(payload.get("timeSlot"));
            slot.setTimeSlot(ts);
            LocalTime[] times = parseTimeSlot(ts);
            if (times[0] != null) slot.setStartTime(times[0]);
            if (times[1] != null) slot.setEndTime(times[1]);
        }
        if (payload.containsKey("courseCode")) {
            String code = asString(payload.get("courseCode"));
            CourseCanonical course = resolveCourseOrThrow(code);
            slot.setCourseId(course.getCourseId());
        }
        if (payload.containsKey("courseName")) slot.setCourseName(asString(payload.get("courseName")));
        if (payload.containsKey("facultyName")) slot.setFacultyName(asString(payload.get("facultyName")));
        if (payload.containsKey("facultyId")) slot.setFacultyCode(asString(payload.get("facultyId")));
        if (payload.containsKey("roomNumber")) slot.setRoomNumber(asString(payload.get("roomNumber")));
        if (payload.containsKey("semester")) slot.setSemesterNumber(asString(payload.get("semester")));
    }

    private Map<String, Object> toDto(TimetableSlotCanonical t) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", t.getSlotId());
        row.put("dayOfWeek", t.getDayOfWeekLabel() != null ? t.getDayOfWeekLabel() : intToDay(t.getDayOfWeek()));
        row.put("timeSlot", t.getTimeSlot());
        row.put("courseCode", resolveCourseCode(t));
        row.put("courseName", t.getCourseName());
        row.put("facultyName", t.getFacultyName());
        row.put("facultyId", t.getFacultyCode());
        row.put("roomNumber", t.getRoomNumber());
        row.put("semester", t.getSemesterNumber());
        return row;
    }

    private static String asString(Object val) {
        return val == null ? null : String.valueOf(val);
    }

    private static int dayToInt(String day) {
        return switch (day.toUpperCase()) {
            case "MONDAY" -> 1;
            case "TUESDAY" -> 2;
            case "WEDNESDAY" -> 3;
            case "THURSDAY" -> 4;
            case "FRIDAY" -> 5;
            case "SATURDAY" -> 6;
            case "SUNDAY" -> 7;
            default -> 0;
        };
    }

    private static String intToDay(Integer val) {
        if (val == null) return null;
        return switch (val) {
            case 1 -> "MONDAY";
            case 2 -> "TUESDAY";
            case 3 -> "WEDNESDAY";
            case 4 -> "THURSDAY";
            case 5 -> "FRIDAY";
            case 6 -> "SATURDAY";
            case 7 -> "SUNDAY";
            default -> null;
        };
    }

    private static LocalTime[] parseTimeSlot(String slot) {
        try {
            String[] parts = slot.split("-");
            if (parts.length != 2) return new LocalTime[] { null, null };
            return new LocalTime[] { LocalTime.parse(parts[0].trim()), LocalTime.parse(parts[1].trim()) };
        } catch (Exception e) {
            return new LocalTime[] { null, null };
        }
    }

    private Optional<CourseCanonical> resolveCourse(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) return Optional.empty();
        UUID tenantId = currentTenantId();
        return courseRepository.findByTenantIdAndCodeAndIsDeletedFalse(tenantId, courseCode.toUpperCase());
    }

    private CourseCanonical resolveCourseOrThrow(String courseCode) {
        return resolveCourse(courseCode)
                .orElseThrow(() -> new ApiException("Course not found: " + courseCode, 404));
    }

    private String resolveCourseCode(TimetableSlotCanonical t) {
        if (t.getCourseId() == null) return null;
        return courseRepository.findById(t.getCourseId())
                .map(CourseCanonical::getCode)
                .orElse(null);
    }
}
