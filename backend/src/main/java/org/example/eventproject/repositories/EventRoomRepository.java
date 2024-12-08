package main.java.org.example.eventproject.repositories;

import main.java.org.example.eventproject.models.EventRoom;
import main.java.org.example.eventproject.models.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EventRoomRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventRoomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Add EventRoom
    public int addEventRoom(EventRoom eventRoom) {
        String sql = "INSERT INTO eventRoom (eventRoom_name, eventRoom_floor, venue_id) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, eventRoom.getEventRoomName(), eventRoom.getEventRoomFloor(), eventRoom.getVenueId());
    }

    // Find EventRoom by ID
    public Optional<EventRoom> findById(Long eventRoomId) {
        String sql = "SELECT * FROM eventRoom WHERE eventRoom_id = ?";
        try {
            EventRoom eventRoom = jdbcTemplate.queryForObject(sql, new Object[]{eventRoomId}, new BeanPropertyRowMapper<>(EventRoom.class));
            return Optional.ofNullable(eventRoom);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Get all EventRooms
    public List<EventRoom> findAllEventRooms() {
        String sql = "SELECT * FROM eventRoom";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EventRoom.class));
    }

    // Get EventRoom with Venue Details
    public List<EventRoom> getAllEventRoomsWithVenueDetails() {
        String sql = """
            SELECT r.eventRoom_id, r.eventRoom_name, r.eventRoom_floor, 
                   v.venue_id, v.venue_name, v.venue_address
            FROM eventRoom r
            JOIN venue v ON r.venue_id = v.venue_id
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EventRoom eventRoom = new EventRoom();
            eventRoom.setEventRoomId(rs.getLong("eventRoom_id"));
            eventRoom.setEventRoomName(rs.getString("eventRoom_name"));
            eventRoom.setEventRoomFloor(rs.getString("eventRoom_floor"));

            Venue venue = new Venue();
            venue.setVenueId(rs.getLong("venue_id"));
            venue.setVenueName(rs.getString("venue_name"));
            venue.setVenueAddress(rs.getString("venue_address"));

            eventRoom.setVenue(venue);
            return eventRoom;
        });
    }

    // Delete EventRoom by ID
    public int deleteById(Long eventRoomId) {
        String sql = "DELETE FROM eventRoom WHERE eventRoom_id = ?";
        return jdbcTemplate.update(sql, eventRoomId);
    }

    // Update EventRoom
    public int updateEventRoom(EventRoom eventRoom) {
        String sql = "UPDATE eventRoom SET eventRoom_name = ?, eventRoom_floor = ?, venue_id = ? WHERE eventRoom_id = ?";
        return jdbcTemplate.update(sql, eventRoom.getEventRoomName(), eventRoom.getEventRoomFloor(), eventRoom.getVenueId(), eventRoom.getEventRoomId());
    }
}