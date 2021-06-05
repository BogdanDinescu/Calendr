package com.bogdan.calendr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.eventHolder> {
    private List<Event> events;

    public EventAdapter(List<Event> events) {
        this.events = events;
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
        holder.edit.setOnClickListener(v -> {});
        holder.delete.setOnClickListener(v -> {});
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
