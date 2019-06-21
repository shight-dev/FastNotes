package com.cougar.maksim.fastnotes.Activities

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.cougar.maksim.fastnotes.DataClasses.Note
import com.cougar.maksim.fastnotes.DataClasses.NoteStatus
import com.cougar.maksim.fastnotes.DbWork.Room.NoteDb
import com.cougar.maksim.fastnotes.DbWork.Room.NotesDao
import com.cougar.maksim.fastnotes.Fragments.NoteFragment
import com.cougar.maksim.fastnotes.Fragments.NoteListFragment
import com.cougar.maksim.fastnotes.R
import java.lang.Exception
import java.util.*

class NoteCombinedActivity : SingleSwapFragmentActivity(),
        NoteListFragment.OnNoteListFragmentInteractionListener,
        NoteFragment.OnNoteFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*try {
            val noteDb: NoteDb = Room.databaseBuilder(this, NoteDb::class.java, "noteDb")
                    .allowMainThreadQueries().build()
            val noteDao: NotesDao = noteDb.notesDao()
            val uuid = UUID.randomUUID()
            val note = Note(uuid)
            note.data = "qwe"
            note.date = Date()
            note.status = NoteStatus.ALL_BEFORE_DATE
            note.title = "i'm awesome"
            noteDao.addNote(note)
            val noteReloaded = noteDao.getNote(uuid)
            val l = 1
        }
        catch (e:Exception){
            val x =1
        }*/
    }

    override fun onNoteFragmentInteraction() {
        setStartFragment()
    }

    override fun onNoteListFragmentInteraction(id: UUID?) {
        setSecondFragment(id)
    }

    private val EXTRA_NOTE_ID = "CURRENT_NOTE_ID"
    private val EXTRA_NEW_ITEM = "NEW_ITEM"

    override fun createStartFragment(): Fragment {
        return if (!intent.getBooleanExtra(TODAY_EVENTS, false)) {
            NoteListFragment.newInstance(false)
        } else {
            NoteListFragment.newInstance(true)
        }
    }

    override fun createSecondFragment(data: Any?): Fragment {
        var id: UUID? = null
        data?.let {
            if (data is UUID) {
                id = data
            }
        }
        //TODO rework with kotlin not null features
        return if (id != null) {
            NoteFragment.newInstance(id)
        } else {
            NoteFragment.newInstance(true)
        }
    }

    override fun setStartFragment() {
        val fm = supportFragmentManager
        fm.beginTransaction()
                .replace(R.id.fragment_container, createStartFragment())
                .commit()
    }

    override fun setSecondFragment(data: Any?) {
        val fm = supportFragmentManager
        fm.beginTransaction()
                .replace(R.id.fragment_container, createSecondFragment(data))
                .commit()
    }

    companion object {
        private val TODAY_EVENTS = "today_events"

        fun newIntent(packageContext: Context, todayEvents: Boolean): Intent {
            val intent = Intent(packageContext, NoteCombinedActivity::class.java)
            if (todayEvents) {
                intent.putExtra(TODAY_EVENTS, true)
            }
            return intent
        }
    }


}