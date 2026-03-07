package com.monocampusconnect.controller;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Event;
import com.monocampusconnect.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    /** POST /api/events — create event (multipart) */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> createEvent(
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("postedBy") String postedBy,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {

        if (title == null || title.isBlank()) throw new ApiException("Title is required", 400);
        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setPostedBy(postedBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(event, imageFile));
    }

    /** GET /api/events — all events for current tenant */
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    /** GET /api/events/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    /** GET /api/events/user/{postedBy} */
    @GetMapping("/user/{postedBy}")
    public ResponseEntity<List<Event>> getEventsByUser(@PathVariable String postedBy) {
        return ResponseEntity.ok(eventService.getEventsByUser(postedBy));
    }

    /** PUT /api/events/{id} — update event (multipart) */
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {

        Event details = new Event();
        details.setTitle(title);
        details.setDescription(description);
        return ResponseEntity.ok(eventService.updateEvent(id, details, imageFile));
    }

    /** DELETE /api/events/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(Map.of("message", "Event deleted successfully"));
    }
}
