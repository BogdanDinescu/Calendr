package com.bogdan.calendr;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.eventHolder> {
    private List<Event> events;
    private final AppDatabase db;

    public EventAdapter(List<Event> events, AppDatabase db) {
        this.events = events;
        this.db = db;
    }

    public static class eventHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView date;
        ImageView edit;
        ImageView delete;
        public eventHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.event_holder_name);
            date = itemView.findViewById(R.id.event_holder_date);
            edit = itemView.findViewById(R.id.btn_edit);
            delete = itemView.findViewById(R.id.btn_delete);
        }
    }

    @NonNull
    @Override
    public eventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View eventView = inflater.inflate(R.layout.event, parent, false);
        return new eventHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull eventHolder holder, int position) {
        final Event item = events.get(position);
        holder.name.setText(item.getName());
        holder.date.setText(item.getDate().getTime().toString());
        holder.edit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditEvent.class);
            intent.putExtra("INTENT_EVENT", events.get(position));
            v.getContext().startActivity(intent);
        });
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
                                    db.eventDao().delete(events.get(position));
                                }
                            });
                        }})
                    .setNegativeButton("Cancel", null).show();
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
