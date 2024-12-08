package org.example.eventproject.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "EventSchedule")
public class EventSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;


    @JoinColumn(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "schedule_date")
    private LocalDate scheduleDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Transient
    private String title; // Transient field to hold title (not mapped to DB)

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    // Getters and setters
    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }



    @Override
    public String toString() {
        return "EventSchedule{" +
                "eventId=" + eventId +
                ", scheduleDate=" + scheduleDate +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}