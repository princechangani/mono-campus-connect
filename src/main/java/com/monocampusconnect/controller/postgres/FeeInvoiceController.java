package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.FeeInvoice;
import com.monocampusconnect.service.postgres.FeeInvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fee-invoices")
public class FeeInvoiceController {

    private final FeeInvoiceService feeInvoiceService;

    public FeeInvoiceController(FeeInvoiceService feeInvoiceService) {
        this.feeInvoiceService = feeInvoiceService;
    }

    @PostMapping
    public ResponseEntity<FeeInvoice> create(@RequestBody FeeInvoice feeInvoice) {
        return new ResponseEntity<>(feeInvoiceService.create(feeInvoice), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FeeInvoice>> getAll() {
        return ResponseEntity.ok(feeInvoiceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeeInvoice> getById(@PathVariable Long id) {
        return ResponseEntity.ok(feeInvoiceService.getById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<FeeInvoice>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(feeInvoiceService.getByStudentId(studentId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<FeeInvoice>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(feeInvoiceService.getByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeeInvoice> update(@PathVariable Long id, @RequestBody FeeInvoice feeInvoice) {
        return ResponseEntity.ok(feeInvoiceService.update(id, feeInvoice));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        feeInvoiceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
