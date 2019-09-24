package com.cougar.maksim.fastnotes.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.cougar.maksim.fastnotes.AppState;
import com.cougar.maksim.fastnotes.daggerWork.App;
import com.cougar.maksim.fastnotes.dataClasses.Note;
import com.cougar.maksim.fastnotes.dbWork.NoteLab;
import com.cougar.maksim.fastnotes.mvpMoxyViews.NoteListView;
import com.cougar.maksim.fastnotes.services.NoteNotificationService;

import java.util.List;
import java.util.UUID;

//TODO fix dependency
import javax.inject.Inject;

import static com.cougar.maksim.fastnotes.fragments.NoteListFragment.DELETE_NOTE;

@InjectViewState
public class NoteListPresenter extends MvpPresenter<NoteListView> {

    private UUID mInUpdate;

    @Inject
    public NoteLab noteLab;

    @Inject
    public Context context;

    public NoteListPresenter() {
        //this.mView = mView;
        App.getComponent().inject(this);
        //mTodayEvents = false;
        mInUpdate = null;
    }

    public void setTodayEvents(boolean todayEvents) {
        AppState.State.setActualNotes(todayEvents);
    }

    public void changeTodayEvents() {
        if (AppState.State.getActualNotes()) {
            AppState.State.setActualNotes(false);
            getViewState().setMenuItemSearch();
        } else {
            AppState.State.setActualNotes(true);
            getViewState().setMenuItemDelete();
        }
    }

    //TODO move to view
    public void updateMenu() {
        if (AppState.State.getActualNotes()) {
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
        NoteNotificationService.setServiceAlarm(context);
    }

    public List<Note> getNotes() {
        if(!AppState.State.getActualNotes()) {
            return noteLab.getNotes();
        }
        else {
            return noteLab.getActualNotes();
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
        return AppState.State.getActualNotes();
    }
}
