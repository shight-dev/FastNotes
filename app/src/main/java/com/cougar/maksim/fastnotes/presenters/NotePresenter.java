package com.cougar.maksim.fastnotes.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.cougar.maksim.fastnotes.daggerWork.App;
import com.cougar.maksim.fastnotes.dataClasses.Note;
import com.cougar.maksim.fastnotes.dbWork.NoteLab;
import com.cougar.maksim.fastnotes.mvpMoxyViews.NoteView;

import java.util.UUID;

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
            //TODO add menu to choose default value
            mNote.setNotify(false);
        }
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

    public void updateTitleView() {
        getViewState().updateTitle(mNote.getTitle());
    }

    public void updateDataView() {
        getViewState().updateData(mNote.getData());
    }

    public void updateNotify(){
        getViewState().updateNotify(mNote.getNotify());
    }

    public void nofifyStatusChanged(boolean isChecked){
        mNote.setNotify(isChecked);
    }

    public UUID getId() {
        return mNote.getId();
    }
}
