package com.cougar.maksim.fastnotes.DataClasses;

import java.util.Date;
import java.util.UUID;

public class Note {

    private UUID mId;
    private String mTitle;
    private String mData;
    private Date mDate;
    private NoteStatus mStatus;

    public Note() {
        mId=UUID.randomUUID();
    }

    public Note(UUID id){
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getData() {
        return mData;
    }

    public Date getDate() {
        return mDate;
    }

    public NoteStatus getStatus() {
        return mStatus;
    }

    public UUID getId() {
        return mId;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setData(String data) {
        mData = data;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public void setStatus(NoteStatus mStatus) {
        this.mStatus = mStatus;
    }
}
