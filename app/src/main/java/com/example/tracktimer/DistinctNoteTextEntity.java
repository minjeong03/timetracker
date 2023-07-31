package com.example.tracktimer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "distinct_note_texts")
public class DistinctNoteTextEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String distinctText;

    public DistinctNoteTextEntity(String distinctText) {
        this.distinctText = distinctText;
    }

    public int getId() {
        return id;
    }

    public String getDistinctText() {
        return distinctText;
    }

    public void setId(int id) {
        this.id = id;
    }
}
