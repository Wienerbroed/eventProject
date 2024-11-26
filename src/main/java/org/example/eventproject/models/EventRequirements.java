package org.example.eventproject.models;

import jakarta.persistence.*;

@Entity
@Table(name = "EventRequirements")
public class EventRequirements {
    @Id
    @Column(name = "event_id")
    private Long eventId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "event_id")
    private Events event;

    @Column(name = "praktiske_krav")
    private String praktiskeKrav;

    @Column(name = "tekniske_krav")
    private String tekniskeKrav;

    @Column(name = "materialebehov")
    private String materialebehov;

    @Column(name = "gopherbehov")
    private String gopherbehov;

    // Getters and setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Events getEvent() {
        return event;
    }

    public void setEvent(Events event) {
        this.event = event;
    }

    public String getPraktiskeKrav() {
        return praktiskeKrav;
    }

    public void setPraktiskeKrav(String praktiskeKrav) {
        this.praktiskeKrav = praktiskeKrav;
    }

    public String getTekniskeKrav() {
        return tekniskeKrav;
    }

    public void setTekniskeKrav(String tekniskeKrav) {
        this.tekniskeKrav = tekniskeKrav;
    }

    public String getMaterialebehov() {
        return materialebehov;
    }

    public void setMaterialebehov(String materialebehov) {
        this.materialebehov = materialebehov;
    }

    public String getGopherbehov() {
        return gopherbehov;
    }

    public void setGopherbehov(String gopherbehov) {
        this.gopherbehov = gopherbehov;
    }
}