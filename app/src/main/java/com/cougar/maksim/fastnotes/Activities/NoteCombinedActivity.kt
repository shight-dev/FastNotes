package com.cougar.maksim.fastnotes.Activities

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.cougar.maksim.fastnotes.Fragments.NoteFragment
import com.cougar.maksim.fastnotes.Fragments.NoteListFragment
import com.cougar.maksim.fastnotes.R
import java.util.*

class NoteCombinedActivity : SingleSwapFragmentActivity(),
        NoteListFragment.OnNoteListFragmentInteractionListener,
        NoteFragment.OnNoteFragmentInteractionListener {

    override fun onNoteFragmentInteraction() {
        setStartFragment()
        if (landscape) {
            removeFragmentFromContainer(NoteFragment::class.java)
        }
    }

    override fun onNoteListFragmentInteraction(id: UUID?) {
        setSecondFragment(id)
    }

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
        if (!landscape) {
            setStartFragmentToContainer(R.id.fragment_container)
        } else {
            setStartFragmentToContainer(R.id.main_fragment_container)
        }
    }

    override fun setSecondFragment(data: Any?) {
        if (!landscape) {
            setSecondFragmentToContainer(data, R.id.fragment_container)
        } else {
            setSecondFragmentToContainer(data, R.id.second_fragment_container)
        }
    }

    private fun setStartFragmentToContainer(containerId: Int) {
        val fm = supportFragmentManager
        fm.beginTransaction()
                .replace(containerId, createStartFragment())
                .commit()
    }

    private fun setSecondFragmentToContainer(data: Any?, containerId: Int) {
        val fm = supportFragmentManager
        fm.beginTransaction()
                .replace(containerId, createSecondFragment(data))
                .commit()
    }

    private fun removeFragmentFromContainer(fragmentClass: Class<out Fragment>) {
        val fm = supportFragmentManager
        val list = fm.fragments.filterIsInstance(fragmentClass)
        list.forEach {
            fm.beginTransaction()
                    .remove(it)
                    .commit()
        }
    }

    companion object {
        private const val TODAY_EVENTS = "today_events"

        fun newIntent(packageContext: Context, todayEvents: Boolean): Intent {
            val intent = Intent(packageContext, NoteCombinedActivity::class.java)
            if (todayEvents) {
                intent.putExtra(TODAY_EVENTS, true)
            }
            return intent
        }
    }


}