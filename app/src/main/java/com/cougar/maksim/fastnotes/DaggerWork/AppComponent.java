package com.cougar.maksim.fastnotes.DaggerWork;

import com.cougar.maksim.fastnotes.Activities.NoteActivity;
import com.cougar.maksim.fastnotes.Activities.NoteListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = NoteLabModule.class)
@Singleton
public interface AppComponent {
    //TODO rework with fragments
    void inject(NoteListActivity noteListActivity);
    void inject(NoteActivity noteActivity);
}
