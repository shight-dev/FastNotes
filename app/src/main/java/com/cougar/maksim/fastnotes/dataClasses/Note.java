package com.cougar.maksim.fastnotes.dataClasses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

@Entity
@TypeConverters({Converters.class})
public class Note {

    @PrimaryKey
    @ColumnInfo(name = "uuid")
    @NotNull
    private UUID id;
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "data")
    private String mData;

    @ColumnInfo(name = "notify")
    private Boolean mNotify;

    @Ignore
    public Note() {
        id =UUID.randomUUID();
    }

    public Note(@NonNull UUID id){
        this.id = id;
}

    public String getTitle() {
        return mTitle;
    }

    public String getData() {
        return mData;
    }

    public UUID getId() {
        return id;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setData(String data) {
        mData = data;
    }

    public Boolean getNotify() {
        return mNotify;
    }

    public void setNotify(Boolean mNotify) {
        this.mNotify = mNotify;
    }
}
