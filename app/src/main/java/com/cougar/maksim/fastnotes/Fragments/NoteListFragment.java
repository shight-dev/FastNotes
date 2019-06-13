package com.cougar.maksim.fastnotes.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.cougar.maksim.fastnotes.Activities.NoteActivity;
import com.cougar.maksim.fastnotes.ContractClasses.NoteListContract;
import com.cougar.maksim.fastnotes.ContractClasses.NoteListPresenter;
import com.cougar.maksim.fastnotes.DataClasses.Note;
import com.cougar.maksim.fastnotes.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NoteListFragment extends Fragment implements NoteListContract.View {

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

    private NoteListPresenter mNoteListPresenter;

    public static NoteListFragment newInstance(boolean todayEvents){
        Bundle bundle = new Bundle();
        bundle.putBoolean(TODAY_EVENTS, todayEvents);

        NoteListFragment noteListFragment=new NoteListFragment();
        noteListFragment.setArguments(bundle);
        return noteListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoteListPresenter = new NoteListPresenter(this);
        //App.getComponent().inject((NoteListActivity) getActivity());
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            boolean todayEvents = savedInstanceState.getBoolean(TODAY_EVENTS, false);
            mNoteListPresenter.setTodayEvents(todayEvents);

        }
        if(getArguments()!=null) {
            boolean todayEvents = getArguments().getBoolean(TODAY_EVENTS);
            mNoteListPresenter.setTodayEvents(todayEvents);
        }

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
        mMenu = menu;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mNoteListPresenter.updateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_note: {
                Intent intent = NoteActivity.newItemIntent(getActivity());
                startActivityForResult(intent, CREATE_NOTE);
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
        mMenu.findItem(R.id.menu_item_today_events).setIcon(android.R.drawable.ic_delete);
    }

    @Override
    public void setMenuItemSearch() {
        mMenu.findItem(R.id.menu_item_today_events).setIcon(android.R.drawable.ic_menu_search);
    }

    @Override
    public void updateAdapterElement(int position) {
        if(mAdapter!=null){
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void updateAdapterDataset() {
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
    }

    public void updateUI() {
        List<Note> notes = mNoteListPresenter.getNotes();

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
        public TextView mDateTextView;
        public ImageButton mDeleteItemBtn;

        private Note mNote;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.list_item_note_title);
            mContentNoteTextView = itemView.findViewById(R.id.list_item_note_text);
            mDateTextView = itemView.findViewById(R.id.list_item_note_date);
            mDeleteItemBtn = itemView.findViewById(R.id.list_item_del_btn);
            itemView.setOnClickListener(this);
        }

        public void bindNote(Note note) {
            mNote = note;
            mTitleTextView.setText(note.getTitle());
            mContentNoteTextView.setText(note.getData());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
            mDateTextView.setText(dateFormat.format(note.getDate()));
            mDeleteItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    if(fragmentManager!=null) {
                        DeleteItemFragment deleteItemFragment = DeleteItemFragment.newInstance(mNote.getId());
                        deleteItemFragment.setTargetFragment(NoteListFragment.this, DELETE_NOTE);
                        deleteItemFragment.show(fragmentManager, DELETE_ITEM);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            Intent intent = NoteActivity.newIntent(getActivity(), mNote.getId());
            startActivityForResult(intent, UPDATE_NOTE);
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
}
