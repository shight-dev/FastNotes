package com.cougar.maksim.fastnotes.DbWork

import android.database.Cursor
import android.database.CursorWrapper

import com.cougar.maksim.fastnotes.DataClasses.Note
import com.cougar.maksim.fastnotes.DataClasses.NoteStatus
import java.lang.Exception

import java.util.Date
import java.util.UUID

class NoteCursorWrapper
/**
 * Creates a cursor wrapper.
 *
 * @param cursor The underlying cursor to wrap.
 */
(cursor: Cursor) : CursorWrapper(cursor) {

    val note: Note
        get() {
            val uuidString = getString(getColumnIndex(NoteTable.NoteFields.UUID))
            val title = getString(getColumnIndex(NoteTable.NoteFields.TITLE))
            val data = getString(getColumnIndex(NoteTable.NoteFields.DATA))
            val date = getLong(getColumnIndex(NoteTable.NoteFields.DATE))
            val status: String? = getString(getColumnIndex(NoteTable.NoteFields.STATUS))

            val note = Note(UUID.fromString(uuidString))
            note.title = title
            note.data = data
            note.date = Date(date)

            val noteStatus = try {
                NoteStatus.valueOf(status ?: NoteStatus.NEVER.toString())
            } catch (e: Exception) {
                NoteStatus.NEVER
            }

            note.status = noteStatus

            return note
        }
}
