package com.example.lostfound;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lostfound.LostDbSchema.LostTable;

public class LostBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "lostBase.db";

    public  LostBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + LostTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                LostTable.Cols.UUID + ", " +
                LostTable.Cols.TITLE + ", " +
                LostTable.Cols.DATE + ", " +
                LostTable.Cols.FOUND + ", " +
                LostTable.Cols.SUSPECT +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase dc, int oldVersion, int newVersion) {
    }
}