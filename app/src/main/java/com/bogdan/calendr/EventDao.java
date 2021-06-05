package com.bogdan.calendr;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.Calendar;
import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event")
    LiveData<List<Event>> getAll();

    @Query("SELECT * FROM event WHERE uid = :id")
    List<Event> getEventById(int id);

    @Query("SELECT * FROM event WHERE date = :day")
    LiveData<List<Event>> getEventsByDay(Calendar day);

    @Update
    void update(Event event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Event event);

    @Delete
    void delete(Event event);
}
