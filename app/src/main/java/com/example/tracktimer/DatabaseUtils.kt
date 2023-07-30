package com.example.tracktimer

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun MainActivity.fetchNotesAndUpdateAdapter() {
    lifecycleScope.launch(Dispatchers.IO) {
        val noteDatabase = noteDatabase
        val noteDao = noteDatabase.noteDao()

        val notes = noteDao.allNotes
        withContext(Dispatchers.Main) {
            noteAdapter.setNotes(notes)
        }
    }
}

fun MainActivity.addNoteAndUpdateAdapter(timestamp : Long, text : String) {
    lifecycleScope.launch(Dispatchers.IO) {
        val note = NoteEntity(timestamp, text)
        noteDatabase.noteDao().insert(note)

        withContext(Dispatchers.Main) {
            noteAdapter.addNote(note)
        }
    }
}