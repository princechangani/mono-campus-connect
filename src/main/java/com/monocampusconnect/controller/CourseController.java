package com.monocampusconnect.controller;

import com.monocampusconnect.dto.CourseRequest;
import com.monocampusconnect.model.Course;
import com.monocampusconnect.service.CourseService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return new ResponseEntity<>(courseService.createCourse(course), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    /** GET /api/courses/filter?semester=4&department=CS&credits=4&subjectType=COMPULSORY&category=Core */
    @GetMapping("/filter")
    public ResponseEntity<List<Course>> filterCourses(
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String instructor,
            @RequestParam(required = false) String facultyId,
            @RequestParam(required = false) Integer credits,
            @RequestParam(required = false) String subjectType,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(courseService.filterCourses(
                semester, department, instructor, facultyId, credits, subjectType, category));
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

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody CourseRequest request) {
        Course course = new Course();
        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setDepartment(request.getDepartment());
        course.setCredits(request.getCredits());
        course.setInstructor(request.getInstructor());
        course.setFacultyId(request.getFacultyId());
        course.setSemester(request.getSemester());
        course.setSubjectType(request.getSubjectType());
        course.setCategory(request.getCategory());
        return ResponseEntity.ok(courseService.updateCourse(id, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
