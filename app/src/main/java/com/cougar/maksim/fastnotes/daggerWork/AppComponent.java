package com.cougar.maksim.fastnotes.daggerWork;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = NoteLabModule.class)
@Singleton
public interface AppComponent {
    //TODO использовать Dagger2
}
