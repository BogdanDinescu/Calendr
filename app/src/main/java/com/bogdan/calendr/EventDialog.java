package com.bogdan.calendr;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class EventDialog extends DialogFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_event,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View textName = view.findViewById(R.id.event_name);
        View cancel = view.findViewById(R.id.cancel_button);
        View ok = view.findViewById(R.id.ok_button);
        View btn_date1 = view.findViewById(R.id.btn_date1);
        View btn_date2 = view.findViewById(R.id.btn_date2);
        EditText in_date = view.findViewById(R.id.in_date);
        EditText end_date = view.findViewById(R.id.end_date);

        btn_date1.setOnClickListener(v -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            in_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        // Cancel
        cancel.setOnClickListener(v -> dismiss());
        // OK
        ok.setOnClickListener(v -> {

        });

    }
}
