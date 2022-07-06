package com.bogdan.calendr;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView eventListView;
    private FloatingActionButton addButton;
    private ProgressBar loading;
    private TextView noEvent;
    public static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Calendr);
        getSupportActionBar().setElevation(0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        eventListView = findViewById(R.id.event_list);
        addButton = findViewById(R.id.add_button);
        loading = findViewById(R.id.loading);
        noEvent = findViewById(R.id.no_event_text);
        loading.setVisibility(View.VISIBLE);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.name).setJournalMode(RoomDatabase.JournalMode.TRUNCATE).build();
        createNotificationChannel();

        calendarView.setOnDayClickListener(this::onDayClick);
        addButton.setOnClickListener(v -> openAddEventActivity());
        setTodayDateAsSelected();

        db.eventDao().getAll().observeForever(events -> calendarView.setEvents(getEventDays(events)));
        showAgenda();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_menu, menu);
        return true;
    }

    private void onDayClick(EventDay eventDay) {
        Calendar daySelected = (Calendar) eventDay.getCalendar().clone();
        Calendar nextDay = (Calendar) eventDay.getCalendar().clone();
        nextDay.add(Calendar.DATE,1);
        db.eventDao().getEventsByRangeOrType(daySelected, nextDay, EventType.BIRTHDAY, previousYears(daySelected)).observe(this, this::showEventsInList);
    }

    private List<Calendar> previousYears(Calendar c) {
        int year = c.get(Calendar.YEAR);
        List<Calendar> result = new ArrayList<>();
        for (int i = year - 1; i > year - 100; i--) {
            Calendar copy = (Calendar) c.clone();
            copy.set(Calendar.YEAR, i);
            result.add(copy);
        }
        return result;
    }

    private void showAllEvents() {
        db.eventDao().getAll().observe(this, this::showEventsInList);
    }

    private void showAgenda() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        db.eventDao().getAllEventsAfter(c).observe(this, this::showEventsInList);
    }

    private void openAddEventActivity() {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("INTENT_EVENT", new Event(0,"",calendarView.getFirstSelectedDate(),EventType.ONE_DAY,EventColor.BLUE, false));
        startActivity(intent);
    }

    private void openOptionsActivity() {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getTitle().toString()) {
            case "Agenda":
                showAgenda();
                break;
            case "Show all":
                showAllEvents();
                break;
            case "Options":
                openOptionsActivity();
                break;
        }
        return true;
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
            if (event.getType() == EventType.BIRTHDAY) {
                event.getDate().set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            }
            result.add(new EventDay(event.getDate(), eventColorToColor(event.getColor())));
        }
        return result;
    }

    public static int eventColorToColor(EventColor eventColor) {
        int[] colors = {R.color.red, R.color.orange, R.color.yellow, R.color.green, R.color.blue_300, R.color.purple};
        return colors[eventColor.ordinal()];
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    "Reminder",
                    getString(R.string.reminder),
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(getString(R.string.reminder_text));

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
