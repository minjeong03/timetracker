package com.example.tracktimer

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun ViewNotesActivity.fetchNotesAndUpdateAdapter() {
    lifecycleScope.launch(Dispatchers.IO) {
        val noteDatabase = noteDatabase
        val noteDao = noteDatabase.noteDao()

        val notes = noteDao.allNotes
        withContext(Dispatchers.Main) {
            noteAdapter.setNotes(notes)
        }
    }
}

fun MainActivity.addNoteAndUpdateLayout(timestamp : Long, text : String) {
    lifecycleScope.launch(Dispatchers.IO) {
        val note = NoteEntity(timestamp, text)
        noteDatabase.noteDao().insert(note)

        val distinctNoteEntity = noteDatabase.distinctNoteTextDao().getDistinctNoteTextByText(text)
        if (distinctNoteEntity == null) {
            val newDistinctNoteText = DistinctNoteTextEntity(text);
            noteDatabase.distinctNoteTextDao().insert(newDistinctNoteText)
            withContext(Dispatchers.Main) {
                appendButtonInLayout(text)
            }
        }
    }
}

fun MainActivity.fetchDistinctNoteTextsAndUpdateLayout() {
    lifecycleScope.launch(Dispatchers.IO) {
        val noteTexts = noteDatabase.distinctNoteTextDao().allDistinctNoteTexts
        withContext(Dispatchers.Main) {
            updateLayout(noteTexts)
        }
    }
}