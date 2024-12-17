package org.example.eventproject.models;

import jakarta.persistence.*;

@Entity
@Table(name = "EventRoom")
public class EventRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_room_id")
    private long eventRoomId;

    @Column(name = "event_room_name")
    private String eventRoomName;

    @Column(name = "event_room_capacity")
    private int eventRoomCapacity;

    @Column(name = "venue_id")
    private long venueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", referencedColumnName = "venue_id", insertable = false, updatable = false)
    private Venue venue;

    //Getters
    public Long getEventRoomId() {
        return eventRoomId;
    }

    public String getEventRoomName() {
        return eventRoomName;
    }

    public int getEventRoomCapacity() {
        return eventRoomCapacity;
    }

    public long getVenueId() {
        return venueId;
    }

    public Venue getVenue() {
        return venue;
    }

    //Setters
    public void setEventRoomId(long eventRoomId) {
        this.eventRoomId = eventRoomId;
    }

    public void setEventRoomName(String eventRoomName) {
        this.eventRoomName = eventRoomName;
    }

    public void setEventRoomCapacity(int eventRoomCapacity) {
        this.eventRoomCapacity = eventRoomCapacity;
    }

    public void setVenueId(long venueId) {
        this.venueId = venueId;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}