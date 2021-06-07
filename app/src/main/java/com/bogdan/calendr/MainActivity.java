package com.bogdan.calendr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView eventListView;
    private ImageView addButton;
    private ProgressBar loading;
    private TextView noEvent;
    public static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Calendr);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        eventListView = findViewById(R.id.event_list);
        addButton = findViewById(R.id.add_button);
        loading = findViewById(R.id.loading);
        noEvent = findViewById(R.id.no_event_text);
        loading.setVisibility(View.VISIBLE);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").build();

        calendarView.setOnDayClickListener(this::onDayClick);
        addButton.setOnClickListener(v -> openAddEventActivity());

        Calendar c =  Calendar.getInstance();
        List<Calendar> list = new ArrayList<>();
        list.add(c);
        calendarView.setSelectedDates(list);

        db.eventDao().getAll().observeForever(events -> calendarView.setEvents(getEventDays(events)));

        db.eventDao().getAll().observe(this, this::showEventsInList);

    }

    private void onDayClick(EventDay eventDay) {
        db.eventDao().getEventsByDay(eventDay.getCalendar()).observe(this, this::showEventsInList);
    }

    private void openAddEventActivity() {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("INTENT_EVENT", new Event(0,"",calendarView.getFirstSelectedDate(),EventType.ONE_DAY,EventColor.BLUE));
        startActivity(intent);
    }

    private void showEventsInList(List<Event> eventList) {
        loading.setVisibility(View.GONE);
        if (eventList.size() == 0) {
            noEvent.setVisibility(View.VISIBLE);
        } else {
            noEvent.setVisibility(View.GONE);
        }
        eventListView.setAdapter(new EventAdapter(eventList, db));
    }

    private List<EventDay> getEventDays(List<Event> events) {
        List<EventDay> result = new ArrayList<>();
        for(Event event:events) {
            result.add(new EventDay(event.getDate(), eventColorToColor(event.getColor())));
        }
        return result;
    }

    public static int eventColorToColor(EventColor eventColor) {
        int[] colors = {R.color.red, R.color.orange, R.color.yellow, R.color.green, R.color.blue_300, R.color.purple};
        return colors[eventColor.ordinal()];
    }
}