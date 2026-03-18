package com.monocampusconnect.service.postgres;

import com.monocampusconnect.dto.canonical.postgres.CourseCanonicalRequest;
import com.monocampusconnect.dto.canonical.postgres.CourseCanonicalResponse;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.CourseCanonical;
import com.monocampusconnect.repository.postgres.CourseCanonicalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CourseCanonicalService {

    private final CourseCanonicalRepository courseRepository;

    public CourseCanonicalService(CourseCanonicalRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional
    public CourseCanonicalResponse create(CourseCanonicalRequest request) {
        String normalizedCode = request.getCode().trim().toUpperCase();
        if (courseRepository.existsByTenantIdAndCodeAndIsDeletedFalse(request.getTenantId(), normalizedCode)) {
            throw new ApiException("Course code already exists: " + normalizedCode, 409);
        }

        CourseCanonical course = new CourseCanonical();
        course.setCoursePublicId(UUID.randomUUID());
        course.setTenantId(request.getTenantId());
        course.setDepartmentId(request.getDepartmentId());
        course.setName(request.getName().trim());
        course.setCode(normalizedCode);
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setLectureHoursPerWeek(request.getLectureHoursPerWeek() == null ? 3 : request.getLectureHoursPerWeek());
        course.setLabHoursPerWeek(request.getLabHoursPerWeek() == null ? 0 : request.getLabHoursPerWeek());
        course.setTutorialHoursPerWeek(request.getTutorialHoursPerWeek() == null ? 0 : request.getTutorialHoursPerWeek());
        course.setCourseType(request.getCourseType() == null ? "theory" : request.getCourseType());
        course.setIsActive(request.getActive() == null ? Boolean.TRUE : request.getActive());
        course.setCreatedAt(OffsetDateTime.now());

        return toResponse(courseRepository.save(course));
    }

    public List<CourseCanonicalResponse> getAllByTenant(UUID tenantId, Long departmentId) {
        List<CourseCanonical> courses = departmentId == null
                ? courseRepository.findByTenantIdAndIsDeletedFalseOrderByCreatedAtDesc(tenantId)
                : courseRepository.findByTenantIdAndDepartmentIdAndIsDeletedFalseOrderByCreatedAtDesc(tenantId, departmentId);
        return courses.stream().map(this::toResponse).toList();
    }

    public CourseCanonicalResponse getByPublicId(UUID tenantId, UUID coursePublicId) {
        return toResponse(getEntity(tenantId, coursePublicId));
    }

    @Transactional
    public CourseCanonicalResponse update(UUID tenantId, UUID coursePublicId, CourseCanonicalRequest request) {
        CourseCanonical course = getEntity(tenantId, coursePublicId);

        String normalizedCode = request.getCode().trim().toUpperCase();
        if (!normalizedCode.equals(course.getCode()) &&
                courseRepository.existsByTenantIdAndCodeAndIsDeletedFalse(tenantId, normalizedCode)) {
            throw new ApiException("Course code already exists: " + normalizedCode, 409);
        }

        course.setDepartmentId(request.getDepartmentId());
        course.setName(request.getName().trim());
        course.setCode(normalizedCode);
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setLectureHoursPerWeek(request.getLectureHoursPerWeek() == null ? course.getLectureHoursPerWeek() : request.getLectureHoursPerWeek());
        course.setLabHoursPerWeek(request.getLabHoursPerWeek() == null ? course.getLabHoursPerWeek() : request.getLabHoursPerWeek());
        course.setTutorialHoursPerWeek(request.getTutorialHoursPerWeek() == null ? course.getTutorialHoursPerWeek() : request.getTutorialHoursPerWeek());
        course.setCourseType(request.getCourseType() == null ? course.getCourseType() : request.getCourseType());
        course.setIsActive(request.getActive() == null ? course.getIsActive() : request.getActive());
        course.setUpdatedAt(OffsetDateTime.now());

        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public void delete(UUID tenantId, UUID coursePublicId) {
        CourseCanonical course = getEntity(tenantId, coursePublicId);
        course.setIsDeleted(Boolean.TRUE);
        course.setDeletedAt(OffsetDateTime.now());
        courseRepository.save(course);
    }

    private CourseCanonical getEntity(UUID tenantId, UUID coursePublicId) {
        return courseRepository.findByCoursePublicIdAndTenantIdAndIsDeletedFalse(coursePublicId, tenantId)
                .orElseThrow(() -> new ApiException("Course not found", 404));
    }

    private CourseCanonicalResponse toResponse(CourseCanonical course) {
        CourseCanonicalResponse response = new CourseCanonicalResponse();
        response.setCourseId(course.getCourseId());
        response.setCoursePublicId(course.getCoursePublicId());
        response.setTenantId(course.getTenantId());
        response.setDepartmentId(course.getDepartmentId());
        response.setName(course.getName());
        response.setCode(course.getCode());
        response.setDescription(course.getDescription());
        response.setCredits(course.getCredits());
        response.setLectureHoursPerWeek(course.getLectureHoursPerWeek());
        response.setLabHoursPerWeek(course.getLabHoursPerWeek());
        response.setTutorialHoursPerWeek(course.getTutorialHoursPerWeek());
        response.setCourseType(course.getCourseType());
        response.setActive(course.getIsActive());
        response.setCreatedAt(course.getCreatedAt());
        response.setUpdatedAt(course.getUpdatedAt());
        return response;
    }
}

