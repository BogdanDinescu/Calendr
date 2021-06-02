package com.bogdan.calendr;

import java.util.Date;

public class Event {
    private String name;
    private Date date;
    private Date end;
    private EventType type;
    private EventColor color;

    public Event(String name, Date date, Date end, EventType type, EventColor color) {
        this.name = name;
        this.date = date;
        this.end = end;
        this.type = type;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public EventColor getColor() {
        return color;
    }

    public void setColor(EventColor color) {
        this.color = color;
    }
}
