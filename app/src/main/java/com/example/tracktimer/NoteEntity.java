package com.example.tracktimer;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long timestamp;
    private String text;

    public NoteEntity(long timestamp, String text) {
        this.timestamp = timestamp;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }

    public void setId(int id) {
        this.id = id;
    }
}
