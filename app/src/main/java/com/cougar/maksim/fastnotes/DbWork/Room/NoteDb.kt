package com.cougar.maksim.fastnotes.DbWork.Room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDb:RoomDatabase() {
    abstract fun notesDao():NotesDao
}