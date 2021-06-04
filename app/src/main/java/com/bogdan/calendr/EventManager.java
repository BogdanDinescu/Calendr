package com.bogdan.calendr;

import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventManager {
    private AppDatabase database;

    public EventManager(AppDatabase database) {
        this.database = database;
    }

    public void addEvent(String name, Calendar date, Calendar end, EventType type, EventColor color) {
        database.eventDao().insert(new Event(name, date, end, type, color));
    }

    public void addEvent(Event event) {
        database.eventDao().insert(event);
    }

    public List<Event> getEvents() {
        return database.eventDao().getAll();
    }

    public List<EventDay> getEventDays() {
        List<EventDay> result = new ArrayList<>();
        for(Event event:getEvents()) {
                result.add(new EventDay(event.getDate(), eventColorToColor(event.getColor())));
        }
        return result;
    }

    public List<Event> getEventsByDay(Calendar day) {
        List<Event> result = new ArrayList<>();

        for (Event event:getEvents()) {
            if (day.get(Calendar.YEAR) == event.getDate().get(Calendar.YEAR) &&
                    day.get(Calendar.MONTH) == event.getDate().get(Calendar.MONTH) &&
                    day.get(Calendar.DAY_OF_MONTH) == event.getDate().get(Calendar.DAY_OF_MONTH))
                result.add(event);
        }

        return result;
    }

    public int eventColorToColor(EventColor eventColor) {
        switch (eventColor) {
            case RED:
                return R.color.red;
            case BLUE:
                return R.color.blue_300;
            case GREEN:
                return R.color.green;
            case ORANGE:
                return R.color.orange;
            case PURPLE:
                return R.color.purple;
            case YELLOW:
                return R.color.yellow;
            default:
                return R.color.white;
        }
    }
}
