package com.cougar.maksim.fastnotes.DbWork;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cougar.maksim.fastnotes.DataClasses.Note;
import com.cougar.maksim.fastnotes.DataClasses.NoteStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class NoteLab {
    private static NoteLab sNoteLab;
    private SQLiteDatabase mDatabase;

    public static NoteLab get(Context context) {
        if (sNoteLab == null) {
            sNoteLab = new NoteLab(context);
        }
        return sNoteLab;
    }

    public NoteLab(Context context) {
        mDatabase = new NoteDBHelper(context)
                .getWritableDatabase();
    }

    public void addNote(Note note) {
        ContentValues values=getContentValues(note);

        mDatabase.insert(NoteTable.NAME,null,values);
    }

    public void updateNote(Note note){
        String uuidString = note.getId().toString();
        ContentValues values=getContentValues(note);

        mDatabase.update(NoteTable.NAME,values,
                NoteTable.NoteFields.UUID + " = ?",
                new String[] { uuidString });
    }

    public List<Note> getNotes() {
        ArrayList<Note> noteList = new ArrayList<>();

        /*for (int i=0;i<100;++i) {
            Note note = new Note();
            note.setTitle("Test #"+i);
            note.setData("Data "+i);
            addNote(note);
        }*/

        try (NoteCursorWrapper cursor = queryNotes(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                noteList.add(cursor.getNote());
                cursor.moveToNext();
            }
        }

        return noteList;
    }

    public List<Note> getTodayNotes(){
        ArrayList<Note> noteList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

        Date date= new Date();
        try {
            date = simpleDateFormat.parse(simpleDateFormat.format(date));
        }
        catch (Exception e){
            //use wrong date
            //TODO rework with JodaTime or something else
        }
        long dateTime = date.getTime();
        String dateStrVal = String.valueOf(dateTime);

        //TODO rework string format (maybe use kotlin?)
        try (NoteCursorWrapper cursor = queryNotes(String.format("((%s = ?) and (%s = ?)) or (%s = ?) or ((%s >= ?) and (%s = ?))",
                NoteTable.NoteFields.DATE, NoteTable.NoteFields.STATUS, NoteTable.NoteFields.STATUS, NoteTable.NoteFields.DATE, NoteTable.NoteFields.STATUS),
                new String[] {dateStrVal, NoteStatus.AT_DAY.toString(), NoteStatus.ALWAYS.toString(), dateStrVal, NoteStatus.ALL_BEFORE_DATE.toString()})) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                noteList.add(cursor.getNote());
                cursor.moveToNext();
            }
        }
        return noteList;
    }

    public Note getNote(UUID id) {

        try (NoteCursorWrapper cursor = queryNotes(
                NoteTable.NoteFields.UUID + "= ?",
                new String[]{id.toString()}
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getNote();
        }
    }

    public void deleteNote(UUID id){
        deleteNotes(NoteTable.NoteFields.UUID + " = ?",new String[] {id.toString()});
    }

    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteTable.NoteFields.UUID, note.getId().toString());
        values.put(NoteTable.NoteFields.TITLE, note.getTitle());
        values.put(NoteTable.NoteFields.DATA, note.getData());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

        Date date= note.getDate();
        try {
            date = simpleDateFormat.parse(simpleDateFormat.format(date));
        }
        catch (Exception e){
            //use wrong date
            //TODO rework with JodaTime or something else
        }

        values.put(NoteTable.NoteFields.DATE, date.getTime());
        if(note.getStatus() != null) {
            values.put(NoteTable.NoteFields.STATUS, note.getStatus().toString());
        }

        return values;
    }

    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs){
        Cursor cursor=mDatabase.query(
                NoteTable.NAME,
                null,   //all fields
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new NoteCursorWrapper(cursor);
    }

    private void deleteNotes(String whereClause, String[] whereArgs){
        mDatabase.delete(
                NoteTable.NAME,
                whereClause,
                whereArgs
        );
    }
}
