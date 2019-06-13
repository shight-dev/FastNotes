package com.cougar.maksim.fastnotes.ContractClasses;

import android.app.Activity;
import android.content.Intent;

import com.cougar.maksim.fastnotes.DataClasses.Note;
import com.cougar.maksim.fastnotes.DbWork.NoteLab;
import com.cougar.maksim.fastnotes.Services.NoteNotificationService;

import java.util.List;
import java.util.UUID;

//TODO fix dependency
import static com.cougar.maksim.fastnotes.Fragments.NoteListFragment.DELETE_NOTE;
import static com.cougar.maksim.fastnotes.Fragments.NoteListFragment.UPDATE_NOTE;

public class NoteListPresenter implements NoteListContract.Presenter {

    private NoteListContract.View mView;

    private boolean mTodayEvents;
    private UUID mInUpdate;


    public NoteListPresenter(NoteListContract.View mView) {
        this.mView = mView;
        mTodayEvents = false;
        mInUpdate = null;
    }

    @Override
    public void setTodayEvents(boolean todayEvents) {
        mTodayEvents = todayEvents;
    }

    @Override
    public void changeTodayEvents() {
        if (mTodayEvents) {
            mTodayEvents = false;
            mView.setMenuItemSearch();
        } else {
            mTodayEvents = true;
            mView.setMenuItemDelete();
        }
    }

    @Override
    public void updateMenu() {
        if (mTodayEvents) {
            mView.setMenuItemDelete();
        } else {
            mView.setMenuItemSearch();
        }
    }

    @Override
    public void onResume() {
        mView.updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //set null in some case lifecycle errors
        mInUpdate = null;
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == UPDATE_NOTE) {
            UUID id;
            try {
                id = (UUID) data.getSerializableExtra("id");
            } catch (Exception e) {
                id = null;
            }
            mInUpdate = id;
        }
        if (requestCode == DELETE_NOTE) {
            mView.updateUI();
        }
    }

    @Override
    public void startNotifications() {
        NoteNotificationService.setServiceAlarm(mView.getActivity(), true);
    }

    @Override
    public List<Note> getNotes() {
        if(!mTodayEvents) {
            return NoteLab.get(mView.getActivity()).getNotes();
        }
        else {
            return NoteLab.get(mView.getActivity()).getTodayNotes();
        }
    }

    @Override
    public void updateDataset(List<Note> notes) {
        if (mInUpdate != null) {
            int position = -1;

            //TODO optimise search
            for (int i = 0; i < notes.size(); ++i) {
                if (notes.get(i).getId().equals(mInUpdate)) {
                    position = i;
                    break;
                }
            }
            mInUpdate = null;
            mView.updateAdapterElement(position);
        } else {
            mView.updateAdapterDataset();
        }
    }

    @Override
    public boolean getTodayEvents() {
        return mTodayEvents;
    }
}
