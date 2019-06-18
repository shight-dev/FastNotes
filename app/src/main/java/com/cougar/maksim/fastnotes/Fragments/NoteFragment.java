package com.cougar.maksim.fastnotes.Fragments;

import android.app.Activity;
import android.content.Context;
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

import static com.cougar.maksim.fastnotes.Activities.NoteActivity.EXTRA_NEW_ITEM;
import static com.cougar.maksim.fastnotes.Activities.NoteActivity.EXTRA_NOTE_ID;

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

    private OnNoteFragmentInteractionListener listener = null;

    /*public static Intent newIntent(UUID noteId){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        return intent;
    }

    public static Intent newItemIntent(){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NEW_ITEM, true);
        return intent;
    }*/

    public static NoteFragment newInstance(boolean isNewItem) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(NEW_ITEM, isNewItem);

        NoteFragment noteFragment = new NoteFragment();
        noteFragment.setArguments(bundle);
        return noteFragment;
    }

    public static NoteFragment newInstance(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(NOTE_ID, id);

        NoteFragment noteFragment = new NoteFragment();
        noteFragment.setArguments(bundle);
        return noteFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotePresenter = new NotePresenter(this);
        if (getArguments() != null) {
            boolean isNewItem = getArguments().getBoolean(NEW_ITEM);
            UUID noteId = (UUID) getArguments().getSerializable(NOTE_ID);

            mNotePresenter.setData(isNewItem, noteId);
        } else {
            throw new RuntimeException("No args exception");
        }
    }

    //установка данных в компоненты и установка слушателей
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
                mNotePresenter.setNoteTitle(s.toString());
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
                mNotePresenter.setNoteContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotePresenter.updateNote();
                saveDataAndExit();
            }
        });
        mPickDateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
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
                if (fragmentManager != null) {
                    StatusFragment statusFragment = StatusFragment.Companion.newInstance(mNotePresenter.getStatus());
                    statusFragment.setTargetFragment(NoteFragment.this, REQUEST_STATUS);
                    statusFragment.show(fragmentManager, DIALOG_STATUS);
                }
            }
        });

        return v;
    }

    //вызывается дочерними фрагментами
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mNotePresenter.onActivityResult(requestCode, resultCode, data);
    }

    public void saveDataAndExit() {
        /*Intent intent = new Intent();
        intent.putExtra("id", mNotePresenter.getId());
        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }*/
        listener.onNoteFragmentInteraction();
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

    public interface OnNoteFragmentInteractionListener {
        void onNoteFragmentInteraction();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnNoteFragmentInteractionListener) {
            listener = (OnNoteFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
