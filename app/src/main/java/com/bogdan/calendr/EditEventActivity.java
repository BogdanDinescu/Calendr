package com.bogdan.calendr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
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
    private EditText date;
    private RadioGroup color_selector;
    private Event eventFromIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        textName = findViewById(R.id.event_name);
        btn_cancel = findViewById(R.id.cancel_button);
        btn_ok = findViewById(R.id.ok_button);
        btn_date = findViewById(R.id.btn_date);
        date = findViewById(R.id.date);
        color_selector = findViewById(R.id.color_selector);

        btn_date.setOnClickListener(v -> datePickerDialog(date));
        btn_cancel.setOnClickListener(v -> finish());
        btn_ok.setOnClickListener(v -> finishWithOk());

        Intent intent = getIntent();
        eventFromIntent = (Event) intent.getExtras().get("INTENT_EVENT");
        date.setText(getString(R.string.date,eventFromIntent.getDate().get(Calendar.DATE),eventFromIntent.getDate().get(Calendar.MONTH),eventFromIntent.getDate().get(Calendar.YEAR)));
        textName.setText(eventFromIntent.getName());
        color_selector.check(color_selector.getChildAt(eventFromIntent.getColor().ordinal()).getId());
    }

    private void datePickerDialog(EditText editText) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year, month, day) -> editText.setText(getString(R.string.date , day, month, year)),
                mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private Calendar getCalendarFromString(String string) throws RuntimeException {
        if (string == null || string.length() == 0) throw new RuntimeException();

        String[] date = string.split("/");
        if (date.length != 3) throw new RuntimeException();

        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(
                parseInt(date[2].trim()),
                parseInt(date[1].trim()),
                parseInt(date[0].trim())
        );
        return c;
    }

    private Event eventFromForm() throws RuntimeException {
        if(textName.getText().toString().length() == 0) throw new RuntimeException();

        Calendar c1 = getCalendarFromString(date.getText().toString());

        int radioId = color_selector.getCheckedRadioButtonId();
        View radioButton = color_selector.findViewById(radioId);
        int id = color_selector.indexOfChild(radioButton);

        return new Event(eventFromIntent.getUid(), textName.getText().toString(), c1, EventType.ONE_DAY, EventColor.values()[id]);
    }

    private void finishWithOk() {
        try {
            Event event = eventFromForm();
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    MainActivity.db.eventDao().insert(event);
                }
            });
            finish();
        } catch (RuntimeException e) {
            Toast.makeText(this,"Fields not completed correctly",Toast.LENGTH_SHORT).show();
        }
    }
}
