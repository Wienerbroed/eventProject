package org.example.eventproject.models;


import jakarta.persistence.*;

@Entity
@Table(name = "venue")
public class Venue {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venue_id", nullable = false)
    private Long venueId;

    @Column(name = "venue_name", nullable = false)
    private String venueName;

    @Column(name = "venue_address", nullable = false)
    private String venueAddress;


    public Long getVenueId() {
        return venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }



}
