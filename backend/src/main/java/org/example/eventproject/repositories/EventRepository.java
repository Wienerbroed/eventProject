package main.java.org.example.eventproject.repositories;

import main.java.org.example.eventproject.models.*;
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
        String sql = """
            INSERT INTO Events (title, event_creator, event_responsible, 
                                event_control, event_type, description, 
                                max_participants, max_audience, conguide_dk, 
                                conguide_en, eventRoom_id) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        return jdbcTemplate.update(sql, event.getTitle(), event.getEventCreator(),
                event.getEventResponsible(), event.getEventControl(), event.getEventType(),
                event.getDescription(), event.getMaxParticipants(), event.getMaxAudience(),
                event.getConguideDk(), event.getConguideEn(), event.getEventRoomId());
    }

    // Find Event by ID
    public Optional<Events> findById(Long id) {
        String sql = """
            SELECT e.event_id, e.title, e.event_creator, e.event_responsible, 
                   e.event_control, e.event_type, e.description, 
                   e.max_participants, e.max_audience, e.conguide_dk, 
                   e.conguide_en, 
                   r.eventRoom_id, r.eventRoom_name, r.eventRoom_floor, 
                   v.venue_id, v.venue_name, v.venue_address
            FROM Events e
            JOIN eventRoom r ON e.eventRoom_id = r.eventRoom_id
            JOIN venue v ON r.venue_id = v.venue_id
            WHERE e.event_id = ?
        """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
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

                // Map EventRoom
                EventRoom eventRoom = new EventRoom();
                eventRoom.setEventRoomId(rs.getLong("eventRoom_id"));
                eventRoom.setEventRoomName(rs.getString("eventRoom_name"));
                eventRoom.setEventRoomFloor(rs.getString("eventRoom_floor"));

                // Map Venue
                Venue venue = new Venue();
                venue.setVenueId(rs.getLong("venue_id"));
                venue.setVenueName(rs.getString("venue_name"));
                venue.setVenueAddress(rs.getString("venue_address"));

                eventRoom.setVenue(venue);
                event.setEventRoom(eventRoom);

                return Optional.of(event);
            });
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Get all Events with EventRoom and Venue Details
    public List<Events> getAllEventsWithVenueDetails() {
        String sql = """
            SELECT e.event_id, e.title, e.event_creator, e.event_responsible, 
                   e.event_control, e.event_type, e.description, 
                   e.max_participants, e.max_audience, e.conguide_dk, 
                   e.conguide_en, 
                   r.eventRoom_id, r.eventRoom_name, r.eventRoom_floor, 
                   v.venue_id, v.venue_name, v.venue_address
            FROM Events e
            JOIN eventRoom r ON e.eventRoom_id = r.eventRoom_id
            JOIN venue v ON r.venue_id = v.venue_id
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
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

            // Map EventRoom
            EventRoom eventRoom = new EventRoom();
            eventRoom.setEventRoomId(rs.getLong("eventRoom_id"));
            eventRoom.setEventRoomName(rs.getString("eventRoom_name"));
            eventRoom.setEventRoomFloor(rs.getString("eventRoom_floor"));

            // Map Venue
            Venue venue = new Venue();
            venue.setVenueId(rs.getLong("venue_id"));
            venue.setVenueName(rs.getString("venue_name"));
            venue.setVenueAddress(rs.getString("venue_address"));

            eventRoom.setVenue(venue);
            event.setEventRoom(eventRoom);

            return event;
        });
    }

    // Get all Events
    public List<Events> findAll() {
        String sql = "SELECT * FROM events";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Events.class));
    }

    // Check if Event exists by ID
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM events WHERE event_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }

    // Delete Event by ID
    public int deleteById(Long id) {
        String sql = "DELETE FROM events WHERE event_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    // Update Event
    public int updateEvent(Events event) {
        String sql = """
            UPDATE events 
            SET title = ?, event_creator = ?, event_responsible = ?, 
                event_control = ?, event_type = ?, description = ?, 
                max_participants = ?, max_audience = ?, conguide_dk = ?, 
                conguide_en = ?, eventRoom_id = ? 
            WHERE event_id = ?
        """;
        return jdbcTemplate.update(sql, event.getTitle(), event.getEventCreator(),
                event.getEventResponsible(), event.getEventControl(), event.getEventType(),
                event.getDescription(), event.getMaxParticipants(), event.getMaxAudience(),
                event.getConguideDk(), event.getConguideEn(), event.getEventRoomId(), event.getEventId());
    }

    // Additional methods (Schedules, Expenses, Requirements)
    public int addEventSchedule(EventSchedule schedule) {
        String sql = "INSERT INTO EventSchedule (event_id, schedule_date, start_time, end_time) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, schedule.getEventId(), schedule.getScheduleDate(), schedule.getStartTime(), schedule.getEndTime());
    }

    public List<EventSchedule> findScheduleByEventId(Long eventId) {
        String sql = "SELECT * FROM EventSchedule WHERE event_id = ?";
        return jdbcTemplate.query(sql, new Object[]{eventId}, new BeanPropertyRowMapper<>(EventSchedule.class));
    }

    public int updateEventSchedule(EventSchedule schedule) {
        String sql = "UPDATE EventSchedule SET schedule_date = ?, start_time = ?, end_time = ? WHERE schedule_id = ?";
        return jdbcTemplate.update(sql, schedule.getScheduleDate(), schedule.getStartTime(), schedule.getEndTime(), schedule.getScheduleId());
    }

    public List<EventSchedule> getEventSchedules() {
        String sql = """
            SELECT es.schedule_id, es.event_id, es.schedule_date, 
                   es.start_time, es.end_time, e.title
            FROM EventSchedule es
            JOIN Events e ON es.event_id = e.event_id
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EventSchedule schedule = new EventSchedule();
            schedule.setScheduleId(rs.getLong("schedule_id"));
            schedule.setEventId(rs.getLong("event_id"));
            schedule.setScheduleDate(rs.getDate("schedule_date").toLocalDate());
            schedule.setStartTime(rs.getTime("start_time").toLocalTime());
            schedule.setEndTime(rs.getTime("end_time").toLocalTime());
            schedule.setTitle(rs.getString("title"));
            return schedule;
        });
    }
}
