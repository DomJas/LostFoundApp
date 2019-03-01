package com.example.lostfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lostfound.LostDbSchema.LostTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class LostLab {
    private static LostLab sLostLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static LostLab get(Context context) {
        if(sLostLab == null) {
            sLostLab = new LostLab(context);
        }

        return sLostLab;
    }

    private LostLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new LostBaseHelper(mContext).getWritableDatabase();
        //This SQHelper removes some of the workload from SQLite
        }

    public void addLost(Lost l) {
        ContentValues values = getContentValues(l);

        mDatabase.insert(LostTable.NAME, null, values);
        //The "+" button adds new crimes using this
    }

    public List<Lost> getLosts() {
        List<Lost> losts = new ArrayList<>();

        LostCursorWrapper cursor = querryLosts(null, null);

        try {
            cursor.moveToFirst();
            while ( !cursor.isAfterLast()) {
                losts.add(cursor.getLost());
                cursor.moveToNext();
            }
        } finally {
                cursor.close();
        }
        return  losts;

        //This code calls my database and pulls the data out of cursors and moves it to the first element by using moveToFirst and then moveToNext until isAfterLast tells you that your pointer is off the end of the data
    }

    //This allows you to add lost to LostLabs and when you press the "New Lost" action item it now works
    // Also allows LostPagerActivity to see all the Losts in LostLabs

    public Lost getLost(UUID id) {
        LostCursorWrapper cursor = querryLosts(
                LostTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {

            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getLost();
        } finally {
            cursor.close();
        }
    }

    //The UUID works too so each LostFragment displayed in LostPagerActivity is showing the real Lost

    private static ContentValues getContentValues(Lost lost) {
        ContentValues values = new ContentValues();
        values.put(LostTable.Cols.UUID, lost.getId().toString());
        values.put(LostTable.Cols.TITLE, lost.getTitle());
        values.put(LostTable.Cols.DATE, lost.getDate().getTime());
        values.put(LostTable.Cols.FOUND, lost.isFound() ? 1 : 0);
        values.put(LostTable.Cols.SUSPECT, lost.getSuspect());

        return values;
//This is a content values chart and they write and update the database
    }

    public void updateLost(Lost lost) {
        String uuidString = lost.getId().toString();
        ContentValues values = getContentValues(lost);

        mDatabase.update(LostTable.NAME, values, LostTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    private LostCursorWrapper querryLosts(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                LostTable.NAME, null, whereClause, whereArgs, null, null, null
                //The table argument is the table to query. It asks the database to bring the information in a specific value and what order you want to receive them in.
        );

        return new LostCursorWrapper(cursor);
    }

}

//Removed a bunch of mLosts because I have a database set up now
