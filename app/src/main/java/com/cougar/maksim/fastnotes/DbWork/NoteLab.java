package com.cougar.maksim.fastnotes.DbWork;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.cougar.maksim.fastnotes.DataClasses.Note;
import com.cougar.maksim.fastnotes.DataClasses.NoteStatus;
import com.cougar.maksim.fastnotes.DbWork.Room.NoteDb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

    private NoteLab(Context context) {
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

    public List<Note> getTodayNotes() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(simpleDateFormat.format(date));
        } catch (Exception e) {
            //use wrong date
            //TODO rework with JodaTime or something else
        }
        long dateTime = date.getTime();
        String dateStrVal = String.valueOf(dateTime);

        return noteDb.notesDao().getTodayNotes(dateStrVal,
                NoteStatus.AT_DAY.toString(), NoteStatus.ALWAYS.toString(), NoteStatus.ALL_BEFORE_DATE.toString());
    }

    public Note getNote(UUID id) {
        return noteDb.notesDao().getNote(id);
    }

    public void deleteNote(UUID id) {
        Note note = noteDb.notesDao().getNote(id);
        noteDb.notesDao().deleteNote(note);
    }
}
