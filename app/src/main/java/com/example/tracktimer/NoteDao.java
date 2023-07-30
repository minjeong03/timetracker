package com.example.tracktimer;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(NoteEntity note);

    @Query("SELECT * FROM note_table ORDER BY timestamp DESC")
    List<NoteEntity> getAllNotes();

    @Delete
    void delete(NoteEntity note);

}
