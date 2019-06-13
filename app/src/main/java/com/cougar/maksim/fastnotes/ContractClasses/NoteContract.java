package com.cougar.maksim.fastnotes.ContractClasses;

import android.app.Activity;
import android.content.Intent;

import com.cougar.maksim.fastnotes.DataClasses.NoteStatus;

import java.util.Date;
import java.util.UUID;

public interface NoteContract {
    interface View{
        void updateDateBtn(String s);
        void updateStatusBtn(String s);
        void updateTitle(String s);
        void updateData(String s);
        Activity getActivity();
    }

    interface Presenter{
        void setData(boolean isNewItem, UUID id);
        void updateNoteDate(Date date);
        void updateNoteTitle(String s);
        void updateNoteContent(String s);
        void updateNoteStatus(NoteStatus status);
        void updateNote();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void updateTitleView();
        void updateDateView();
        void updateDataView();
        void updateStatusView();

        Date getDate();
        UUID getId();
    }
}
