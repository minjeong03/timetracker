package com.example.tracktimer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DistinctNoteTextDao {
    @Insert
    void insert(DistinctNoteTextEntity distinctNoteText);

    @Query("SELECT * FROM distinct_note_texts")
    List<DistinctNoteTextEntity> getAllDistinctNoteTexts();

    @Query("SELECT * FROM distinct_note_texts WHERE distinctText = :text LIMIT 1")
    DistinctNoteTextEntity getDistinctNoteTextByText(String text);
}
