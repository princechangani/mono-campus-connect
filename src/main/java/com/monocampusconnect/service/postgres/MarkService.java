package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.Mark;
import com.monocampusconnect.repository.postgres.MarkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkService {

    private final MarkRepository markRepository;

    public MarkService(MarkRepository markRepository) {
        this.markRepository = markRepository;
    }

    public Mark create(Mark mark) {
        return markRepository.save(mark);
    }

    public List<Mark> getAll() {
        return markRepository.findAll();
    }

    public Mark getById(Long id) {
        return markRepository.findById(id)
                .orElseThrow(() -> new ApiException("Mark not found", 404));
    }

    public Mark update(Long id, Mark mark) {
        Mark existing = getById(id);
        if (mark.getStudentId() != null) existing.setStudentId(mark.getStudentId());
        if (mark.getExamId() != null) existing.setExamId(mark.getExamId());
        if (mark.getMarksObtained() != null) existing.setMarksObtained(mark.getMarksObtained());
        return markRepository.save(existing);
    }

    public void delete(Long id) {
        markRepository.delete(getById(id));
    }

    public List<Mark> getByStudentId(Long studentId) {
        return markRepository.findAll().stream()
                .filter(m -> studentId.equals(m.getStudentId()))
                .toList();
    }

    public List<Mark> getByExamId(Long examId) {
        return markRepository.findAll().stream()
                .filter(m -> examId.equals(m.getExamId()))
                .toList();
    }
}

