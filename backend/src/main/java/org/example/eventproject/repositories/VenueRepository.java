package org.example.eventproject.repositories;

import org.example.eventproject.models.Events;
import org.example.eventproject.models.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VenueRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public VenueRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Add Venue
    public int addEvent(Venue venue) {
        String sql = "INSERT INTO venue (venue_name, venue_address) VALUES (?, ?)";
        return jdbcTemplate.update(sql, venue.getVenueName(), venue.getVenueAddress());
    }

    // Find Venue by ID
    public Optional<Venue> findById(Long venueId) {
        String sql = "SELECT * FROM venue WHERE venue_id = ?";
        try {
            Venue venue = jdbcTemplate.queryForObject(sql, new Object[]{venueId}, new BeanPropertyRowMapper<>(Venue.class));
            return Optional.ofNullable(venue);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // Return Optional.empty if no event is found
        }
    }

    // Get all venues
    public List<Venue> findAllVenues() {
        String sql = "SELECT * FROM venue";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Venue.class));
    }

    // Delete venue by ID
    public int deleteById(Long venueId) {
        String sql = "DELETE FROM venue WHERE venue_id = ?";
        return jdbcTemplate.update(sql, venueId);
    }

    // Check if venue exists
    public boolean existsById(Long venueId) {
        String sql = "SELECT COUNT(*) FROM venue WHERE venue_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{venueId}, Integer.class);
        return count != null && count > 0;
    }

    // Update venue by ID
    public int updateVenue(Venue venue) {
        String sql = "UPDATE venue SET venue_name = ?, venue_address = ? WHERE venue_id = ?";
        return jdbcTemplate.update(sql, venue.getVenueName(), venue.getVenueAddress(), venue.getVenueId());
    }

    // Get venue by name
    public Optional<Venue> findByName(String venueName) {
        String sql = "SELECT * FROM venue WHERE venue_name = ?";
        try {
            Venue venue = jdbcTemplate.queryForObject(sql, new Object[]{venueName}, new BeanPropertyRowMapper<>(Venue.class));
            return Optional.ofNullable(venue);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // Return Optional.empty if no event is found
        }
    }











}
