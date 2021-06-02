package com.bogdan.calendr;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.applandeo.materialcalendarview.CalendarView;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
    }

}