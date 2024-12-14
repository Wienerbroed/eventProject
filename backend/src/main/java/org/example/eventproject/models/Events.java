package org.example.eventproject.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Events")
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "event_creator", nullable = false)
    private String eventCreator;

    @Column(name = "event_responsible", nullable = false)
    private String eventResponsible;

    @Column(name = "event_control", nullable = false)
    private String eventControl;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "max_participants", nullable = false)
    private int maxParticipants;

    @Column(name = "max_audience", nullable = false)
    private int maxAudience;

    @Column(name = "conguide_dk", nullable = false)
    private String conguideDk;

    @Column(name = "conguide_en", nullable = false)
    private String conguideEn;

    @Column(name = "event_room_id", nullable = false)
    private Long eventRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_room_id", referencedColumnName = "event_room_id", insertable = false, updatable = false)
    private EventRoom eventRoom;

    // Getters and setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventCreator() {
        return eventCreator;
    }

    public void setEventCreator(String eventCreator) {
        this.eventCreator = eventCreator;
    }

    public String getEventResponsible() {
        return eventResponsible;
    }

    public void setEventResponsible(String eventResponsible) {
        this.eventResponsible = eventResponsible;
    }

    public String getEventControl() {
        return eventControl;
    }

    public void setEventControl(String eventControl) {
        this.eventControl = eventControl;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getMaxAudience() {
        return maxAudience;
    }

    public void setMaxAudience(int maxAudience) {
        this.maxAudience = maxAudience;
    }

    public String getConguideDk() {
        return conguideDk;
    }

    public void setConguideDk(String conguideDk) {
        this.conguideDk = conguideDk;
    }

    public String getConguideEn() {
        return conguideEn;
    }

    public void setConguideEn(String conguideEn) {
        this.conguideEn = conguideEn;
    }

    public EventRoom getEventRoom() {
        return eventRoom;
    }

    public void setEventRoom(EventRoom eventRoom) {
        this.eventRoom = eventRoom;
    }

    public Long getEventRoomId() {
        return eventRoomId;
    }

    public void setEventRoomId(Long eventRoomId) {
        this.eventRoomId = eventRoomId;
    }
}