package com.monocampusconnect.service;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Course;
import com.monocampusconnect.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    private UUID currentTenant() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    public Course createCourse(Course course) {
        course.setTenantId(currentTenant());
        if (course.getSubjectType() == null) course.setSubjectType(Course.SubjectType.COMPULSORY);
        return courseRepository.save(course);
    }

    public Course getCourse(String courseCode) {
        return courseRepository.findByTenantIdAndCourseCode(currentTenant(), courseCode)
                .orElseThrow(() -> new ApiException("Course not found: " + courseCode, 404));
    }

    public List<Course> getCoursesByDepartment(String department) {
        return courseRepository.findByDepartment(department);
    }

    public List<Course> getCoursesBySemester(String semester) {
        return courseRepository.findBySemester(semester);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findByTenantId(currentTenant());
    }

    public List<Course> filterCourses(String semester, String department, String instructor,
                                      String facultyId, Integer credits,
                                      String subjectType, String category) {
        Course.SubjectType type = null;
        if (subjectType != null && !subjectType.isBlank()) {
            try { type = Course.SubjectType.valueOf(subjectType.toUpperCase()); }
            catch (IllegalArgumentException ignored) {}
        }
        return courseRepository.filterCourses(
            currentTenant(),
            blankToNull(semester),
            blankToNull(department),
            blankToNull(instructor),
            blankToNull(facultyId),
            credits,
            type,
            blankToNull(category)
        );
    }

    private String blankToNull(String s) { return (s == null || s.isBlank()) ? null : s; }

    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ApiException("Course not found", 404));
        if (!currentTenant().equals(course.getTenantId()))
            throw new ApiException("Course not found in this college", 404);
        course.setCourseCode(courseDetails.getCourseCode());
        course.setCourseName(courseDetails.getCourseName());
        course.setDepartment(courseDetails.getDepartment());
        course.setCredits(courseDetails.getCredits());
        course.setInstructor(courseDetails.getInstructor());
        course.setFacultyId(courseDetails.getFacultyId());
        course.setSemester(courseDetails.getSemester());
        if (courseDetails.getSubjectType() != null) course.setSubjectType(courseDetails.getSubjectType());
        course.setCategory(courseDetails.getCategory());
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ApiException("Course not found", 404));
        if (!currentTenant().equals(course.getTenantId()))
            throw new ApiException("Course not found in this college", 404);
        courseRepository.deleteById(id);
    }
}
