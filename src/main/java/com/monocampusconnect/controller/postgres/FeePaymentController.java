package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.FeePayment;
import com.monocampusconnect.service.postgres.FeePaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fee-payments")
public class FeePaymentController {

    private final FeePaymentService feePaymentService;

    public FeePaymentController(FeePaymentService feePaymentService) {
        this.feePaymentService = feePaymentService;
    }

    @PostMapping
    public ResponseEntity<FeePayment> create(@RequestBody FeePayment feePayment) {
        return new ResponseEntity<>(feePaymentService.create(feePayment), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FeePayment>> getAll() {
        return ResponseEntity.ok(feePaymentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeePayment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(feePaymentService.getById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<FeePayment>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(feePaymentService.getByStudentId(studentId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<FeePayment>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(feePaymentService.getByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeePayment> update(@PathVariable Long id, @RequestBody FeePayment feePayment) {
        return ResponseEntity.ok(feePaymentService.update(id, feePayment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        feePaymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
