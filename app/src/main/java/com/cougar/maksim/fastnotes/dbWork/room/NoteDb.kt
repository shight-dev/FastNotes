package com.cougar.maksim.fastnotes.dbWork.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.cougar.maksim.fastnotes.dataClasses.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDb : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}