package org.example.eventproject.models;

import jakarta.persistence.*;

@Entity
@Table(name = "eventRoom")
public class EventRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventRoom_id")
    private Long eventRoomId;

    @Column(name = "eventRoom_name")
    private String eventRoomName;

    @Column(name = "eventRoom_floor")
    private String eventRoomFloor;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    public Long getEventRoomId() {
        return eventRoomId;
    }

    public String getEventRoomName() {
        return eventRoomName;
    }

    public String getEventRoomFloor() {
        return eventRoomFloor;
    }

    public void setEventRoomId(Long eventRoomId) {
        this.eventRoomId = eventRoomId;
    }

    public void setEventRoomName(String eventRoomName) {
        this.eventRoomName = eventRoomName;
    }

    public void setEventRoomFloor(String eventRoomFloor) {
        this.eventRoomFloor = eventRoomFloor;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}