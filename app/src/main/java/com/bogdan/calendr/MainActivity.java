package com.bogdan.calendr;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView eventListView;
    private EventManager eventManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        eventListView = findViewById(R.id.event_list);
        eventManager = new EventManager();

        calendarView.setOnDayClickListener(this::onDayClick);

        Calendar c =  Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,1);
        eventManager.addEvent("Cina", c, c,EventType.ONE_DAY,EventColor.BLUE);
        calendarView.setEvents(eventManager.getEventDays());

        showEventsInList(eventManager.getEvents());
    }

    private void onDayClick(EventDay eventDay) {
        showEventsInList(eventManager.getEventsByDay(eventDay.getCalendar()));
    }

    private void showEventsInList(List<Event> eventList) {
        eventListView.setAdapter(new EventAdapter(eventList, this));
    }

}