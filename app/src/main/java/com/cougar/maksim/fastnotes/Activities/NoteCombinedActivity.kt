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

class NoteCombinedActivity:SingleSwapFragmentActivity() {

    private val TODAY_EVENTS = "today_events"

    private val EXTRA_NOTE_ID = "CURRENT_NOTE_ID"
    private val EXTRA_NEW_ITEM = "NEW_ITEM"

    override fun createStartFragment(): Fragment {
        return if(!intent.getBooleanExtra(TODAY_EVENTS,false)){
            NoteListFragment.newInstance(false)
        }
        else{
            NoteListFragment.newInstance(true)
        }
    }

    override fun createSecondFragment(): Fragment {
        val isNewItem = intent.getBooleanExtra(EXTRA_NEW_ITEM, false)
        val id = intent.getSerializableExtra(EXTRA_NOTE_ID) as UUID
        return if (!isNewItem) {
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

    override fun setSecondFragment() {
        val fm = supportFragmentManager
        fm.beginTransaction()
                .replace(R.id.fragment_container, createSecondFragment())
                .commit()
    }

    fun newIntent(packageContext: Context, todayEvents: Boolean): Intent {
        val intent = Intent(packageContext, NoteListActivity::class.java)
        if (todayEvents) {
            intent.putExtra(TODAY_EVENTS, true)
        }
        return intent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            data?.let {
                setSecondFragment()
            }
        }
    }
}