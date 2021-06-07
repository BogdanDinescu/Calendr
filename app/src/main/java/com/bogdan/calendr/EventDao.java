package com.bogdan.calendr;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.Calendar;
import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event ORDER BY date")
    LiveData<List<Event>> getAll();

    @Query("SELECT * FROM event WHERE date = :day")
    LiveData<List<Event>> getEventsByDay(Calendar day);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Event event);

    @Delete
    void delete(Event event);
}
