package com.example.tracktimer;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NoteEntity.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

        public abstract NoteDao noteDao();

        private static NoteDatabase instance;

        public static synchronized NoteDatabase getInstance(Context context) {
                if (instance == null) {
                        instance = Room.databaseBuilder((context.getApplicationContext()),
                                NoteDatabase.class, "note_database").build();
                }
                return instance;
        }
}
