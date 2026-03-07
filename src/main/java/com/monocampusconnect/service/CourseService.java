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
        course.setSemester(courseDetails.getSemester());
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
