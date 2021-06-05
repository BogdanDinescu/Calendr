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

import static java.lang.Integer.parseInt;

public class EditEvent extends Activity {

    EditText textName;
    Button btn_cancel;
    Button btn_ok;
    Button btn_date1;
    Button btn_date2;
    EditText in_date;
    EditText end_date;
    RadioGroup color_selector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        textName = findViewById(R.id.event_name);
        btn_cancel = findViewById(R.id.cancel_button);
        btn_ok = findViewById(R.id.ok_button);
        btn_date1 = findViewById(R.id.btn_date1);
        btn_date2 = findViewById(R.id.btn_date2);
        in_date = findViewById(R.id.in_date);
        end_date = findViewById(R.id.end_date);
        color_selector = findViewById(R.id.color_selector);

        btn_date1.setOnClickListener(v -> datePickerDialog(in_date));
        btn_date2.setOnClickListener(v -> datePickerDialog(end_date));
        btn_cancel.setOnClickListener(v -> finish());
        btn_ok.setOnClickListener(v -> finishWithOk());

        Intent intent = getIntent();
        Calendar dateFromIntent = (Calendar) intent.getExtras().get("INTENT_DATE");
        in_date.setText(getString(R.string.date,dateFromIntent.get(Calendar.DATE),dateFromIntent.get(Calendar.MONTH),dateFromIntent.get(Calendar.YEAR)));
        end_date.setText(getString(R.string.date,dateFromIntent.get(Calendar.DATE),dateFromIntent.get(Calendar.MONTH),dateFromIntent.get(Calendar.YEAR)));
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
        c.set(
                parseInt(date[2].trim()),
                parseInt(date[1].trim()),
                parseInt(date[0].trim()),
                0,0,0
        );
        return c;
    }

    private Event eventFromForm() throws RuntimeException {
        if(textName.getText().toString().length() == 0) throw new RuntimeException();

        Calendar c1 = getCalendarFromString(in_date.getText().toString());
        Calendar c2 = getCalendarFromString(end_date.getText().toString());

        int radioId = color_selector.getCheckedRadioButtonId();
        View radioButton = color_selector.findViewById(radioId);
        int id = color_selector.indexOfChild(radioButton);

        return new Event(textName.getText().toString(), c1, c2, EventType.ONE_DAY, EventColor.values()[id]);
    }

    private void finishWithOk() {
        try {
            Event event = eventFromForm();
            Intent intent = new Intent();
            intent.putExtra("INTENT_RESPONSE", event);
            setResult(RESULT_OK, intent);
            finish();
        } catch (RuntimeException e) {
            Toast.makeText(this,"Fields not completed correctly",Toast.LENGTH_SHORT).show();
        }
    }
}
