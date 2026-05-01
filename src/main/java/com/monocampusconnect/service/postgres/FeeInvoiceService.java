package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.FeeInvoice;
import com.monocampusconnect.repository.postgres.FeeInvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeeInvoiceService {

    private final FeeInvoiceRepository feeInvoiceRepository;

    public FeeInvoiceService(FeeInvoiceRepository feeInvoiceRepository) {
        this.feeInvoiceRepository = feeInvoiceRepository;
    }

    public FeeInvoice create(FeeInvoice feeInvoice) {
        if (feeInvoice.getIssueDate() == null) {
            feeInvoice.setIssueDate(LocalDate.now());
        }
        return feeInvoiceRepository.save(feeInvoice);
    }

    public List<FeeInvoice> getAll() {
        return feeInvoiceRepository.findAll();
    }

    public FeeInvoice getById(Long id) {
        return feeInvoiceRepository.findById(id)
                .orElseThrow(() -> new ApiException("Fee invoice not found", 404));
    }

    public FeeInvoice update(Long id, FeeInvoice feeInvoice) {
        FeeInvoice existing = getById(id);
        if (feeInvoice.getStudentId() != null) existing.setStudentId(feeInvoice.getStudentId());
        if (feeInvoice.getFeeStructureId() != null) existing.setFeeStructureId(feeInvoice.getFeeStructureId());
        if (feeInvoice.getDueDate() != null) existing.setDueDate(feeInvoice.getDueDate());
        if (feeInvoice.getStatus() != null) existing.setStatus(feeInvoice.getStatus());
        return feeInvoiceRepository.save(existing);
    }

    public void delete(Long id) {
        feeInvoiceRepository.delete(getById(id));
    }

    public List<FeeInvoice> getByStudentId(Long studentId) {
        return feeInvoiceRepository.findAll().stream()
                .filter(fi -> studentId.equals(fi.getStudentId()))
                .toList();
    }

    public List<FeeInvoice> getByStatus(String status) {
        return feeInvoiceRepository.findAll().stream()
                .filter(fi -> status.equals(fi.getStatus()))
                .toList();
    }
}

