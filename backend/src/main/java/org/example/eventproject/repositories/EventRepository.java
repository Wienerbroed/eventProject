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
        String sql = "INSERT INTO Events (title, event_creator, event_responsible, event_control, event_type, description, max_participants, max_audience, conguide_dk, conguide_en, warnings, event_room_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, event.getTitle(), event.getEventCreator(), event.getEventResponsible(), event.getEventControl(), event.getEventType(), event.getDescription(), event.getMaxParticipants(), event.getMaxAudience(), event.getConguideDk(), event.getConguideEn(), event.getWarnings(), event.getEventRoomId());
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

    public List<Events> getAllEventsWithEventRoomDetails() {
        String sql = """
    SELECT e.event_id, e.title, e.event_creator, e.event_responsible, 
           e.event_control, e.event_type, e.description, 
           e.max_participants, e.max_audience, e.conguide_dk, 
           e.conguide_en, e.warnings, er.event_room_id, er.event_room_name, er.event_room_capacity
    FROM Events e
    JOIN EventRoom er ON e.event_room_id = er.event_room_id
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
            event.setWarnings(rs.getString("warnings"));

            // Create an EventRoom instance and set it to the Event
            EventRoom eventRoom = new EventRoom();
            eventRoom.setEventRoomId(rs.getLong("event_room_id"));
            eventRoom.setEventRoomName(rs.getString("event_room_name"));
            eventRoom.setEventRoomCapacity(rs.getInt("event_room_capacity"));


            // Set the eventRoom in the event object
            event.setEventRoom(eventRoom);

            return event;
        });
    }

    public int deleteScheduleById(Long scheduleId) {
        String sql = "DELETE FROM EventSchedule WHERE schedule_id = ?";
        return jdbcTemplate.update(sql, scheduleId);
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
        return jdbcTemplate.update(sql, schedule.getEventId(), schedule.getScheduleDate(), schedule.getStartTime(), schedule.getEndTime());
    }

    public List<EventSchedule> findScheduleByEventId(Long eventId) {
        String sql = "SELECT * FROM EventSchedule WHERE event_id = ?";
        return jdbcTemplate.query(sql, new Object[]{eventId}, new BeanPropertyRowMapper<>(EventSchedule.class));
    }

    // Update Event
    public int updateEvent(Events event) {
        String sql = "UPDATE Events SET title = ?, event_creator = ?, event_responsible = ?, event_control = ?, event_type = ?, description = ?, max_participants = ?, max_audience = ?, conguide_dk = ?, conguide_en = ?, warnings= ?, event_room_id = ? WHERE event_id = ?";
        return jdbcTemplate.update(sql, event.getTitle(), event.getEventCreator(), event.getEventResponsible(), event.getEventControl(), event.getEventType(), event.getDescription(), event.getMaxParticipants(), event.getMaxAudience(), event.getConguideDk(), event.getConguideEn(), event.getWarnings(), event.getEventRoomId(), event.getEventId());
    }

    public List<Events> getEventsByEventRoomId(Long eventRoomId) {
        String sql = """
            SELECT e.event_id, e.title, e.event_creator, e.event_responsible, 
                   e.event_control, e.event_type, e.description, 
                   e.max_participants, e.max_audience, e.conguide_dk, 
                   e.conguide_en, e.warnings , e.event_room_id
            FROM Events e
            WHERE e.event_room_id = ?
        """;

        return jdbcTemplate.query(sql, new Object[]{eventRoomId}, (rs, rowNum) -> {
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
            event.setWarnings(rs.getString("warnings"));
            event.setEventRoomId(rs.getLong("event_room_id"));

            return event;
        });
    }


    public int updateEventSchedule(EventSchedule schedule) {
        String sql = "UPDATE EventSchedule SET schedule_date = ?, start_time = ?, end_time = ? WHERE schedule_id = ?";
        return jdbcTemplate.update(sql, schedule.getScheduleDate(), schedule.getStartTime(), schedule.getEndTime(), schedule.getScheduleId());
    }

    public Optional<EventSchedule> findScheduleById(Long scheduleId) {
        String sql = "SELECT * FROM EventSchedule WHERE schedule_id = ?";
        try {
            EventSchedule schedule = jdbcTemplate.queryForObject(sql, new Object[]{scheduleId}, new BeanPropertyRowMapper<>(EventSchedule.class));
            return Optional.ofNullable(schedule);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // Return Optional.empty if no schedule is found
        }
    }


    public List<EventSchedule> getEventSchedules() {
        String sql = """
    SELECT es.schedule_id, es.event_id, es.schedule_date, es.start_time, es.end_time, e.title
    FROM EventSchedule es
    JOIN Events e ON es.event_id = e.event_id
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EventSchedule schedule = new EventSchedule();
            schedule.setScheduleId(rs.getLong("schedule_id"));
            schedule.setEventId(rs.getLong("event_id"));
            schedule.setScheduleDate(rs.getDate("schedule_date").toLocalDate()); // Convert String to LocalDate
            schedule.setStartTime(rs.getTime("start_time").toLocalTime()); // Convert String to LocalTime
            schedule.setEndTime(rs.getTime("end_time").toLocalTime()); // Convert String to LocalTime
            schedule.setTitle(rs.getString("title")); // Map title from the Event table
            return schedule;
        });
    }

    // Get expenses by event ID
    public List<EventExpenses> findExpensesByEventId(Long eventId) {
        String sql = "SELECT * FROM EventExpenses WHERE event_id = ?";
        return jdbcTemplate.query(sql, new Object[]{eventId}, new BeanPropertyRowMapper<>(EventExpenses.class));
    }

    public List<EventRequirements> findRequirementsByEventId(Long eventId) {
        String sql = """
        SELECT er.requirement_id, er.event_id, er.praktiske_krav, er.tekniske_krav, er.materialebehov, er.gopherbehov,
               e.event_id AS e_event_id, e.title, e.event_creator, e.event_responsible, e.event_control, e.event_type, e.description, e.max_participants, e.max_audience, e.conguide_dk, e.conguide_en, e.warnings, e.venue_id
        FROM EventRequirements er
        JOIN Events e ON er.event_id = e.event_id
        WHERE er.event_id = ?
    """;

        return jdbcTemplate.query(sql, new Object[]{eventId}, (rs, rowNum) -> {
            EventRequirements requirement = new EventRequirements();
            requirement.setRequirementId(rs.getLong("requirement_id"));
            requirement.setEventId(rs.getLong("event_id"));
            requirement.setPraktiskeKrav(rs.getString("praktiske_krav"));
            requirement.setTekniskeKrav(rs.getString("tekniske_krav"));
            requirement.setMaterialebehov(rs.getString("materialebehov"));
            requirement.setGopherbehov(rs.getString("gopherbehov"));

            Events event = new Events();
            event.setEventId(rs.getLong("e_event_id"));
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
            event.setWarnings(rs.getString("warnings"));
            event.setVenueId(rs.getLong("venue_id"));

            requirement.setEvent(event);
            return requirement;
        });
    }

    // Delete requirement by event ID
    public int deleteRequirementByEventId(Long eventId) {
        String sql = "DELETE FROM EventRequirements WHERE event_id = ?";
        return jdbcTemplate.update(sql, eventId);
    }

    // Delete expense by ID
    public int deleteExpenseById(Long expenseId) {
        String sql = "DELETE FROM EventExpenses WHERE expense_id = ?";
        return jdbcTemplate.update(sql, expenseId);
    }



}
