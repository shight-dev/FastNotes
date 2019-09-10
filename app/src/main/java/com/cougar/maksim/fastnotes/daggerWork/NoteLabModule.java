package com.cougar.maksim.fastnotes.daggerWork;

import android.content.Context;

import com.cougar.maksim.fastnotes.dbWork.NoteLab;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class NoteLabModule {

    @Provides
    @Singleton
    public NoteLab provideNoteLab(Context context){
        return new NoteLab(context);
    }
}
