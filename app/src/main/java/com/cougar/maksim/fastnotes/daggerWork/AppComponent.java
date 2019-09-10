package com.cougar.maksim.fastnotes.daggerWork;

import com.cougar.maksim.fastnotes.presenters.NoteListPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {NoteLabModule.class, ContextModule.class})
@Singleton
public interface AppComponent {
    //TODO добавить все используемые классы

    void inject(NoteListPresenter noteListPresenter);
}
