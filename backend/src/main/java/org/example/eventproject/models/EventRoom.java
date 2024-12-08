package main.java.org.example.eventproject.models;


import jakarta.persistence.*;

@Entity
@Table(name = "eventRoom")
public class EventRoom {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventRoomId", nullable = false)
    private Long eventRoomId;

    @Column(name = "eventRoom_name", nullable = false)
    private String eventRoomName;

    @Column(name = "eventRoom_floor", nullable = false)
    private String eventRoomFloor;

    @Column(name = "venue_id", nullable = false)
    private Long venueId;  // This is the foreign key field

    // Use @ManyToOne to define the relationship with the Venue entity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_Id", referencedColumnName = "venue_Id", insertable = false, updatable = false)
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

    public void setEventRoomId(Long venueId) {
        this.eventRoomId = eventRoomId;
    }

    public void setEventRoomName(String eventRoomName) {
        this.eventRoomName = eventRoomName;
    }

    public void getEventRoomFloor(String eventRoomFloor) {
        this.eventRoomFloor = eventRoomFloor;
    }

    // Getter and setter for venue relationship
    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }



}
