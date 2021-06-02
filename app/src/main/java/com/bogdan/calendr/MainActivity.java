package com.bogdan.calendr;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView eventListView;
    private EventAdapter eventAdapter;
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
        eventManager.addEvent("Cina",x,x,EventType.ONE_DAY,EventColor.BLUE);
        eventManager.addEvent("Cina",x,x,EventType.ONE_DAY,EventColor.BLUE);
        eventManager.addEvent("Cina",x,x,EventType.ONE_DAY,EventColor.BLUE);
        eventManager.addEvent("Cina",x,x,EventType.ONE_DAY,EventColor.BLUE);
        eventAdapter = new EventAdapter(eventManager.getEvents(), this);
        eventListView.setAdapter(eventAdapter);
    }

    private void onDayClick(EventDay eventDay) {
        Toast.makeText(this,eventDay.getCalendar().getTime().toString(),Toast.LENGTH_SHORT).show();
    }

}