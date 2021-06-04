package com.bogdan.calendr;

import androidx.room.*;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event")
    List<Event> getAll();

    @Query("SELECT * FROM event WHERE uid = :id")
    Event getEventById(int id);

    @Update
    void update(Event event);

    @Insert
    void insert(Event event);

    @Delete
    void delete(Event event);
}
