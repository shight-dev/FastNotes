package com.cougar.maksim.fastnotes.DbWork.Room

import android.arch.persistence.room.*
import java.util.*
import kotlin.collections.ArrayList

@Dao
interface NotesDao {

    @Insert
    fun addNote(noteEntity: NoteEntity)

    @Delete
    fun deleteNote(noteEntity: NoteEntity)

    @Update
    fun updateNote(noteEntity: NoteEntity)

    @Query("select * from NoteEntity where id = :id")
    fun getNote(id: String):NoteEntity

    @Query("select * from NoteEntity")
    fun getNotes(): List<NoteEntity>

    //fun getTodayNotes(): ArrayList<NoteEntity>
}