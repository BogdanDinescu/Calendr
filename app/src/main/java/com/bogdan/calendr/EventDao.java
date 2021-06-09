package com.bogdan.calendr;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.Calendar;
import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event ORDER BY date")
    LiveData<List<Event>> getAll();

    @Query("SELECT * FROM event WHERE date >= :startDay AND date <= :endDay")
    LiveData<List<Event>> getEventsByRange(Calendar startDay, Calendar endDay);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Event event);

    @Delete
    void delete(Event event);

    @Query("DELETE FROM event WHERE date < :day")
    void deleteEventsBefore(Calendar day);
}
