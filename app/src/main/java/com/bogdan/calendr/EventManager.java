package com.bogdan.calendr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventManager {
    private List<Event> events;

    public EventManager() {
        this.events = new ArrayList<>();
    }

    public void addEvent(String name, Date date, Date end, EventType type, EventColor color) {
        events.add(new Event(name, date, end, type, color));
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Event> getEventsByDay(Date day) {
        List<Event> result = new ArrayList<>();
        Calendar curDay = Calendar.getInstance();
        Calendar evDay = Calendar.getInstance();
        curDay.setTime(day);

        for (Event event:events) {
            evDay.setTime(event.getDate());
            if (evDay.get(Calendar.YEAR) == curDay.get(Calendar.YEAR) &&
                    evDay.get(Calendar.MONTH) == curDay.get(Calendar.MONTH) &&
                    evDay.get(Calendar.DAY_OF_MONTH) == curDay.get(Calendar.DAY_OF_MONTH))
                result.add(event);
        }

        return result;
    }
}
