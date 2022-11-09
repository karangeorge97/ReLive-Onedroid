package com.onedroid.relive.model;


/**
 * Data model for Event.
 */
public class Event {
    private final String name;
    private final String fromDate;
    private final String toDate;

    /**
     * Constructor to initialize City object.
     * @param name Name of Event.
     * @param fromDate From Date of the event.
     * @param toDate To Date of the event.
     */
    public Event(final String name, final String fromDate, final String toDate) {
        this.name = name;
        this.fromDate = fromDate;
        this.toDate = toDate;
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
}
