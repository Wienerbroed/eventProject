package org.example.eventproject.repositories;

import org.example.eventproject.models.EventExpenses;
import org.example.eventproject.models.EventRequirements;
import org.example.eventproject.models.EventSchedule;
import org.example.eventproject.models.Events;
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
        String sql = "INSERT INTO Events (title, event_creator, event_responsible, event_control, event_type, description, max_participants, max_audience, conguide_dk, conguide_en) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, event.getTitle(), event.getEventCreator(), event.getEventResponsible(), event.getEventControl(), event.getEventType(), event.getDescription(), event.getMaxParticipants(), event.getMaxAudience(), event.getConguideDk(), event.getConguideEn());
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




}
