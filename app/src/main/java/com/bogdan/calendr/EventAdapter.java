package com.bogdan.calendr;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.eventHolder> {
    private final List<Event> events;
    private final AppDatabase db;
    private Context context;

    public EventAdapter(List<Event> events, AppDatabase db) {
        this.events = events;
        this.db = db;
    }

    public static class eventHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView birthday;
        ImageView edit;
        ImageView delete;
        public eventHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.event_holder_name);
            date = itemView.findViewById(R.id.event_holder_date);
            birthday = itemView.findViewById(R.id.event_anniversary);
            edit = itemView.findViewById(R.id.btn_edit);
            delete = itemView.findViewById(R.id.btn_delete);
        }
    }

    @NonNull
    @Override
    public eventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View eventView = inflater.inflate(R.layout.event, parent, false);
        context = parent.getContext();
        return new eventHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull eventHolder holder, int position) {
        final Event item = events.get(position);
        holder.name.setText(item.getName());
        holder.name.setTextColor(ContextCompat.getColor(context,MainActivity.eventColorToColor(item.getColor())));
        // birthday
        StringBuilder birthdayText = new StringBuilder();
        if (item.getType() == EventType.BIRTHDAY) {
            birthdayText.append("Anniversary");
            if (item.getConsiderYear()) {
                birthdayText.append(" ")
                        .append(Calendar.getInstance().get(Calendar.YEAR) - item.getDate().get(Calendar.YEAR))
                        .append(" years");
            }
            holder.birthday.setText(birthdayText);
        } else {
            holder.birthday.setVisibility(View.GONE);
        }
        // date
        StringBuilder dateText = new StringBuilder();
        if (item.getType() == EventType.ONE_DAY || item.getType() == EventType.REMINDER) {
            dateText.append(SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(item.getDate().getTime()));
            if (item.getType() == EventType.REMINDER) {
                dateText.append(" ");
                dateText.append(SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(item.getDate().getTime()));
            }
        }
        if (item.getType() == EventType.BIRTHDAY) {
            if (item.getConsiderYear()) {
                dateText.append(SimpleDateFormat.getDateInstance(DateFormat.SHORT).format(item.getDate().getTime()));
            } else {
                SimpleDateFormat monthDayFormat = new SimpleDateFormat("dd/MM", Locale.UK);
                dateText.append(monthDayFormat.format(item.getDate().getTime()));
            }
        }
        holder.date.setText(dateText);
        // edit button
        holder.edit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditEventActivity.class);
            intent.putExtra("INTENT_EVENT", item);
            v.getContext().startActivity(intent);
        });
        // delete button
        holder.delete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Alert")
                    .setMessage("Do you really want to delete?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    db.eventDao().delete(item);
                                    if (item.getType() == EventType.REMINDER) {
                                        cancelAlert(item, v.getContext());
                                    }
                                }
                            });
                        }})
                    .setNegativeButton("Cancel", null).show();
        });
    }

    private void cancelAlert(Event event, Context context) {
        Intent intent = new Intent(context.getApplicationContext(), Broadcast.class);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), event.getUid(), intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), event.getUid(), intent, 0);
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
