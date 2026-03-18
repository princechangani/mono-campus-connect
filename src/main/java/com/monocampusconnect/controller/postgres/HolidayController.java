package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.Holiday;
import com.monocampusconnect.service.postgres.HolidayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @PostMapping
    public ResponseEntity<Holiday> create(@RequestBody Holiday holiday) {
        return new ResponseEntity<>(holidayService.create(holiday), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Holiday>> getAll() {
        return ResponseEntity.ok(holidayService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Holiday> getById(@PathVariable Long id) {
        return ResponseEntity.ok(holidayService.getById(id));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Holiday>> getByTenantId(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(holidayService.getByTenantId(tenantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Holiday> update(@PathVariable Long id, @RequestBody Holiday holiday) {
        return ResponseEntity.ok(holidayService.update(id, holiday));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        holidayService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
