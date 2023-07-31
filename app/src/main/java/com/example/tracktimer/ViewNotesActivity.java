package com.example.tracktimer;

import static com.example.tracktimer.DatabaseUtilsKt.fetchNotesAndUpdateAdapter;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewNotesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private NoteDatabase noteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(new ArrayList<NoteEntity>());
        recyclerView.setAdapter(noteAdapter);


        fetchNotesAndUpdateAdapter(this);

    }
    public NoteDatabase getNoteDatabase() {
        return noteDatabase;
    }
    public NoteAdapter getNoteAdapter() {
        return noteAdapter;
    }
}
