package com.cougar.maksim.fastnotes.daggerWork;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Context context;

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

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    //возвращает глобальный контекст
    public static Context getAppContext(){
        return context;
    }
}
