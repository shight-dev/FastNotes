package com.cougar.maksim.fastnotes.DaggerWork;

import android.content.Context;

import com.cougar.maksim.fastnotes.DbWork.NoteLab;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NoteLabModule {

    @Provides
    @Singleton
    public NoteLab provideNoteLab(Context context){
        return new NoteLab(context);
    }
}
