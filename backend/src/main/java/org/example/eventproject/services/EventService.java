package org.example.eventproject.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.eventproject.models.EventExpenses;
import org.example.eventproject.models.EventRequirements;
import org.example.eventproject.models.EventSchedule;
import org.example.eventproject.repositories.EventRepository;
import org.example.eventproject.models.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Add Event
    public Events addEvent(Events event) {
        eventRepository.addEvent(event);
        return event;
    }

    //add event Schedule
    public int addEventSchedule(EventSchedule schedule) {
        return eventRepository.addEventSchedule(schedule);
    }



    // Get Event by ID
    public Optional<Events> getEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    // Get All Events
    public List<Events> getAllEvents() {
        return eventRepository.findAll();
    }

    // Service method to fetch all events with venue details
    public List<Events> getAllEventsWithVenueDetails() {
        // Call the repository method to fetch events with venue details
        return eventRepository.getAllEventsWithVenueDetails();
    }


    // Delete Event
    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Event not found with ID: " + eventId);
        }
        eventRepository.deleteById(eventId);
    }

    public Events updateEvent(Events event) {
        Long eventId = event.getEventId();
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Event not found with ID: " + eventId);
        }

        Optional<Events> existingEventOpt = eventRepository.findById(eventId);
        if (existingEventOpt.isEmpty()) {
            throw new EntityNotFoundException("Event not found with ID: " + eventId);
        }

        Events existingEvent = existingEventOpt.get();

        // If no venue is provided, keep the existing one
        if (event.getVenue() == null) {
            event.setVenue(existingEvent.getVenue());
        }

        // If warnings are not provided in the updated event, keep the existing warnings
        if (event.getWarnings() == null || event.getWarnings().trim().isEmpty()) {
            event.setWarnings(existingEvent.getWarnings());
        }

        // Ensure the venue is not null
        if (event.getVenue() == null) {
            throw new IllegalArgumentException("Venue cannot be null");
        }

        // Now call the repository update method, which includes the warnings field
        eventRepository.updateEvent(event);
        return event;
    }



    public List<Events> getEventsByVenueId(Long venueId) {
        return eventRepository.getEventsByVenueId(venueId);
    }


    public List<EventSchedule> getEventSchedule(Long eventId) {
        return eventRepository.findScheduleByEventId(eventId);
    }

    public void deleteSchedule(Long scheduleId) {
        eventRepository.deleteScheduleById(scheduleId);
    }


    public void updateEventSchedule(Long scheduleId, EventSchedule updatedSchedule) {
        EventSchedule existingSchedule = eventRepository.findScheduleById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id " + scheduleId));

        existingSchedule.setScheduleDate(updatedSchedule.getScheduleDate());
        existingSchedule.setStartTime(updatedSchedule.getStartTime());
        existingSchedule.setEndTime(updatedSchedule.getEndTime());
        // Update other fields as necessary

        eventRepository.updateEventSchedule(existingSchedule);
    }

    public List<EventSchedule> getEventSchedules() {
        return eventRepository.getEventSchedules();
    }

    public int addEventExpense(EventExpenses expense) {
        return eventRepository.addEventExpense(expense);
    }


    public void addEventRequirement(EventRequirements requirement) {
        Optional<Events> eventOptional = eventRepository.findById(requirement.getEventId());
        if (eventOptional.isPresent()) {
            requirement.setEvent(eventOptional.get());
            eventRepository.addEventRequirement(requirement);
        } else {
            throw new RuntimeException("Event not found with ID: " + requirement.getEventId());
        }
    }

    public List<EventExpenses> findExpensesByEventId(Long eventId) {
        return eventRepository.findExpensesByEventId(eventId);
    }

    public List<EventRequirements> findRequirementsByEventId(Long eventId) {
        return eventRepository.findRequirementsByEventId(eventId);
    }


    public void deleteRequirementByEventId(Long eventId) {
        eventRepository.deleteRequirementByEventId(eventId);
    }



    public int deleteExpenseById(Long expenseId) {
        return eventRepository.deleteExpenseById(expenseId);
    }

}