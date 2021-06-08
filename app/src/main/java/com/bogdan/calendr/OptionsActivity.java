package com.bogdan.calendr;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OptionsActivity extends AppCompatActivity {

    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        deleteButton = findViewById(R.id.btn_delete_past);

        deleteButton.setOnClickListener(this::deletePastEvents);
    }

    private void deletePastEvents(View v) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                MainActivity.db.eventDao().deleteEventsBefore(c);
                finish();
            }
        });
    }
}