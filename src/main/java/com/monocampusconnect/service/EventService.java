package com.monocampusconnect.service;

import com.monocampusconnect.config.TenantContextHolder;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Event;
import com.monocampusconnect.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private UUID currentTenant() {
        UUID tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) throw new ApiException("Tenant context missing", 400);
        return tenantId;
    }

    public Event createEvent(Event event, MultipartFile imageFile) throws IOException {
        event.setTenantId(currentTenant());
        if (imageFile != null && !imageFile.isEmpty()) {
            event.setImageContent(imageFile.getBytes());
        }
        event.setCreatedAt(new Date());
        event.setUpdatedAt(new Date());
        return eventRepository.save(event);
    }

    public Event getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ApiException("Event not found", 404));
        if (!currentTenant().equals(event.getTenantId()))
            throw new ApiException("Event not found", 404);
        return event;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findByTenantId(currentTenant());
    }

    public List<Event> getEventsByUser(String postedBy) {
        return eventRepository.findByPostedBy(postedBy);
    }

    public Event updateEvent(Long id, Event eventDetails, MultipartFile imageFile) throws IOException {
        Event event = getEvent(id);
        if (imageFile != null && !imageFile.isEmpty()) {
            event.setImageContent(imageFile.getBytes());
        }
        event.setTitle(eventDetails.getTitle());
        event.setDescription(eventDetails.getDescription());
        event.setUpdatedAt(new Date());
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = getEvent(id);
        eventRepository.deleteById(event.getEventId());
    }
}
