package com.bogdan.calendr;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.Calendar;
import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event ORDER BY date")
    LiveData<List<Event>> getAll();

    @Query("SELECT * FROM event WHERE date >= :date ORDER BY date")
    LiveData<List<Event>> getAllEventsAfter(Calendar date);

    @Query("SELECT * FROM event WHERE (date >= :startDay AND date < :endDay) OR (type = :type AND date in (:dates))")
    LiveData<List<Event>> getEventsByRangeOrType(Calendar startDay, Calendar endDay, EventType type, List<Calendar> dates);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Event event);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM event WHERE date < :day")
    void deleteEventsBefore(Calendar day);
}
