package com.onedroid.relive.model;


import java.util.ArrayList;
import java.util.UUID;

/**
 * Data model for Event.
 */
public class Event {
    private final UUID eventId;
    private final String name;
    private final String fromDate;
    private final String toDate;
    private  final ArrayList<String> attendees;

    /**
     * Constructor to initialize City object.
     * @param eventId
     * @param name Name of Event.
     * @param fromDate From Date of the event.
     * @param toDate To Date of the event.
     */
    public Event(UUID eventId, final String name, final String fromDate, final String toDate, final ArrayList<String> attendees) {
        this.eventId = eventId;
        this.name = name;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.attendees = attendees;
    }

    public String getName() {
        return name;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public ArrayList<String> getAttendees() {
        return attendees;
    }

    public void  addAttendee(String username) {
        attendees.add(username);
    }

    public UUID getEventId() {
        return eventId;
    }
}
