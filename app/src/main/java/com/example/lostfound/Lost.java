package com.example.lostfound;

import java.util.Date;
import java.util.UUID;

public class Lost {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mFound;
    private String mSuspect;

    public Lost() {
        this(UUID.randomUUID());
    }

    public  Lost(UUID id) {
        mId = id;
        mDate = new Date();
    }
        //Creates random UUID every time you create a Crime


    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isFound() {
        return mFound;
    }

    public void setFound(boolean found) {
        mFound = found;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void  setSuspect(String suspect) {
        mSuspect = suspect;

        //This will hold the name of the suspect
    }
}
