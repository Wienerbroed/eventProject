package org.example.eventproject.repositories;

import org.example.eventproject.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EventRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Add Event
    public int addEvent(Events event) {
        String sql = "INSERT INTO Events (title, event_creator, event_responsible, event_control, event_type, description, max_participants, max_audience, conguide_dk, conguide_en, venue_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, event.getTitle(), event.getEventCreator(), event.getEventResponsible(), event.getEventControl(), event.getEventType(), event.getDescription(), event.getMaxParticipants(), event.getMaxAudience(), event.getConguideDk(), event.getConguideEn(), event.getVenueId());
    }

    // Find Event by ID
    public Optional<Events> findById(Long id) {
        String sql = "SELECT * FROM events WHERE event_id = ?";
        try {
            Events event = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Events.class));
            return Optional.ofNullable(event);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // Return Optional.empty if no event is found
        }
    }

    // Get all events
    public List<Events> findAll() {
        String sql = "SELECT * FROM events";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Events.class));
    }


    public List<Events> getAllEventsWithVenueDetails() {
        String sql = """
    SELECT e.event_id, e.title, e.event_creator, e.event_responsible, 
           e.event_control, e.event_type, e.description, 
           e.max_participants, e.max_audience, e.conguide_dk, 
           e.conguide_en, v.venue_id, v.venue_name, v.venue_address
    FROM Events e
    JOIN venue v ON e.venue_id = v.venue_id
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            // Create a new Event instance
            Events event = new Events();
            event.setEventId(rs.getLong("event_id"));
            event.setTitle(rs.getString("title"));
            event.setEventCreator(rs.getString("event_creator"));
            event.setEventResponsible(rs.getString("event_responsible"));
            event.setEventControl(rs.getString("event_control"));
            event.setEventType(rs.getString("event_type"));
            event.setDescription(rs.getString("description"));
            event.setMaxParticipants(rs.getInt("max_participants"));
            event.setMaxAudience(rs.getInt("max_audience"));
            event.setConguideDk(rs.getString("conguide_dk"));
            event.setConguideEn(rs.getString("conguide_en"));

            // Create a Venue instance and set it to the Event
            Venue venue = new Venue();
            venue.setVenueId(rs.getLong("venue_id"));
            venue.setVenueName(rs.getString("venue_name"));
            venue.setVenueAddress(rs.getString("venue_address"));

            // Set the venue in the event object
            event.setVenue(venue);

            return event;
        });
    }


    // Delete event by ID
    public int deleteById(Long id) {
        String sql = "DELETE FROM events WHERE event_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    // Check if event exists
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM events WHERE event_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }

    public int addEventExpense(EventExpenses expense) {
        String sql = "INSERT INTO EventExpenses (event_id, time, prize, cost) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, expense.getEvent().getEventId(), expense.getTime(), expense.getPrize(), expense.getCost());
    }

    public int addEventRequirement(EventRequirements requirement) {
        String sql = "INSERT INTO EventRequirements (event_id, praktiske_krav, tekniske_krav, materialebehov, gopherbehov) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, requirement.getEvent().getEventId(), requirement.getPraktiskeKrav(), requirement.getTekniskeKrav(), requirement.getMaterialebehov(), requirement.getGopherbehov());
    }

    public int addEventSchedule(EventSchedule schedule) {
        String sql = "INSERT INTO EventSchedule (event_id, schedule_date, start_time, end_time) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, schedule.getEvent().getEventId(), schedule.getScheduleDate(), schedule.getStartTime(), schedule.getEndTime());
    }

    public int updateEvent(Events event) {
        String sql = "UPDATE events SET title = ?, event_creator = ?, event_responsible = ?, event_control = ?, event_type = ?, description = ?, max_participants = ?, max_audience = ?, conguide_dk = ?, conguide_en = ?, venue_id = ? WHERE event_id = ?";
        return jdbcTemplate.update(sql, event.getTitle(), event.getEventCreator(), event.getEventResponsible(), event.getEventControl(), event.getEventType(), event.getDescription(), event.getMaxParticipants(), event.getMaxAudience(), event.getConguideDk(), event.getConguideEn(),event.getVenueId(), event.getEventId());
    }

    public List<Events> getEventsByVenueId(Long venueId) {
        String sql = """
            SELECT e.event_id, e.title, e.event_creator, e.event_responsible, 
                   e.event_control, e.event_type, e.description, 
                   e.max_participants, e.max_audience, e.conguide_dk, 
                   e.conguide_en, e.venue_id
            FROM Events e
            WHERE e.venue_id = ?
        """;

        return jdbcTemplate.query(sql, new Object[]{venueId}, (rs, rowNum) -> {
            Events event = new Events();
            event.setEventId(rs.getLong("event_id"));
            event.setTitle(rs.getString("title"));
            event.setEventCreator(rs.getString("event_creator"));
            event.setEventResponsible(rs.getString("event_responsible"));
            event.setEventControl(rs.getString("event_control"));
            event.setEventType(rs.getString("event_type"));
            event.setDescription(rs.getString("description"));
            event.setMaxParticipants(rs.getInt("max_participants"));
            event.setMaxAudience(rs.getInt("max_audience"));
            event.setConguideDk(rs.getString("conguide_dk"));
            event.setConguideEn(rs.getString("conguide_en"));
            event.setVenueId(rs.getLong("venue_id"));
            return event;
        });
    }

}
