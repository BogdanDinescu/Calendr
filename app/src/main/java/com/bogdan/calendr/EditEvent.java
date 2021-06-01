package com.bogdan.calendr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;

import java.util.Calendar;

public class EditEvent extends Activity {

    EditText textName;
    Button cancel;
    Button btn_ok;
    Button btn_date1;
    Button btn_date2;
    EditText in_date;
    EditText end_date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        textName = findViewById(R.id.event_name);
        cancel = findViewById(R.id.cancel_button);
        btn_ok = findViewById(R.id.ok_button);
        btn_date1 = findViewById(R.id.btn_date1);
        btn_date2 = findViewById(R.id.btn_date2);
        in_date = findViewById(R.id.in_date);
        end_date = findViewById(R.id.end_date);

        btn_date1.setOnClickListener(v -> datePickerDialog(in_date));
        btn_date2.setOnClickListener(v -> datePickerDialog(end_date));
        cancel.setOnClickListener(v -> finish());
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

}
