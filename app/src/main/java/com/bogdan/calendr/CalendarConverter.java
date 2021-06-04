package com.bogdan.calendr;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class CalendarConverter {
    @TypeConverter
    public static Calendar toCalendar(long value) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(value);
        return c;
    }
    @TypeConverter
    public static long toLong(Calendar value) {
        return value.getTimeInMillis();
    }
}
