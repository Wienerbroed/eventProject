package main.java.org.example.eventproject.controllers;

import main.java.org.example.eventproject.models.EventRoom;
import main.java.org.example.eventproject.services.EventRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/event-rooms")
public class EventRoomController {

    private final EventRoomService eventRoomService;

    @Autowired
    public EventRoomController(EventRoomService eventRoomService) {
        this.eventRoomService = eventRoomService;
    }

    // Get all event rooms
    @GetMapping
    public ResponseEntity<List<EventRoom>> getAllEventRooms() {
        List<EventRoom> eventRooms = eventRoomService.getAllEventRooms();
        return ResponseEntity.ok(eventRooms);
    }

    // Sample method to add an event room (restricted to certain roles)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN', 'UDVIKLER', 'AFVIKLER')")
    @PostMapping("/add")
    public ResponseEntity<EventRoom> addEventRoom(@RequestBody EventRoom eventRoom) {
        EventRoom createdRoom = eventRoomService.addEventRoom(eventRoom);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    // Get an event room by ID
    @GetMapping("/{eventRoomId}")
    public ResponseEntity<EventRoom> getEventRoomById(@PathVariable Long eventRoomId) {
        return eventRoomService.getEventRoomById(eventRoomId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Sample method to update an event room (restricted to certain roles)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN', 'UDVIKLER', 'AFVIKLER')")
    @PutMapping("/{eventRoomId}")
    public ResponseEntity<EventRoom> updateEventRoom(
            @PathVariable Long eventRoomId,
            @RequestBody EventRoom eventRoom) {
        eventRoom.setEventRoomId(eventRoomId);
        boolean updated = eventRoomService.updateEventRoom(eventRoom);
        if (updated) {
            return ResponseEntity.ok(eventRoom);
        }
        return ResponseEntity.notFound().build();
    }

    // Sample method to delete an event room (restricted to certain roles)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN', 'UDVIKLER', 'AFVIKLER')")
    @DeleteMapping("/{eventRoomId}")
    public ResponseEntity<Void> deleteEventRoom(@PathVariable Long eventRoomId) {
        boolean deleted = eventRoomService.deleteEventRoomById(eventRoomId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Get event rooms by venue ID
    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<EventRoom>> getEventRoomsByVenueId(@PathVariable Long venueId) {
        List<EventRoom> eventRooms = eventRoomService.getEventRoomsByVenueId(venueId);
        if (eventRooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(eventRooms);
    }
}