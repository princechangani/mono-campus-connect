package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.LeaveApplication;
import com.monocampusconnect.repository.postgres.LeaveApplicationRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveApplicationRepository;

    public LeaveApplicationService(LeaveApplicationRepository leaveApplicationRepository) {
        this.leaveApplicationRepository = leaveApplicationRepository;
    }

    public LeaveApplication create(LeaveApplication leaveApplication) {
        if (leaveApplication.getLeavePublicId() == null) {
            leaveApplication.setLeavePublicId(UUID.randomUUID());
        }
        return leaveApplicationRepository.save(leaveApplication);
    }

    public List<LeaveApplication> getAll() {
        return leaveApplicationRepository.findAll();
    }

    public LeaveApplication getById(Long id) {
        return leaveApplicationRepository.findById(id)
                .orElseThrow(() -> new ApiException("Leave application not found", 404));
    }

    public LeaveApplication update(Long id, LeaveApplication leaveApplication) {
        LeaveApplication existing = getById(id);
        if (leaveApplication.getLeaveType() != null)       existing.setLeaveType(leaveApplication.getLeaveType());
        if (leaveApplication.getFromDate() != null)        existing.setFromDate(leaveApplication.getFromDate());
        if (leaveApplication.getToDate() != null)          existing.setToDate(leaveApplication.getToDate());
        if (leaveApplication.getReason() != null)          existing.setReason(leaveApplication.getReason());
        if (leaveApplication.getDocumentUrl() != null)     existing.setDocumentUrl(leaveApplication.getDocumentUrl());
        if (leaveApplication.getStatus() != null)          existing.setStatus(leaveApplication.getStatus());
        if (leaveApplication.getReviewedBy() != null)      existing.setReviewedBy(leaveApplication.getReviewedBy());
        if (leaveApplication.getReviewerRemarks() != null) existing.setReviewerRemarks(leaveApplication.getReviewerRemarks());
        if (leaveApplication.getReviewedAt() != null) {
            existing.setReviewedAt(leaveApplication.getReviewedAt());
        } else if (leaveApplication.getStatus() != null) {
            existing.setReviewedAt(OffsetDateTime.now());
        }
        return leaveApplicationRepository.save(existing);
    }

    public void delete(Long id) {
        leaveApplicationRepository.delete(getById(id));
    }

    public List<LeaveApplication> getByApplicantUserId(Long userId) {
        return leaveApplicationRepository.findAll().stream()
                .filter(la -> userId.equals(la.getApplicantUserId()))
                .toList();
    }

    public List<LeaveApplication> getByApprovalStatus(String status) {
        return leaveApplicationRepository.findAll().stream()
                .filter(la -> status.equals(la.getStatus()))
                .toList();
    }
}

