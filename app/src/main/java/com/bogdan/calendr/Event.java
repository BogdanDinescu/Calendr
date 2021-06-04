package com.bogdan.calendr;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity
public class Event implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    private String name;
    private Calendar date;
    private Calendar end;
    private EventType type;
    private EventColor color;

    public Event(String name, Calendar date, Calendar end, EventType type, EventColor color) {
        this.name = name;
        this.date = date;
        this.end = end;
        this.type = type;
        this.color = color;
    }

    protected Event(Parcel in) {
        name = in.readString();
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(in.readLong());
        date = c1;
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(in.readLong());
        end = c2;
        type = (EventType) in.readSerializable();
        color = (EventColor) in.readSerializable();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public EventColor getColor() {
        return color;
    }

    public void setColor(EventColor color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(date.getTimeInMillis());
        dest.writeLong(end.getTimeInMillis());
        dest.writeSerializable(type);
        dest.writeSerializable(color);
    }
}
