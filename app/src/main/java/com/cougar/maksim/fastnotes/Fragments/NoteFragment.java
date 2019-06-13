package com.cougar.maksim.fastnotes.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cougar.maksim.fastnotes.ContractClasses.NoteContract;
import com.cougar.maksim.fastnotes.ContractClasses.NotePresenter;
import com.cougar.maksim.fastnotes.R;

import java.util.UUID;

public class NoteFragment extends Fragment implements NoteContract.View {

    private EditText mTitleField;
    private EditText mDataField;
    private Button mSaveButton;
    private Button mPickDateBtn;
    private Button mSetStatusBtn;

    private NotePresenter mNotePresenter;

    private static final String NEW_ITEM = "NEW_ITEM";
    private static final String NOTE_ID = "NOTE_ID";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_STATUS = "DialogStatus";
    public static final int REQUEST_DATE = 0;
    public static final int REQUEST_STATUS = 1;


    public static NoteFragment newInstance(boolean isNewItem){
        Bundle bundle=new Bundle();
        bundle.putBoolean(NEW_ITEM, isNewItem);

        NoteFragment noteFragment = new NoteFragment();
        noteFragment.setArguments(bundle);
        return noteFragment;
    }

    public static NoteFragment newInstance(UUID id){
        Bundle bundle=new Bundle();
        bundle.putSerializable(NOTE_ID, id);

        NoteFragment noteFragment = new NoteFragment();
        noteFragment.setArguments(bundle);
        return noteFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotePresenter = new NotePresenter(this);
        if(getArguments()!=null) {
            boolean isNewItem = getArguments().getBoolean(NEW_ITEM);
            UUID noteId = (UUID) getArguments().getSerializable(NOTE_ID);

            mNotePresenter.setData(isNewItem, noteId);
        }
        else {
            throw new RuntimeException("No args exception");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.note_fragment, container, false);
        mTitleField = v.findViewById(R.id.note_title);
        mDataField = v.findViewById(R.id.note_data);
        mSaveButton = v.findViewById(R.id.saveBtn);
        mPickDateBtn = v.findViewById(R.id.setDateBtn);
        mSetStatusBtn = v.findViewById(R.id.setStatusBtn);

        mNotePresenter.updateTitleView();
        mNotePresenter.updateDataView();
        mNotePresenter.updateDateView();
        mNotePresenter.updateStatusView();

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNotePresenter.updateNoteTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //nothing here
            }
        });

        mDataField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNotePresenter.updateNoteContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO relocate to presenter
                saveDataAndExit();
            }
        });
        mPickDateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                if(fragmentManager!=null) {
                    DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mNotePresenter.getDate());
                    datePickerFragment.setTargetFragment(NoteFragment.this, REQUEST_DATE);
                    datePickerFragment.show(fragmentManager, DIALOG_DATE);
                }
            }
        });
        mSetStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                if(fragmentManager != null){
                    //TODO open enter status fragment
                    StatusFragment statusFragment =StatusFragment.Companion.newInstance(mNotePresenter.getStatus());
                    statusFragment.setTargetFragment(NoteFragment.this, REQUEST_STATUS);
                    statusFragment.show(fragmentManager, DIALOG_STATUS);
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if(resultCode !=Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mNotePresenter.updateNoteDate(date);
            mNotePresenter.updateDateView();
        }*/
        mNotePresenter.onActivityResult(requestCode, resultCode, data);
    }

    /*@Override
    public void onPause() {
        super.onPause();
        NoteLab.get(getActivity()).updateNote(mNote);
    }*/

    public void saveDataAndExit() {
        mNotePresenter.updateNote();
        Intent intent = new Intent();
        intent.putExtra("id", mNotePresenter.getId());
        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
    }

    @Override
    public void updateDateBtn(String s) {
        mPickDateBtn.setText(s);
    }

    @Override
    public void updateStatusBtn(String s) {
        mSetStatusBtn.setText(s);
    }

    @Override
    public void updateTitle(String s) {
        mTitleField.setText(s);
    }

    @Override
    public void updateData(String s) {
        mDataField.setText(s);
    }
}
