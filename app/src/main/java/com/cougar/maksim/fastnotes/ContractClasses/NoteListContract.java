package com.cougar.maksim.fastnotes.ContractClasses;

import android.app.Activity;
import android.content.Intent;

import com.cougar.maksim.fastnotes.DataClasses.Note;

import java.util.List;

public interface NoteListContract {

    interface View{
        void setMenuItemDelete();
        void setMenuItemSearch();

        void updateAdapterElement(int position);
        void updateAdapterDataset();

        void updateUI();

        Activity getActivity();
    }

    interface Presenter{
        void setTodayEvents(boolean todayEvents);
        void changeTodayEvents();
        void updateMenu();
        void onResume();
        void onActivityResult(int requestCode, int resultCode, Intent data);

        void startNotifications();

        List<Note> getNotes();

        void updateDataset(List<Note> notes);

        boolean getTodayEvents();
    }
}
