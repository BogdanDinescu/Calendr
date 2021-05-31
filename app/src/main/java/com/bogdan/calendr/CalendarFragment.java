package com.bogdan.calendr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
        calendarView.setOnDateChangeListener((v, year, month, day) -> dateChange(year, month, day));
        createButton.setOnClickListener(v -> createEvent());
    }

    public void todayClick() {
        Date now = new Date();
        calendarView.setDate(now.getTime());
    }

    public void dateChange(int year, int month, int day) {

    }

    public void createEvent() {
        DialogFragment newFragment = new EventDialog();
        newFragment.show(getActivity().getSupportFragmentManager(),"eventDialog");
    }
}
