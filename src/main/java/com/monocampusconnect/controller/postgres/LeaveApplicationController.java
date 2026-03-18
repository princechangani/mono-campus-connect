package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.LeaveApplication;
import com.monocampusconnect.service.postgres.LeaveApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-applications")
public class LeaveApplicationController {

    private final LeaveApplicationService leaveApplicationService;

    public LeaveApplicationController(LeaveApplicationService leaveApplicationService) {
        this.leaveApplicationService = leaveApplicationService;
    }

    @PostMapping
    public ResponseEntity<LeaveApplication> create(@RequestBody LeaveApplication leaveApplication) {
        return new ResponseEntity<>(leaveApplicationService.create(leaveApplication), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LeaveApplication>> getAll() {
        return ResponseEntity.ok(leaveApplicationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveApplication> getById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveApplicationService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LeaveApplication>> getByApplicantUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveApplicationService.getByApplicantUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeaveApplication>> getByApprovalStatus(@PathVariable String status) {
        return ResponseEntity.ok(leaveApplicationService.getByApprovalStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveApplication> update(@PathVariable Long id, @RequestBody LeaveApplication leaveApplication) {
        return ResponseEntity.ok(leaveApplicationService.update(id, leaveApplication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        leaveApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

