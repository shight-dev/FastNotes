package com.cougar.maksim.fastnotes.daggerWork;

import com.cougar.maksim.fastnotes.activities.NoteActivity;
import com.cougar.maksim.fastnotes.activities.NoteListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = NoteLabModule.class)
@Singleton
public interface AppComponent {
    //TODO rework with fragments
    void inject(NoteListActivity noteListActivity);
    void inject(NoteActivity noteActivity);
}
