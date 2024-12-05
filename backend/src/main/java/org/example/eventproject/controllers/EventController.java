package org.example.eventproject.controllers;

import org.example.eventproject.models.EventSchedule;
import org.example.eventproject.models.Events;
import org.example.eventproject.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<Events>> getAllEventsWithVenueDetails() {
        List<Events> events = eventService.getAllEventsWithVenueDetails();
        return ResponseEntity.ok(events);
    }


    // Add Event
    @PostMapping("/add")
    public ResponseEntity<Events> addEvent(@RequestBody Events event) {
        Events createdEvent = eventService.addEvent(event);
        return ResponseEntity.ok(createdEvent);
    }

   /* // Add Event Schedule
    @PostMapping("/addSchedule/{eventId}")
    public ResponseEntity<EventSchedule> addEventSchedule(@PathVariable Long eventId, @RequestBody EventSchedule schedule) {
        // Ensure the schedule is associated with the eventId
        schedule.setEventId(eventId);

        int result = eventService.addEventSchedule(schedule);

        if (result == 1) {
            return ResponseEntity.ok(schedule);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Optionally return null or error details
        }
    }*/



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

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<Events>> getEventsByVenue(@PathVariable Long venueId) {
        List<Events> events = eventService.getEventsByVenueId(venueId);
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }



}
