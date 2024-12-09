package org.example.eventproject.controllers;

import org.example.eventproject.models.EventRoom;
import org.example.eventproject.services.EventRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/eventrooms")
public class EventRoomController {

    private final EventRoomService eventRoomService;

    @Autowired
    public EventRoomController(EventRoomService eventRoomService) {
        this.eventRoomService = eventRoomService;
    }

    //Get all events
    @GetMapping()
    public ResponseEntity<List<EventRoom>> getAllEventRooms() {
        List<EventRoom> eventRooms = eventRoomService.getAllEventRooms();
        return ResponseEntity.ok(eventRooms);
    }

    //Add event
    @PostMapping("/add")
    public ResponseEntity<EventRoom> addEventRoom(@RequestBody EventRoom eventRoom) {
        eventRoomService.addEventRoom(eventRoom);
        return ResponseEntity.ok(eventRoom);
    }

    //Get event by id
    @GetMapping("/{eventRoomId}")
    public ResponseEntity<EventRoom> getEventRoomById(@PathVariable long eventRoomId) {
        Optional<EventRoom> eventRoom = eventRoomService.getEventRoomById(eventRoomId);
        return eventRoom.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Delete event
    @DeleteMapping("/{eventRoomId}")
    public ResponseEntity<Void> deleteEventRoom(@PathVariable long eventRoomId) {
        eventRoomService.deleteEventRoomById(eventRoomId);
        return ResponseEntity.noContent().build();
    }

    //Update event
    @PostMapping("/{eventRoomId}")
    public ResponseEntity<EventRoom> updateEventRoom(@PathVariable long eventRoomId, @RequestBody EventRoom eventRoom) {
        eventRoom.setEventRoomId(eventRoomId);
        EventRoom updatedEventRoom = eventRoomService.updateEventRoom(eventRoom);
        return ResponseEntity.ok(updatedEventRoom);
    }

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<EventRoom>> getEventRoomsByVenueId(long venueId) {
        List<EventRoom> eventRooms = eventRoomService.getEventRoomsByVenueId(venueId);
        if (eventRooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(eventRooms);
    }
}
