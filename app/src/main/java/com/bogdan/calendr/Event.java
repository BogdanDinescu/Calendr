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
    private EventType type;
    private EventColor color;
    // only for BIRTHDAY type
    private boolean considerYear;

    public Event(int uid, String name, Calendar date, EventType type, EventColor color, boolean considerYear) {
        this.uid = uid;
        this.name = name;
        this.date = date;
        this.type = type;
        this.color = color;
        this.considerYear = considerYear;
    }

    protected Event(Parcel in) {
        uid = in.readInt();
        name = in.readString();
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(in.readLong());
        date = c1;
        type = (EventType) in.readSerializable();
        color = (EventColor) in.readSerializable();
        considerYear = in.readByte() != 0;
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

    public boolean getConsiderYear() {
        return considerYear;
    }

    public void setConsiderYear(boolean considerYear) {
        this.considerYear = considerYear;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(name);
        dest.writeLong(date.getTimeInMillis());
        dest.writeSerializable(type);
        dest.writeSerializable(color);
        dest.writeByte((byte) (considerYear ? 1 : 0));
    }
}
