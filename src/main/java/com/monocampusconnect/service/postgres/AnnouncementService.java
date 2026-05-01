package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.Announcement;
import com.monocampusconnect.repository.postgres.AnnouncementRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public Announcement create(Announcement announcement) {
        if (announcement.getAnnouncementPublicId() == null) {
            announcement.setAnnouncementPublicId(UUID.randomUUID());
        }
        if (announcement.getPublishedAt() == null) {
            announcement.setPublishedAt(OffsetDateTime.now());
        }
        return announcementRepository.save(announcement);
    }

    public List<Announcement> getAll() {
        return announcementRepository.findAll();
    }

    public Announcement getById(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new ApiException("Announcement not found", 404));
    }

    public Announcement update(Long id, Announcement announcement) {
        Announcement existing = getById(id);
        if (announcement.getTitle() != null) existing.setTitle(announcement.getTitle());
        if (announcement.getContent() != null) existing.setContent(announcement.getContent());
        if (announcement.getAudience() != null) existing.setAudience(announcement.getAudience());
        if (announcement.getPriority() != null) existing.setPriority(announcement.getPriority());
        if (announcement.getPinnedUntil() != null) existing.setPinnedUntil(announcement.getPinnedUntil());
        if (announcement.getExpiresAt() != null) existing.setExpiresAt(announcement.getExpiresAt());
        return announcementRepository.save(existing);
    }

    public void delete(Long id) {
        Announcement announcement = getById(id);
        announcementRepository.delete(announcement);
    }

    public List<Announcement> getByTenantId(UUID tenantId) {
        return announcementRepository.findAll().stream()
                .filter(a -> tenantId.equals(a.getTenantId()))
                .toList();
    }

    public List<Announcement> getActive() {
        OffsetDateTime now = OffsetDateTime.now();
        return announcementRepository.findAll().stream()
                .filter(a -> (a.getPublishedAt() == null || a.getPublishedAt().isBefore(now)) &&
                            (a.getExpiresAt() == null || a.getExpiresAt().isAfter(now)))
                .toList();
    }
}

