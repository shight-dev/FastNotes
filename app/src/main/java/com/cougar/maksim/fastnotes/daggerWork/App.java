package com.cougar.maksim.fastnotes.daggerWork;

import android.app.Application;
import android.content.Context;

public class App extends Application {


    private static Context context;

    private static AppComponent component;

    public static AppComponent getComponent() {
        return component;
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .noteLabModule(new NoteLabModule())
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
