package com.bogdan.calendr;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;

import java.util.Date;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private Button todayButton;
    private Button createButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarView = view.findViewById(R.id.calendarView);
        todayButton = view.findViewById(R.id.today_button);
        createButton = view.findViewById(R.id.create_button);
        todayButton.setOnClickListener(v -> todayClick());
        createButton.setOnClickListener(v -> createEvent());
    }

    public void todayClick() {
        Date now = new Date();
        try {
            calendarView.setDate(now);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
    }


    public void createEvent() {
        Intent intent = new Intent(getActivity(), EditEvent.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }
}
