package com.cougar.maksim.fastnotes.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.cougar.maksim.fastnotes.Fragments.NoteListFragment;


public class NoteListActivity extends SingleFragmentActivity {

    public static final String TODAY_EVENTS = "today_events";

    public static Intent newIntent(Context packageContext, boolean todayEvents){
        Intent intent = new Intent(packageContext, NoteListActivity.class);
        if(todayEvents){
            intent.putExtra(TODAY_EVENTS, true);
        }
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        if(!getIntent().getBooleanExtra(TODAY_EVENTS, false)){
            return new NoteListFragment();
        }
        else {
            return NoteListFragment.newInstance(true);
        }
    }
}
