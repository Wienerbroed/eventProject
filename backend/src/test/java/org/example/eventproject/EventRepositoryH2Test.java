package org.example.eventproject;

import org.example.eventproject.models.Events;
import org.example.eventproject.repositories.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("h2")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:h2init.sql")
public class EventRepositoryH2Test {
//a
    @Autowired
    private EventRepository repository;

    @Test
    public void addEvent() {
        Events newEvent = new Events();

        newEvent.setTitle("testTitle");
        newEvent.setEventCreator("testCreator");
        newEvent.setEventResponsible("testResponsible");
        newEvent.setEventControl("testControl");
        newEvent.setEventType("testType");
        newEvent.setDescription("testDescription");
        newEvent.setMaxParticipants(100);
        newEvent.setMaxAudience(500);
        newEvent.setConguideDk("testConguideDk");
        newEvent.setConguideEn("testConguideEn");
        newEvent.setVenueId(2L);  // Assuming venue_id is a Long type
        newEvent.setWarnings("testWarnings");
        newEvent.setEventRoomId(2L);  // Assuming event_room_id is a Long type

        repository.addEvent(newEvent);

        Optional<Events> retrievedEventOptional = repository.findById(2L);
        Events retrievedEvent = retrievedEventOptional.orElseThrow(() -> new RuntimeException("Event not found"));
        assertEquals("testTitle", retrievedEvent.getTitle());
    }
}