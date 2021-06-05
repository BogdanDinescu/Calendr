package com.bogdan.calendr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static final Calendar INTENT_DATE = null;
    private CalendarView calendarView;
    private RecyclerView eventListView;
    private ImageView addButton;
    private ActivityResultLauncher<Intent> startForResult;
    private AppDatabase db;

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
        startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                MainActivity.this.onActivityResult(result);
            }
        });

        Calendar c =  Calendar.getInstance();
        List<Calendar> list = new ArrayList<>();
        list.add(c);
        calendarView.setSelectedDates(list);

        db.eventDao().getAll().observeForever(events -> calendarView.setEvents(EventManager.getEventDays(events)));

        db.eventDao().getAll().observe(this, events -> showEventsInList(events));

    }

    private void onDayClick(EventDay eventDay) {
        db.eventDao().getEventsByDay(eventDay.getCalendar()).observe(this, events -> {
            Toast.makeText(getApplication(),String.valueOf(events.size()),Toast.LENGTH_SHORT).show();
            showEventsInList(events);
        });
    }

    private void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                Event event = result.getData().getParcelableExtra("INTENT_RESPONSE");
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.eventDao().insert(event);
                    }
                });
                Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openAddEventActivity() {
        Intent intent = new Intent(this, EditEvent.class);
        intent.putExtra("INTENT_DATE",calendarView.getFirstSelectedDate());
        startForResult.launch(intent);
    }

    private void showEventsInList(List<Event> eventList) {
        eventListView.setAdapter(new EventAdapter(eventList));
    }

}