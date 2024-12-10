package org.example.eventproject.services;

import org.example.eventproject.models.Events;
import org.example.eventproject.models.Venue;
import org.example.eventproject.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    @Autowired
    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    // Add a new venue
    public Venue addVenue(Venue venue) {
        venueRepository.addEvent(venue);
        return venue;
    }


    // Find a venue by ID
    public Optional<Venue> findVenueById(Long venueId) {
        return venueRepository.findById(venueId);
    }

    // Get all venues
    public List<Venue> getAllVenues() {
        return venueRepository.findAllVenues();
    }

    // Delete a venue by ID
    public boolean deleteVenueById(Long venueId) {
        if (venueRepository.existsById(venueId)) {
            venueRepository.deleteById(venueId);
            return true;
        }
        return false; // Venue not found
    }

    // Check if a venue exists by ID
    public boolean venueExists(Long venueId) {
        return venueRepository.existsById(venueId);
    }

    // Update venue details
    public boolean updateVenue(Venue venue) {
        if (venueRepository.existsById(venue.getVenueId())) {
            venueRepository.updateVenue(venue);
            return true;
        }
        return false; // Venue not found
    }

    // Find a venue by name
    public Optional<Venue> findVenueByName(String venueName) {
        return venueRepository.findByName(venueName);
    }
}
