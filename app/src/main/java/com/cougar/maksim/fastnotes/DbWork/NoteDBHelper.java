package com.cougar.maksim.fastnotes.DbWork;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NoteDBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "noteBase.db";

    public NoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(String.format("create table %s( _id integer primary key autoincrement, %s text, %s text, %s text, %s INT, " +
                        "%s text)",
                NoteTable.NAME, NoteTable.NoteFields.UUID, NoteTable.NoteFields.TITLE, NoteTable.NoteFields.DATA, NoteTable.NoteFields.DATE,
                NoteTable.NoteFields.STATUS));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
