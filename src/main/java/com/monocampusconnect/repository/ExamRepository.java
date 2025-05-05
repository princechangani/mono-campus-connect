package com.monocampusconnect.repository;

import com.monocampusconnect.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Optional<Exam> findByExamCode(String examCode);
    List<Exam> findByCourseCode(String courseCode);
    List<Exam> findByType(Exam.ExamType type);
    List<Exam> findByEnrolledStudentsContains(String studentId);
    List<Exam> findByEnrolledStudentsContainsAndStartDateAfter(String studentId, Date date);
    List<Exam> findByEnrolledStudentsContainsAndEndDateBefore(String studentId, Date date);
    List<Exam> findByStartDateBetween(Date startDate, Date endDate);
    List<Exam> findByStartDateBefore(Date date);
    List<Exam> findByEndDateAfter(Date date);
    List<Exam> findByStartDateAfter(Date date);
    List<Exam> findByEndDateBefore(Date date);
    
    // Optimized query methods
    List<Exam> findByCourseCodeAndType(String courseCode, Exam.ExamType type);
    List<Exam> findByCourseCodeAndStartDateAfter(String courseCode, Date startDate);
    List<Exam> findByCourseCodeAndEndDateBefore(String courseCode, Date endDate);
    List<Exam> findByTypeAndStartDateAfter(Exam.ExamType type, Date startDate);
    List<Exam> findByTypeAndEndDateBefore(Exam.ExamType type, Date endDate);
}
