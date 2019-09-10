package com.cougar.maksim.fastnotes.presenters;

import android.app.Activity;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.cougar.maksim.fastnotes.daggerWork.App;
import com.cougar.maksim.fastnotes.dataClasses.Note;
import com.cougar.maksim.fastnotes.dataClasses.NoteStatus;
import com.cougar.maksim.fastnotes.dbWork.NoteLab;
import com.cougar.maksim.fastnotes.fragments.DatePickerFragment;
import com.cougar.maksim.fastnotes.fragments.StatusFragment;
import com.cougar.maksim.fastnotes.R;
import com.cougar.maksim.fastnotes.mvpMoxyViews.NoteView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

//TODO fix dependency
import static com.cougar.maksim.fastnotes.fragments.NoteFragment.REQUEST_DATE;
import static com.cougar.maksim.fastnotes.fragments.NoteFragment.REQUEST_STATUS;

@InjectViewState
public class NotePresenter extends MvpPresenter<NoteView> {

    //private NoteContract.View mView;
    private Note mNote;
    private boolean mIsNewItem;

    public NotePresenter() {
        //this.mView = mView;
    }

    public void setData(boolean isNewItem, UUID id) {
        this.mIsNewItem = isNewItem;

        if (!isNewItem) {
            if(id!=null) {
                //TODO inject
                mNote = NoteLab.get(App.getAppContext()).getNote(id);
            }
            else {
                throw new RuntimeException("No id arg exception");
            }
        } else {
            mNote = new Note();
            //TODO date format
            mNote.setDate(new Date());
            //TODO add menu to choose default value
            mNote.setStatus(NoteStatus.NEVER);
            //TODO add menu to choose default value
            mNote.setNotify(false);
        }
    }

    public void setNoteDate(Date date) {
        mNote.setDate(date);

    }

    public void setNoteTitle(String s) {
        mNote.setTitle(s);
    }

    public void setNoteContent(String s) {
        mNote.setData(s);
    }

    public void updateNote() {
        if (!mIsNewItem) {
            NoteLab.get(App.getAppContext()).updateNote(mNote);
        } else {
            NoteLab.get(App.getAppContext()).addNote(mNote);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode !=Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            //TODO check external usage
            setNoteDate(date);
            updateDateView();
        }

        if(requestCode == REQUEST_STATUS){
            NoteStatus status = (NoteStatus) data.getSerializableExtra(StatusFragment.STATUS);
            setNoteStatus(status);
            updateStatusView();
        }
    }

    public void setNoteStatus(NoteStatus status) {
        mNote.setStatus(status);
    }

    public void updateTitleView() {
        getViewState().updateTitle(mNote.getTitle());
    }

    public void updateDateView() {
        Date date = mNote.getDate();
        if(date!=null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
            getViewState().updateDateBtn(dateFormat.format(mNote.getDate()));
        }
        else {
            getViewState().updateDateBtn(App.getAppContext().getString(R.string.set_date));
        }
    }

    public void updateDataView() {
        getViewState().updateData(mNote.getData());
    }

    public void updateStatusView() {
        NoteStatus noteStatus = mNote.getStatus();
        if(noteStatus != null){
            getViewState().updateStatusBtn(noteStatus.getStringVal());
        }
        else {
            //TODO set default status
            getViewState().updateStatusBtn(NoteStatus.ALWAYS.getStringVal());
        }
    }

    public void updateNotify(){
        getViewState().updateNotify(mNote.getNotify());
    }

    public void nofifyStatusChanged(boolean isChecked){
        mNote.setNotify(isChecked);
    }

    public Date getDate() {
        return mNote.getDate();
    }

    public UUID getId() {
        return mNote.getId();
    }

    public String getStatus() {
        return mNote.getStatus().toString();
    }
}
