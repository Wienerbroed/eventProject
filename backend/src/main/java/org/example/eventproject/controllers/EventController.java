package org.example.eventproject.controllers;

import org.example.eventproject.models.Events;
import org.example.eventproject.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    // Get All Events
    @GetMapping()
    public ResponseEntity<List<Events>> getAllEvents() {
        List<Events> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }


    // Add Event
    @PostMapping("/add")
    public ResponseEntity<Events> addEvent(@RequestBody Events event) {
        Events createdEvent = eventService.addEvent(event);
        return ResponseEntity.ok(createdEvent);
    }

    // Get Event by ID
    @GetMapping("/{eventId}")
    public ResponseEntity<Events> getEventById(@PathVariable Long eventId) {
        Optional<Events> event = eventService.getEventById(eventId);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete Event
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
    // Update Event
    @PostMapping("/{eventId}")
    public ResponseEntity<Events> updateEvent(@PathVariable Long eventId, @RequestBody Events event) {
        event.setEventId(eventId);
        Events updatedEvent = eventService.updateEvent(event);
        return ResponseEntity.ok(updatedEvent);
    }



}
