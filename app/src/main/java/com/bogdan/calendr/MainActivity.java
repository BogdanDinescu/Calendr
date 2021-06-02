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
        Date x = Calendar.getInstance().getTime();
        eventManager.addEvent("Cina",x,x,EventType.ONE_DAY,EventColor.BLUE);
        showEventsInList(eventManager.getEvents());

    }

    private void onDayClick(EventDay eventDay) {
        showEventsInList(eventManager.getEventsByDay(eventDay.getCalendar().getTime()));
    }

    private void showEventsInList(List<Event> eventList) {
        eventListView.setAdapter(new EventAdapter(eventList, this));
    }

}