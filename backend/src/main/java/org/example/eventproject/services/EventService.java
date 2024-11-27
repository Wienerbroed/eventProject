package org.example.eventproject.services;

import jakarta.persistence.EntityNotFoundException;
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

    // Get Event by ID
    public Optional<Events> getEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    // Get All Events
    public List<Events> getAllEvents() {
        return eventRepository.findAll();
    }

    // Delete Event
    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException("Event not found with ID: " + eventId);
        }
        eventRepository.deleteById(eventId);
    }
}