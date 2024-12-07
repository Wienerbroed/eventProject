package org.example.eventproject.services;

import jakarta.persistence.EntityNotFoundException;
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

    // Update Event
    public Events updateEvent(Events event) {
        if (!eventRepository.existsById(event.getEventId())) {
            throw new EntityNotFoundException("Event not found with ID: " + event.getEventId());
        }
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


}