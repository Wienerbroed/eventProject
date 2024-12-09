package org.example.eventproject.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.eventproject.models.EventRoom;
import org.example.eventproject.repositories.EventRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventRoomService {

    private final EventRoomRepository eventRoomRepository;

    @Autowired
    public EventRoomService(EventRoomRepository eventRoomRepository) {
        this.eventRoomRepository = eventRoomRepository;
    }

    //Add event room
    public EventRoom addEventRoom(EventRoom eventRoom) {
        eventRoomRepository.addEventRoom(eventRoom);
        return eventRoom;
    }

    //Get event room by id
    public Optional<EventRoom> getEventRoomById(long eventRoomId) {
        return eventRoomRepository.findEventRoomById(eventRoomId);
    }

    //Get all event rooms
    public List<EventRoom> getAllEventRooms() {
        return eventRoomRepository.findAllEventRooms();
    }

    public List<EventRoom> getAllEventRoomsWithVenueDetails() {
        return eventRoomRepository.getAllEventRoomsWithVenueDetails();
    }

    //Delete event room
    public void deleteEventRoomById(long eventRoomId) {
        if (!eventRoomRepository.eventRoomExists(eventRoomId)) {
            throw new EntityNotFoundException("Event room does not exist" + eventRoomId);
        }
        eventRoomRepository.deleteEventRoomById(eventRoomId);
    }

    //Update event room
    public EventRoom updateEventRoom(EventRoom eventRoom) {
        if (!eventRoomRepository.eventRoomExists(eventRoom.getEventRoomId())){
            throw new EntityNotFoundException("Event room does not exist" + eventRoom.getEventRoomId());
        }
        eventRoomRepository.updateEventRoom(eventRoom);
        return eventRoom;
    }

    public List<EventRoom> getEventRoomsByVenueId(long venueId) {
        return eventRoomRepository.getEventRoomByVenueId(venueId);
    }
}
