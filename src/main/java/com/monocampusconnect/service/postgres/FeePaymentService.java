package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.FeePayment;
import com.monocampusconnect.repository.postgres.FeePaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeePaymentService {

    private final FeePaymentRepository feePaymentRepository;

    public FeePaymentService(FeePaymentRepository feePaymentRepository) {
        this.feePaymentRepository = feePaymentRepository;
    }

    public FeePayment create(FeePayment feePayment) {
        if (feePayment.getPaymentDate() == null) {
            feePayment.setPaymentDate(LocalDate.now());
        }
        return feePaymentRepository.save(feePayment);
    }

    public List<FeePayment> getAll() {
        return feePaymentRepository.findAll();
    }

    public FeePayment getById(Long id) {
        return feePaymentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Fee payment not found", 404));
    }

    public FeePayment update(Long id, FeePayment feePayment) {
        FeePayment existing = getById(id);
        if (feePayment.getAmountPaid() != null) existing.setAmountPaid(feePayment.getAmountPaid());
        if (feePayment.getPaymentMode() != null) existing.setPaymentMode(feePayment.getPaymentMode());
        if (feePayment.getStatus() != null) existing.setStatus(feePayment.getStatus());
        if (feePayment.getTransactionReference() != null) existing.setTransactionReference(feePayment.getTransactionReference());
        return feePaymentRepository.save(existing);
    }

    public void delete(Long id) {
        feePaymentRepository.delete(getById(id));
    }

    public List<FeePayment> getByStudentId(Long studentId) {
        return feePaymentRepository.findAll().stream()
                .filter(fp -> studentId.equals(fp.getStudentId()))
                .toList();
    }

    public List<FeePayment> getByStatus(String status) {
        return feePaymentRepository.findAll().stream()
                .filter(fp -> status.equals(fp.getStatus()))
                .toList();
    }
}

