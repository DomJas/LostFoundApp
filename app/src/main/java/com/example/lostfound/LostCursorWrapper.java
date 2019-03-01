package com.example.lostfound;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

public class LostCursorWrapper extends CursorWrapper {
    public LostCursorWrapper(Cursor cursor) {
        super(cursor);
        //This created a wrap about Cursor and it calls those methods that do the exact same thing, but it also allows to make new methods
    }

    public Lost getLost(){
        String uuidString = getString(getColumnIndex(LostDbSchema.LostTable.Cols.UUID));
        String title = getString(getColumnIndex(LostDbSchema.LostTable.Cols.TITLE));
        long date = getLong(getColumnIndex(LostDbSchema.LostTable.Cols.DATE));
        int isFound = getInt(getColumnIndex(LostDbSchema.LostTable.Cols.FOUND));
        String suspect = getString(getColumnIndex(LostDbSchema.LostTable.Cols.SUSPECT));

        Lost lost = new Lost(UUID.fromString(uuidString));
        lost.setTitle(title);
        lost.setDate(new Date(date));
        lost.setFound(isFound !=0);
        lost.setSuspect(suspect);

        return lost;

        //getLost() method pulls out relevant data from the column
    }


}
