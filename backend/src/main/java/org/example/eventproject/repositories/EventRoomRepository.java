package org.example.eventproject.repositories;

import org.example.eventproject.models.EventRoom;
import org.example.eventproject.models.Venue;
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

    public int addEventRoom(EventRoom eventRoom) {
        String sql = "INSERT INTO EventRoom (eventRoomName, eventRoomCapacity, venue_id) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, eventRoom.getEventRoomName(), eventRoom.getEventRoomCapacity(), eventRoom.getVenueId());
    }

    //Find event room by id
    public Optional<EventRoom> findEventRoomById(Long id) {
        String sql = "SELECT * FROM EventRoom WHERE eventRoom_id = ?";
        try{
            EventRoom eventRoom = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(EventRoom.class));
            return Optional.ofNullable(eventRoom);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // Return Optional.empty if no event is found
        }
    }

    //Get all event rooms
    public List<EventRoom> findAllEventRooms() {
        String sql = "SELECT * FROM EventRoom";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EventRoom.class));
    }

    public List<EventRoom> getAllEventRoomsWithVenueDetails() {
        String sql = """
                SELECT e.eventRoom_Id, e.eventRoomName, e.eventRoomCapacity, v.venue_id, v.venue_name, v.venue_address
                FROM EventRoom e
                JOIN venue v ON e.venue_id = v.venue_id""";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EventRoom eventRoom = new EventRoom();
            eventRoom.setEventRoomId(rs.getLong("eventRoom_id"));
            eventRoom.setEventRoomName(rs.getString("eventRoom_name"));
            eventRoom.setEventRoomCapacity(rs.getInt("eventRoom_capacity"));

            // Create a Venue instance and set it to the Event
            Venue venue = new Venue();
            venue.setVenueId(rs.getLong("venue_id"));
            venue.setVenueName(rs.getString("venue_name"));
            venue.setVenueAddress(rs.getString("venue_address"));

            // Set the venue in the event object
            eventRoom.setVenue(venue);
            return eventRoom;
        });
    }

    //Delete event room by id
    public int deleteEventRoomById(Long id) {
        String sql = "DELETE FROM EventRoom WHERE event_room_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    //Check if event room exists
    public boolean eventRoomExists(Long eventRoomId) {
        String sql = "SELECT COUNT(*) FROM EventRoom WHERE eventRoom_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{eventRoomId}, Integer.class);
        return count != null && count > 0;
    }

    //Update event room
    public int updateEventRoom(EventRoom eventRoom) {
        String sql = "UPDATE EventRoom SET eventRoom_name = ?, eventRoom_capacity = ?, venue_id = ? WHERE eventRoom_id = ?";
        return jdbcTemplate.update(sql, eventRoom.getEventRoomName(), eventRoom.getEventRoomCapacity(), eventRoom.getVenueId(), eventRoom.getEventRoomId());
    }

    public List<EventRoom> getEventRoomByVenueId(Long venueId) {
        String sql = """
                SELECT e.eventRoom_id, e.eventRoom_name, e.eventRoomCapacity, e.venue_id
                FROM EventRoom e
                WHERE e.venue_id = ?""";
        return jdbcTemplate.query(sql, new Object[]{venueId}, (rs, rowNum) -> {
            EventRoom eventRoom = new EventRoom();
            eventRoom.setEventRoomId(rs.getLong("eventRoom_id"));
            eventRoom.setEventRoomName(rs.getString("eventRoom_name"));
            eventRoom.setEventRoomCapacity(rs.getInt("eventRoom_capacity"));
            eventRoom.setVenueId(rs.getLong("venue_id"));
            return eventRoom;
        });
    }


}
