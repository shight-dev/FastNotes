package com.cougar.maksim.fastnotes.dbWork;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.cougar.maksim.fastnotes.dataClasses.Note;
import com.cougar.maksim.fastnotes.dbWork.room.NoteDb;

import java.util.List;
import java.util.UUID;

public class NoteLab {
    private static NoteLab sNoteLab;
    private NoteDb noteDb;

    public static NoteLab get(Context context) {
        if (sNoteLab == null) {
            sNoteLab = new NoteLab(context);
        }
        return sNoteLab;
    }

    public NoteLab(Context context) {
        //TODO work async
        noteDb = Room.databaseBuilder(context, NoteDb.class, "noteDb")
                .allowMainThreadQueries()
                .build();
    }

    public void addNote(Note note) {
        noteDb.notesDao().addNote(note);
    }

    public void updateNote(Note note) {
        noteDb.notesDao().updateNote(note);
    }

    public List<Note> getNotes() {
        return noteDb.notesDao().getNotes();
    }

    public List<Note> getActualNotes(){
        return noteDb.notesDao().getActualNotes();
    }

    public Note getNote(UUID id) {
        return noteDb.notesDao().getNote(id);
    }

    public void deleteNote(UUID id) {
        Note note = noteDb.notesDao().getNote(id);
        noteDb.notesDao().deleteNote(note);
    }
}
