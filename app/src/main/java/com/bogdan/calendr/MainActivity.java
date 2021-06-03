package com.bogdan.calendr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final Calendar INTENT_DATE = null;
    private CalendarView calendarView;
    private RecyclerView eventListView;
    private EventManager eventManager;
    private ImageView addButton;
    private ActivityResultLauncher<Intent> startForResult;

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
        startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                MainActivity.this.onActivityResult(result);
            }
        });

        Calendar c =  Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,1);
        eventManager.addEvent("Cina", c, c,EventType.ONE_DAY,EventColor.RED);
        displayEventsOnCalendarView();

        showEventsInList(eventManager.getEvents());

    }

    private void onDayClick(EventDay eventDay) {
        showEventsInList(eventManager.getEventsByDay(eventDay.getCalendar()));
    }

    private void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                Event event = result.getData().getParcelableExtra("INTENT_RESPONSE");
                eventManager.addEvent(event);
                displayEventsOnCalendarView();
                Toast.makeText(getApplicationContext(), "Added successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayEventsOnCalendarView() {
        calendarView.setEvents(eventManager.getEventDays());
    }

    private void openAddEventActivity() {
        Intent intent = new Intent(this, EditEvent.class);
        intent.putExtra("INTENT_DATE",calendarView.getFirstSelectedDate());
        startForResult.launch(intent);
    }

    private void showEventsInList(List<Event> eventList) {
        eventListView.setAdapter(new EventAdapter(eventList, this));
    }

}