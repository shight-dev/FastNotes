package com.cougar.maksim.fastnotes.daggerWork;

import android.app.Application;

public class App extends Application {

    private static AppComponent component;

    public static AppComponent getComponent() {
        return component;
    }

    /*protected AppComponent builderComponent() {
        //TODO add dependency daggerpocesssor or something else
        return DaggerAppComponent.builder()
                .noteLabModule(new NoteLabModule())
                .build();
    }*/
}
