package com.cougar.maksim.fastnotes.dbWork.room

import android.arch.persistence.room.*
import com.cougar.maksim.fastnotes.dataClasses.Converters
import com.cougar.maksim.fastnotes.dataClasses.Note
import java.util.*

@Dao
interface NotesDao {

    @Insert
    fun addNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)

    @Query("select * from Note where uuid = :id")
    fun getNote(@TypeConverters(Converters::class) id: UUID): Note

    @Query("select * from Note")
    fun getNotes(): List<Note>

    @Query("select * from Note where notify = 1")
    fun getActualNotes():List<Note>
}