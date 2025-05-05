package com.monocampusconnect.controller;

import com.monocampusconnect.dto.EventRequest;
import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.Event;
import java.util.Date;
import com.monocampusconnect.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Event> createEvents(
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("postedBy") String postedBy,
            @RequestPart("image") MultipartFile imageFile) throws IOException {
        if (title == null || description == null) {
            throw new ApiException("Title and description are required", 400);
        }
        
        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setPostedBy(postedBy);
        event.setCreatedAt(new Date());
        
        return ResponseEntity.ok(eventService.createEvent(event, imageFile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

}
