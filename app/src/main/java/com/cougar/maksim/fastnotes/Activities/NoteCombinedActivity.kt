package com.cougar.maksim.fastnotes.Activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.cougar.maksim.fastnotes.Fragments.NoteFragment
import com.cougar.maksim.fastnotes.Fragments.NoteListFragment
import com.cougar.maksim.fastnotes.R
import java.util.*

class NoteCombinedActivity : SingleSwapFragmentActivity(), NoteListFragment.OnNoteListFragmentInteractionListener {
    override fun onFragmentInteraction(id: UUID?) {
        setSecondFragment(id)
    }

    private val TODAY_EVENTS = "today_events"

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

    fun newIntent(packageContext: Context, todayEvents: Boolean): Intent {
        val intent = Intent(packageContext, NoteListActivity::class.java)
        if (todayEvents) {
            intent.putExtra(TODAY_EVENTS, true)
        }
        return intent
    }

}