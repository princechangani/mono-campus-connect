package com.monocampusconnect.controller;

import com.monocampusconnect.dto.CourseRequest;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Course;
import org.springframework.http.HttpStatus;
import com.monocampusconnect.service.CourseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CourseRequest request) {
        if (request.getCourseCode() == null || request.getCourseName() == null) {
            throw new ApiException("Course code and name are required", 400);
        }
        
        Course course = new Course();
        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setDepartment(request.getDepartment());
        course.setCredits(request.getCredits());
        
        return new ResponseEntity<>(courseService.createCourse(course), HttpStatus.CREATED);
    }

    @GetMapping("/{courseCode}")
    public ResponseEntity<Course> getCourse(@PathVariable String courseCode) {
        return ResponseEntity.ok(courseService.getCourse(courseCode));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<Course>> getCoursesByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(courseService.getCoursesByDepartment(department));
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<Course>> getCoursesBySemester(@PathVariable String semester) {
        return ResponseEntity.ok(courseService.getCoursesBySemester(semester));
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @RequestParam(value = "courseCode", required = false) String courseCode,
            @RequestParam(value = "courseName", required = false) String courseName,
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "credits", required = false) Integer credits,
            @RequestParam(value = "instructor", required = false) String instructor,
            @RequestParam(value = "semester", required = false) String semester) {
        
        if (courseCode == null && courseName == null) {
            throw new ApiException("At least course code or name is required", 400);
        }
        
        Course course = new Course();
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setDepartment(department);
        course.setCredits(credits);
        course.setInstructor(instructor);
        course.setSemester(semester);

        return ResponseEntity.ok(courseService.updateCourse(id, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
