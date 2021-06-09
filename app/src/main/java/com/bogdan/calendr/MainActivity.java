package com.bogdan.calendr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView eventListView;
    private Button addButton;
    private Button showAllButton;
    private Button optionsButton;
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
        showAllButton = findViewById(R.id.btn_show_all);
        optionsButton = findViewById(R.id.btn_options);
        loading = findViewById(R.id.loading);
        noEvent = findViewById(R.id.no_event_text);
        loading.setVisibility(View.VISIBLE);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.name).setJournalMode(RoomDatabase.JournalMode.TRUNCATE).build();

        calendarView.setOnDayClickListener(this::onDayClick);
        addButton.setOnClickListener(v -> openAddEventActivity());
        showAllButton.setOnClickListener(v -> showAllEvents());
        optionsButton.setOnClickListener(v -> openOptionsActivity());
        setTodayDateAsSelected();

        db.eventDao().getAll().observeForever(events -> calendarView.setEvents(getEventDays(events)));
        showAllEvents();
    }

    private void onDayClick(EventDay eventDay) {
        Calendar daySelected = (Calendar) eventDay.getCalendar().clone();
        eventDay.getCalendar().add(Calendar.DATE,1);
        Calendar nextDay = eventDay.getCalendar();
        db.eventDao().getEventsByRange(daySelected, nextDay).observe(this, this::showEventsInList);
    }

    private void showAllEvents() {
        db.eventDao().getAll().observe(this, this::showEventsInList);
    }

    private void openAddEventActivity() {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("INTENT_EVENT", new Event(0,"",calendarView.getFirstSelectedDate(),EventType.ONE_DAY,EventColor.BLUE));
        startActivity(intent);
    }

    private void openOptionsActivity() {
        Intent intent = new Intent(this, OptionsActivity.class);
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

    private void setTodayDateAsSelected() {
        Calendar c =  Calendar.getInstance();
        List<Calendar> list = new ArrayList<>();
        list.add(c);
        calendarView.setSelectedDates(list);
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