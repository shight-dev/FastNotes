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

    @Query("select * from Note where ((date = :dateStrVal) and (status = :atDay)) " +
            "or (status = :always) or ((date  >= :dateStrVal) and (status = :allBeforeDay))")
    fun getTodayNotes(dateStrVal: String, atDay: String, always: String, allBeforeDay: String): List<Note>
}