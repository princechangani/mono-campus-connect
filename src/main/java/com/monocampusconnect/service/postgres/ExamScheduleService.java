package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.ExamSchedule;
import com.monocampusconnect.repository.postgres.ExamScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamScheduleService {

    private final ExamScheduleRepository examScheduleRepository;

    public ExamScheduleService(ExamScheduleRepository examScheduleRepository) {
        this.examScheduleRepository = examScheduleRepository;
    }

    public ExamSchedule create(ExamSchedule examSchedule) {
        return examScheduleRepository.save(examSchedule);
    }

    public List<ExamSchedule> getAll() {
        return examScheduleRepository.findAll();
    }

    public ExamSchedule getById(Long id) {
        return examScheduleRepository.findById(id)
                .orElseThrow(() -> new ApiException("Exam schedule not found", 404));
    }

    public ExamSchedule update(Long id, ExamSchedule examSchedule) {
        ExamSchedule existing = getById(id);
        if (examSchedule.getExamId() != null)       existing.setExamId(examSchedule.getExamId());
        if (examSchedule.getCourseId() != null)     existing.setCourseId(examSchedule.getCourseId());
        if (examSchedule.getRoomId() != null)       existing.setRoomId(examSchedule.getRoomId());
        if (examSchedule.getExamDate() != null)     existing.setExamDate(examSchedule.getExamDate());
        if (examSchedule.getStartTime() != null)    existing.setStartTime(examSchedule.getStartTime());
        if (examSchedule.getEndTime() != null)      existing.setEndTime(examSchedule.getEndTime());
        if (examSchedule.getInvigilatorId() != null) existing.setInvigilatorId(examSchedule.getInvigilatorId());
        return examScheduleRepository.save(existing);
    }

    public void delete(Long id) {
        examScheduleRepository.delete(getById(id));
    }

    public List<ExamSchedule> getByExamId(Long examId) {
        return examScheduleRepository.findAll().stream()
                .filter(es -> examId.equals(es.getExamId()))
                .toList();
    }

    public List<ExamSchedule> getByRoomId(Long roomId) {
        return examScheduleRepository.findAll().stream()
                .filter(es -> roomId.equals(es.getRoomId()))
                .toList();
    }
}

