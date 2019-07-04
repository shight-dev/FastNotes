package com.cougar.maksim.fastnotes.presenters;

import android.app.Activity;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.cougar.maksim.fastnotes.daggerWork.App;
import com.cougar.maksim.fastnotes.dataClasses.Note;
import com.cougar.maksim.fastnotes.dbWork.NoteLab;
import com.cougar.maksim.fastnotes.mvpMoxyViews.NoteListView;
import com.cougar.maksim.fastnotes.services.NoteNotificationService;

import java.util.List;
import java.util.UUID;

//TODO fix dependency
import static com.cougar.maksim.fastnotes.fragments.NoteListFragment.DELETE_NOTE;

@InjectViewState
public class NoteListPresenter extends MvpPresenter<NoteListView> {

    private boolean mTodayEvents;
    private UUID mInUpdate;


    public NoteListPresenter() {
        //this.mView = mView;
        mTodayEvents = false;
        mInUpdate = null;
    }

    public void setTodayEvents(boolean todayEvents) {
        mTodayEvents = todayEvents;
    }

    public void changeTodayEvents() {
        if (mTodayEvents) {
            mTodayEvents = false;
            getViewState().setMenuItemSearch();
        } else {
            mTodayEvents = true;
            getViewState().setMenuItemDelete();
        }
    }

    public void updateMenu() {
        if (mTodayEvents) {
            getViewState().setMenuItemDelete();
        } else {
            getViewState().setMenuItemSearch();
        }
    }

    public void onResume() {
        getViewState().updateUI();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //set null in some case lifecycle errors
        mInUpdate = null;
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        //TODO обновлять конкретный элемент
        /*if (requestCode == UPDATE_NOTE) {
            UUID id;
            try {
                id = (UUID) data.getSerializableExtra("id");
            } catch (Exception e) {
                id = null;
            }
            mInUpdate = id;
        }*/
        if (requestCode == DELETE_NOTE) {
            getViewState().updateUI();
        }
    }

    public void startNotifications() {
        NoteNotificationService.setServiceAlarm(App.getAppContext(), true);
    }

    public List<Note> getNotes() {
        if(!mTodayEvents) {
            return NoteLab.get(App.getAppContext()).getNotes();
        }
        else {
            return NoteLab.get(App.getAppContext()).getTodayNotes();
        }
    }

    public void updateDataset(List<Note> notes) {
        //TODO в текущей реализации проход по if ветке недостижим, переделать или удалить
        if (mInUpdate != null) {
            int position = -1;

            //TODO optimise search
            //TODO DiffUtil
            for (int i = 0; i < notes.size(); ++i) {
                if (notes.get(i).getId().equals(mInUpdate)) {
                    position = i;
                    break;
                }
            }
            mInUpdate = null;
            getViewState().updateAdapterElement(position);
        } else {
            getViewState().updateAdapterDataset();
        }
    }

    public boolean getTodayEvents() {
        return mTodayEvents;
    }
}
