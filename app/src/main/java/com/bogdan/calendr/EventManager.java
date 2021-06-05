package com.bogdan.calendr;

import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    public static List<EventDay> getEventDays(List<Event> events) {
        List<EventDay> result = new ArrayList<>();
        for(Event event:events) {
                result.add(new EventDay(event.getDate(), eventColorToColor(event.getColor())));
        }
        return result;
    }

    public static int eventColorToColor(EventColor eventColor) {
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
