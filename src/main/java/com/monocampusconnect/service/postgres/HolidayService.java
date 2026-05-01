package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.Holiday;
import com.monocampusconnect.repository.postgres.HolidayRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    public Holiday create(Holiday holiday) {
        if (holiday.getHolidayPublicId() == null) {
            holiday.setHolidayPublicId(UUID.randomUUID());
        }
        return holidayRepository.save(holiday);
    }

    public List<Holiday> getAll() {
        return holidayRepository.findAll();
    }

    public Holiday getById(Long id) {
        return holidayRepository.findById(id)
                .orElseThrow(() -> new ApiException("Holiday not found", 404));
    }

    public Holiday update(Long id, Holiday holiday) {
        Holiday existing = getById(id);
        if (holiday.getTitle() != null) existing.setTitle(holiday.getTitle());
        if (holiday.getHolidayDate() != null) existing.setHolidayDate(holiday.getHolidayDate());
        if (holiday.getHolidayType() != null) existing.setHolidayType(holiday.getHolidayType());
        if (holiday.getAppliesTo() != null) existing.setAppliesTo(holiday.getAppliesTo());
        if (holiday.getDescription() != null) existing.setDescription(holiday.getDescription());
        return holidayRepository.save(existing);
    }

    public void delete(Long id) {
        Holiday holiday = getById(id);
        holidayRepository.delete(holiday);
    }

    public List<Holiday> getByTenantId(UUID tenantId) {
        return holidayRepository.findAll().stream()
                .filter(h -> tenantId.equals(h.getTenantId()))
                .toList();
    }
}

