package org.example.eventproject.controllers;


import org.example.eventproject.models.Venue;
import org.example.eventproject.services.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/venues")
public class VenueController {

    private final VenueService venueService;

    @Autowired
    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    // Get all venues
    @GetMapping
    public ResponseEntity<List<Venue>> getAllVenues() {
        List<Venue> venues = venueService.getAllVenues();
        return new ResponseEntity<>(venues, HttpStatus.OK);
    }


    // Add a new venue
    @PostMapping("/add")
    public ResponseEntity<Venue> addVenue(@RequestBody Venue venue) {
        Venue createdVenue = venueService.addVenue(venue);
        return ResponseEntity.ok(createdVenue);
    }

    // Find a venue by ID
    @GetMapping("/{venueId}")
    public ResponseEntity<Venue> findVenueById(@PathVariable Long venueId) {
        return venueService.findVenueById(venueId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Delete a venue by ID
    @DeleteMapping("/{venueId}")
    public ResponseEntity<Void> deleteVenueById(@PathVariable Long venueId) {
        if (venueService.deleteVenueById(venueId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }



}
