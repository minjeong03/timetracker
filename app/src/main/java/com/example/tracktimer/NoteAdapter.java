package com.example.tracktimer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<NoteEntity> notes;

    public NoteAdapter(List<NoteEntity> notes) {
        this.notes = notes;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        NoteEntity note = notes.get(position);
        holder.textViewTimestamp.setText(formatTimestamp(note.getTimestamp()));
        holder.textViewNote.setText(note.getText());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<NoteEntity> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public void addNote(NoteEntity note) {
        this.notes.add(0, note);
        notifyDataSetChanged();
    }

    private String formatTimestamp(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(timestamp));
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTimestamp;
        TextView textViewNote;

        NoteViewHolder(View itemView) {
            super(itemView);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            textViewNote = itemView.findViewById(R.id.textViewNote);
        }
    }
}
