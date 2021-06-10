package com.bogdan.calendr;

import android.app.*;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.lang.Integer.parseInt;

public class EditEventActivity extends Activity {

    private EditText textName;
    private Button btn_cancel;
    private Button btn_ok;
    private Button btn_date;
    private Button btn_time;
    private EditText date;
    private EditText time;
    private RadioGroup colorSelector;
    private RadioGroup typeSelector;
    private Event eventFromIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        textName = findViewById(R.id.event_name);
        btn_cancel = findViewById(R.id.cancel_button);
        btn_ok = findViewById(R.id.ok_button);
        btn_date = findViewById(R.id.btn_date);
        btn_time = findViewById(R.id.btn_time);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        colorSelector = findViewById(R.id.color_selector);
        typeSelector = findViewById(R.id.type_selector);

        btn_date.setOnClickListener(v -> datePickerDialog(date));
        btn_time.setOnClickListener(v -> timePickerDialog(time));
        btn_cancel.setOnClickListener(v -> finish());
        btn_ok.setOnClickListener(v -> finishAndInsert());
        typeSelector.setOnCheckedChangeListener(this::typeChange);

        Intent intent = getIntent();
        eventFromIntent = (Event) intent.getExtras().get("INTENT_EVENT");
        date.setText(getString(R.string.date_format,eventFromIntent.getDate().get(Calendar.DATE),eventFromIntent.getDate().get(Calendar.MONTH) + 1,eventFromIntent.getDate().get(Calendar.YEAR)));
        time.setText(getString(R.string.time_format,eventFromIntent.getDate().get(Calendar.HOUR_OF_DAY),eventFromIntent.getDate().get(Calendar.MINUTE)));
        textName.setText(eventFromIntent.getName());
        colorSelector.check(colorSelector.getChildAt(eventFromIntent.getColor().ordinal()).getId());
        typeSelector.check(typeSelector.getChildAt(eventFromIntent.getType().ordinal()).getId());
    }

    private void datePickerDialog(EditText editText) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, day) -> editText.setText(getString(R.string.date_format , day, month + 1, year)),
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePickerDialog(EditText editText) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog datePickerDialog = new TimePickerDialog(this,
                (view, hour, minute) -> editText.setText(getString(R.string.time_format, hour, minute)),
                mHour, mMinute, true);
        datePickerDialog.show();
    }

    private Calendar getCalendarFromString() throws RuntimeException {
        String string_date = this.date.getText().toString();
        if (string_date.length() == 0) throw new RuntimeException();

        String[] date = string_date.split("/");
        if (date.length != 3) throw new RuntimeException();

        String[] time;
        if (RadioGroupToCheckedPosition(typeSelector) == 1) {
            String string_time = this.time.getText().toString();
            if (string_time.length() == 0) throw new RuntimeException();
            time = string_time.split(":");
            if (time.length != 2) throw new RuntimeException();
        } else {
            time = new String[] {"0","0"};
        }

        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(
                parseInt(date[2].trim()),
                parseInt(date[1].trim()) - 1,
                parseInt(date[0].trim()),
                parseInt(time[0].trim()),
                parseInt(time[1].trim())
        );
        return c;
    }

    private Event eventFromForm() throws RuntimeException {
        if(textName.getText().toString().length() == 0) throw new RuntimeException();

        Calendar c = getCalendarFromString();

        return new Event(
                eventFromIntent.getUid(),
                textName.getText().toString(),
                c,
                EventType.values()[RadioGroupToCheckedPosition(typeSelector)],
                EventColor.values()[RadioGroupToCheckedPosition(colorSelector)]);
    }

    private int RadioGroupToCheckedPosition(RadioGroup radioGroup) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioId);
        return radioGroup.indexOfChild(radioButton);
    }

    private void typeChange(RadioGroup group, int checkedId) {
        timeShow(checkedId == R.id.time_radio);
    }

    private void timeShow(boolean show) {
        if (show) {
            time.setVisibility(View.VISIBLE);
            btn_time.setVisibility(View.VISIBLE);
        } else {
            time.setVisibility(View.GONE);
            btn_time.setVisibility(View.GONE);
        }
    }

    private void finishAndInsert() {
        try {
            Event event = eventFromForm();
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    MainActivity.db.eventDao().insert(event);
                    if (event.getType() == EventType.REMINDER)
                        startAlert(event);
                }
            });
            finish();
        } catch (RuntimeException e) {
            Toast.makeText(this,"Fields not completed correctly",Toast.LENGTH_SHORT).show();
        }
    }

    private void startAlert(Event event) {
        Intent intent = new Intent(getApplicationContext(), Broadcast.class);
        intent.putExtra("notification_title", event.getName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), event.getUid(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, event.getDate().getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, event.getDate().getTimeInMillis(), pendingIntent);
        }
    }
}
