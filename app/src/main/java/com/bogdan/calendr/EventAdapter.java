package com.bogdan.calendr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.eventHolder> {
    private List<Event> events;
    private Context context;

    public EventAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    public static class eventHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView date;
        public eventHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.event_holder_name);
            date = itemView.findViewById(R.id.event_holder_date);
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
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
