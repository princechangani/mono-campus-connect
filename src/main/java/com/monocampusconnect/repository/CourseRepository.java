package com.monocampusconnect.repository;

import com.monocampusconnect.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCourseCode(String courseCode);
    List<Course> findByDepartment(String department);
    List<Course> findBySemester(String semester);
}
