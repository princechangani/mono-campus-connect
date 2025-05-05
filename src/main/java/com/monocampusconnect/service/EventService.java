package com.monocampusconnect.service;

import com.monocampusconnect.model.Event;
import com.monocampusconnect.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event, MultipartFile imageFile) throws IOException {
        // Store image content directly in the database
        event.setImageContent(imageFile.getBytes());
        event.setCreatedAt(new Date());
        event.setUpdatedAt(new Date());
        
        return eventRepository.save(event);
    }

    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEventsByUser(String postedBy) {
        return eventRepository.findByPostedBy(postedBy);
    }

    public Event updateEvent(Long id, Event eventDetails, MultipartFile imageFile) throws IOException {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Update image if new image is provided
        if (imageFile != null && !imageFile.isEmpty()) {
            event.setImageContent(imageFile.getBytes());
        }

        // Update event details
        event.setTitle(eventDetails.getTitle());
        event.setDescription(eventDetails.getDescription());
        event.setUpdatedAt(new Date());

        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        eventRepository.deleteById(id);
    }
}
