package com.monocampusconnect.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.monocampusconnect.model.Event;
import com.monocampusconnect.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {


    private final EventRepository eventRepository;


    private final AmazonS3 amazonS3;


    private String bucketName;

    @Autowired
    public EventService(EventRepository eventRepository, AmazonS3 amazonS3) {
        this.eventRepository = eventRepository;
        this.amazonS3 = amazonS3;
    }

    public Event createEvent(Event event, MultipartFile imageFile) throws IOException {
        // Save image to S3
        String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, imageFile.getInputStream(), null));
        
        // Set event properties
        event.setImageUrl("https://" + bucketName + ".s3.amazonaws.com/" + fileName);
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
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, imageFile.getInputStream(), null));
            event.setImageUrl("https://" + bucketName + ".s3.amazonaws.com/" + fileName);
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

        // Delete image from S3
        String fileName = event.getImageUrl().split("/")[4];
        amazonS3.deleteObject(bucketName, fileName);

        eventRepository.deleteById(id);
    }
}
