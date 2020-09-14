package com.example.hydrophoicapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao

public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Query("SELECT * FROM firebase_data")
    LiveData<List<Note>> getAllNotes();

}
