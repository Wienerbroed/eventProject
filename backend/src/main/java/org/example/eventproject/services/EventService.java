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

    // Service method to fetch all events with event room details
    public List<Events> getAllEventsWithEventRoomDetails() {
        // Call the repository method to fetch events with event room details
        return eventRepository.getAllEventsWithEventRoomDetails();
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



    // Get Events by Event Room ID
    public List<Events> getEventsByEventRoomId(Long eventRoomId) {
        return eventRepository.getEventsByEventRoomId(eventRoomId);
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



}