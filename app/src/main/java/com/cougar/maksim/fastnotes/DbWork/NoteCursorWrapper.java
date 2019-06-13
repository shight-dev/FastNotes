package com.cougar.maksim.fastnotes.DbWork;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.cougar.maksim.fastnotes.DataClasses.Note;
import com.cougar.maksim.fastnotes.DataClasses.NoteStatus;

import java.util.Date;
import java.util.UUID;

public class NoteCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote(){
        String uuidString = getString(getColumnIndex(NoteTable.NoteFields.UUID));
        String title = getString(getColumnIndex(NoteTable.NoteFields.TITLE));
        String data = getString(getColumnIndex(NoteTable.NoteFields.DATA));
        Long date= getLong(getColumnIndex(NoteTable.NoteFields.DATE));
        String status = getString(getColumnIndex(NoteTable.NoteFields.STATUS));
        //TODO rework (possibly error in lifecycle of note)
        if(status == null){
            status = NoteStatus.NEVER.toString();
        }

        Note note=new Note(UUID.fromString(uuidString));
        note.setTitle(title);
        note.setData(data);
        note.setDate(new Date(date));
        //TODO use safe cast
        note.setStatus(NoteStatus.valueOf(status));

        return note;
    }
}
