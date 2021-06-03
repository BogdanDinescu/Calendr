package com.bogdan.calendr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final Calendar INTENT_DATE = null;
    private CalendarView calendarView;
    private RecyclerView eventListView;
    private EventManager eventManager;
    private ImageView addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        eventListView = findViewById(R.id.event_list);
        addButton = findViewById(R.id.add_button);
        eventManager = new EventManager();

        calendarView.setOnDayClickListener(this::onDayClick);
        addButton.setOnClickListener(v -> openAddEventActivity());

        Calendar c =  Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,1);
        eventManager.addEvent("Cina", c, c,EventType.ONE_DAY,EventColor.RED);
        calendarView.setEvents(eventManager.getEventDays());

        showEventsInList(eventManager.getEvents());

    }

    private void onDayClick(EventDay eventDay) {
        showEventsInList(eventManager.getEventsByDay(eventDay.getCalendar()));
    }

    private void openAddEventActivity() {
        Intent intent = new Intent(this, EditEvent.class);
        intent.putExtra("INTENT_DATE",calendarView.getFirstSelectedDate());
        startActivity(intent);
    }

    private void showEventsInList(List<Event> eventList) {
        eventListView.setAdapter(new EventAdapter(eventList, this));
    }

}