package com.cougar.maksim.fastnotes.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.cougar.maksim.fastnotes.fragments.NoteFragment;

import java.util.UUID;

public class NoteActivity extends SingleFragmentActivity {

    public static final String EXTRA_NOTE_ID = "CURRENT_NOTE_ID";
    public static final String EXTRA_NEW_ITEM = "NEW_ITEM";

    public static Intent newIntent(Context packageContext, UUID noteId){
        Intent intent = new Intent(packageContext,NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        return intent;
    }

    public static Intent newItemIntent(Context packageContext){
        Intent intent = new Intent(packageContext,NoteActivity.class);
        intent.putExtra(EXTRA_NEW_ITEM, true);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        boolean isNewItem = getIntent().getBooleanExtra(EXTRA_NEW_ITEM, false);
        UUID id = (UUID)getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        if(!isNewItem){
            return NoteFragment.newInstance(id);
        }
        else {
            return NoteFragment.newInstance(true);
        }
    }
}
