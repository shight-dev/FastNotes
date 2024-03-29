package com.cougar.maksim.fastnotes.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.cougar.maksim.fastnotes.presenters.NoteListPresenter;
import com.cougar.maksim.fastnotes.dataClasses.Note;
import com.cougar.maksim.fastnotes.R;
import com.cougar.maksim.fastnotes.mvpMoxyViews.NoteListView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class NoteListFragment extends MvpAppCompatFragment implements NoteListView {

    /*@Inject
    NoteLab noteLab;*/

    private RecyclerView mNoteRecyclerView;
    private NoteAdapter mAdapter;

    private Menu mMenu;

    public static final int UPDATE_NOTE = 111;
    public static final int CREATE_NOTE = 222;
    public static final int DELETE_NOTE = 333;
    public static final String TODAY_EVENTS = "today_events";
    public static final String DELETE_ITEM = "delete_item";

    @InjectPresenter
    public NoteListPresenter mNoteListPresenter;

    private OnNoteListFragmentInteractionListener listener = null;

    public static NoteListFragment newInstance() {
        return new NoteListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoteListPresenter.startNotifications();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_list_fragment, container, false);

        mNoteRecyclerView = view.findViewById(R.id.note_recycler_view);
        mNoteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note_list_menu, menu);
        //TODO проблемы с сохранением состояния меню
        //mMenu = menu;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mMenu = menu;
        //mNoteListPresenter.updateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_note: {
                listener.onNoteListFragmentInteraction(null);
                break;
            }
            case R.id.menu_item_today_events: {
                mNoteListPresenter.changeTodayEvents();
                updateUI();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setMenuItemDelete() {
        if (mMenu != null) {
            mMenu.findItem(R.id.menu_item_today_events).setIcon(android.R.drawable.ic_delete);
        }
    }

    @Override
    public void setMenuItemSearch() {
        //TODO выплняется два раза при запуске фрагмента
        //TODO возможно moxy выполняет команду при пересоздании фрагмента перед сохранением меню в поле класса
        if (mMenu != null) {
            mMenu.findItem(R.id.menu_item_today_events).setIcon(android.R.drawable.ic_menu_search);
        }
    }

    @Override
    public void updateAdapterElement(int position) {
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void updateAdapterDataset() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void updateUI() {
        //TODO move to presenter
        List<Note> notes = mNoteListPresenter.getNotes();
        mNoteListPresenter.updateMenu();

        if (mAdapter == null) {
            mAdapter = new NoteAdapter(notes);
            mNoteRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setNotes(notes);
            //change presenter update state
            mNoteListPresenter.updateDataset(notes);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mNoteListPresenter.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mNoteListPresenter.onActivityResult(requestCode, resultCode, data);
        //TODO проверять result code
        if(requestCode == DELETE_NOTE) {
            UUID id = (UUID) data.getSerializableExtra("id");
            listener.closeEditFragmentIfExist(id);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(TODAY_EVENTS, mNoteListPresenter.getTodayEvents());
    }

    private class NoteHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView mTitleTextView;
        public TextView mContentNoteTextView;
        public ImageButton mDeleteItemBtn;

        private Note mNote;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.list_item_note_title);
            mContentNoteTextView = itemView.findViewById(R.id.list_item_note_text);
            mDeleteItemBtn = itemView.findViewById(R.id.list_item_del_btn);
            itemView.setOnClickListener(this);
        }

        public void bindNote(Note note) {
            mNote = note;
            mTitleTextView.setText(note.getTitle());
            mContentNoteTextView.setText(note.getData());

            mDeleteItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    if (fragmentManager != null) {
                        DeleteItemFragment deleteItemFragment = DeleteItemFragment.Companion.newInstance(mNote.getId());
                        deleteItemFragment.setTargetFragment(NoteListFragment.this, DELETE_NOTE);
                        deleteItemFragment.show(fragmentManager, DELETE_ITEM);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            //TODO подумать над сохранением фрагментов и передачей в них информации об событиях
            if (listener != null) {
                listener.onNoteListFragmentInteraction(mNote.getId());
            }
        }
    }

    public class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {

        private List<Note> mNotes;

        public NoteAdapter(List<Note> notes) {
            mNotes = notes;
        }

        public void setNotes(List<Note> notes) {
            mNotes = notes;
        }

        @NonNull
        @Override
        public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_note, viewGroup, false);
            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
            Note note = mNotes.get(position);
            holder.bindNote(note);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }
    }

    public interface OnNoteListFragmentInteractionListener {
        void onNoteListFragmentInteraction(UUID id);
        void closeEditFragmentIfExist(UUID id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteListFragmentInteractionListener) {
            listener = (OnNoteListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
