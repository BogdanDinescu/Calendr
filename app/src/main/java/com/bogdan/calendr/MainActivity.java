package com.bogdan.calendr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
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
    public static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        eventListView = findViewById(R.id.event_list);
        addButton = findViewById(R.id.add_button);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").build();

        calendarView.setOnDayClickListener(this::onDayClick);
        addButton.setOnClickListener(v -> openAddEventActivity());

        Calendar c =  Calendar.getInstance();
        List<Calendar> list = new ArrayList<>();
        list.add(c);
        calendarView.setSelectedDates(list);

        db.eventDao().getAll().observeForever(events -> calendarView.setEvents(EventManager.getEventDays(events)));

        db.eventDao().getAll().observe(this, this::showEventsInList);

    }

    private void onDayClick(EventDay eventDay) {
        db.eventDao().getEventsByDay(eventDay.getCalendar()).observe(this, this::showEventsInList);
    }


    private void openAddEventActivity() {
        Intent intent = new Intent(this, EditEvent.class);
        intent.putExtra("INTENT_EVENT", new Event(0,"",calendarView.getFirstSelectedDate(),EventType.ONE_DAY,EventColor.BLUE));
        startActivity(intent);
    }

    private void showEventsInList(List<Event> eventList) {
        eventListView.setAdapter(new EventAdapter(eventList, db));
    }

}