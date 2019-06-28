package com.cougar.maksim.fastnotes.contractClasses;

import android.app.Activity;
import android.content.Intent;

import com.cougar.maksim.fastnotes.dataClasses.Note;
import com.cougar.maksim.fastnotes.dataClasses.NoteStatus;
import com.cougar.maksim.fastnotes.dbWork.NoteLab;
import com.cougar.maksim.fastnotes.fragments.DatePickerFragment;
import com.cougar.maksim.fastnotes.fragments.StatusFragment;
import com.cougar.maksim.fastnotes.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

//TODO fix dependency
import static com.cougar.maksim.fastnotes.fragments.NoteFragment.REQUEST_DATE;
import static com.cougar.maksim.fastnotes.fragments.NoteFragment.REQUEST_STATUS;

public class NotePresenter implements NoteContract.Presenter {

    private NoteContract.View mView;
    private Note mNote;
    private boolean mIsNewItem;

    public NotePresenter(NoteContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void setData(boolean isNewItem, UUID id) {
        this.mIsNewItem = isNewItem;

        if (!isNewItem) {
            if(id!=null) {
                mNote = NoteLab.get(mView.getActivity()).getNote(id);
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
        }
    }

    @Override
    public void setNoteDate(Date date) {
        mNote.setDate(date);

    }

    @Override
    public void setNoteTitle(String s) {
        mNote.setTitle(s);
    }

    @Override
    public void setNoteContent(String s) {
        mNote.setData(s);
    }

    @Override
    public void updateNote() {
        if (!mIsNewItem) {
            NoteLab.get(mView.getActivity()).updateNote(mNote);
        } else {
            NoteLab.get(mView.getActivity()).addNote(mNote);
        }
    }

    @Override
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

    @Override
    public void updateTitleView() {
        mView.updateTitle(mNote.getTitle());
    }

    @Override
    public void updateDateView() {
        Date date = mNote.getDate();
        if(date!=null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
            mView.updateDateBtn(dateFormat.format(mNote.getDate()));
        }
        else {
            mView.updateDateBtn(mView.getActivity().getString(R.string.set_date));
        }
    }

    @Override
    public void updateDataView() {
        mView.updateData(mNote.getData());
    }

    @Override
    public void updateStatusView() {
        NoteStatus noteStatus = mNote.getStatus();
        if(noteStatus != null){
            mView.updateStatusBtn(noteStatus.getStringVal());
        }
        else {
            //TODO set default status
            mView.updateStatusBtn(NoteStatus.ALWAYS.getStringVal());
        }
    }

    @Override
    public Date getDate() {
        return mNote.getDate();
    }

    @Override
    public UUID getId() {
        return mNote.getId();
    }

    public String getStatus() {
        return mNote.getStatus().toString();
    }
}