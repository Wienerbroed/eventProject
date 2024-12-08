package main.java.org.example.eventproject.services;

import main.java.org.example.eventproject.models.EventRoom;
import main.java.org.example.eventproject.repositories.EventRoomRepository;
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

    // Add a new EventRoom
    public EventRoom addEventRoom(EventRoom eventRoom) {
        eventRoomRepository.addEventRoom(eventRoom);
        return eventRoom;
    }

    // Get EventRoom by ID
    public Optional<EventRoom> getEventRoomById(Long eventRoomId) {
        return eventRoomRepository.findById(eventRoomId);
    }

    // Get all EventRooms
    public List<EventRoom> getAllEventRooms() {
        return eventRoomRepository.findAll();
    }

    // Update EventRoom details
    public boolean updateEventRoom(EventRoom eventRoom) {
        if (eventRoomRepository.existsById(eventRoom.getEventRoomId())) {
            eventRoomRepository.updateEventRoom(eventRoom);
            return true;
        }
        return false; // EventRoom not found
    }

    // Delete EventRoom by ID
    public boolean deleteEventRoomById(Long eventRoomId) {
        if (eventRoomRepository.existsById(eventRoomId)) {
            eventRoomRepository.deleteById(eventRoomId);
            return true;
        }
        return false; // EventRoom not found
    }

    // Get EventRooms by Venue ID
    public List<EventRoom> getEventRoomsByVenueId(Long venueId) {
        return eventRoomRepository.findByVenueId(venueId);
    }
}
