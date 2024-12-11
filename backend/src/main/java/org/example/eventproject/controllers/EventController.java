package org.example.eventproject.controllers;

import org.example.eventproject.models.EventSchedule;
import org.example.eventproject.models.Events;
import org.example.eventproject.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.eventproject.models.EventExpenses;
import org.example.eventproject.models.EventRequirements;


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

    @PostMapping("/addSchedule/{eventId}")
    public ResponseEntity<String> addEventSchedule(@PathVariable Long eventId, @RequestBody EventSchedule schedule) {
        try {
            System.out.println("Received schedule: " + schedule); // Add logging
            schedule.setEventId(eventId);
            eventService.addEventSchedule(schedule);
            return ResponseEntity.ok("Schedule added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding schedule: " + e.getMessage());
        }
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

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<Events>> getEventsByVenue(@PathVariable Long venueId) {
        List<Events> events = eventService.getEventsByVenueId(venueId);
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}/schedule")
    public List<EventSchedule> getEventSchedule(@PathVariable Long eventId) {
        return eventService.getEventSchedule(eventId);
    }


    @DeleteMapping("/schedule/{scheduleId}")
    public void deleteSchedule(@PathVariable Long scheduleId) {
        eventService.deleteSchedule(scheduleId);
    }



    @PutMapping("/schedule/{scheduleId}")
    public ResponseEntity<String> updateEventSchedule(@PathVariable Long scheduleId, @RequestBody EventSchedule updatedSchedule) {
        try {
            eventService.updateEventSchedule(scheduleId, updatedSchedule);
            return ResponseEntity.ok("Event updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating event: " + e.getMessage());
        }
    }


    @GetMapping("/schedules")
    public ResponseEntity<List<EventSchedule>> getEventSchedules() {
        List<EventSchedule> schedules = eventService.getEventSchedules();
        return ResponseEntity.ok(schedules);
    }

    @PostMapping("/addExpense/{eventId}")
    public ResponseEntity<String> addEventExpense(@PathVariable Long eventId, @RequestBody EventExpenses expense) {
        try {
            Optional<Events> event = eventService.getEventById(eventId);
            if (event.isPresent()) {
                expense.setEvent(event.get());
                eventService.addEventExpense(expense);
                return ResponseEntity.ok("Expense added successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found with ID: " + eventId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding expense: " + e.getMessage());
        }
    }


    @PostMapping("/addRequirement/{eventId}")
    public ResponseEntity<String> addEventRequirement(@PathVariable Long eventId, @RequestBody EventRequirements requirement) {
        try {
            requirement.setEventId(eventId);
            eventService.addEventRequirement(requirement);
            return ResponseEntity.ok("Requirement added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding requirement: " + e.getMessage());
        }
    }
    @DeleteMapping("/requirement/{requirementId}")
    public ResponseEntity<Void> deleteEventRequirement(@PathVariable Long requirementId) {
        eventService.deleteRequirementById(requirementId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/expense/{expenseId}")
    public ResponseEntity<Void> deleteEventExpense(@PathVariable Long expenseId) {
        eventService.deleteExpenseById(expenseId);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/{eventId}/expenses")
    public ResponseEntity<List<EventExpenses>> getEventExpenses(@PathVariable Long eventId) {
        List<EventExpenses> expenses = eventService.findExpensesByEventId(eventId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{eventId}/requirements")
    public ResponseEntity<List<EventRequirements>> getEventRequirements(@PathVariable Long eventId) {
        List<EventRequirements> requirements = eventService.findRequirementsByEventId(eventId);
        return ResponseEntity.ok(requirements);
    }



}
