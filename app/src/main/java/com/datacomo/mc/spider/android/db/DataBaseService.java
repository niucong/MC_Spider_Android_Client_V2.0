package com.datacomo.mc.spider.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DataBaseService {

    private DataBaseHelper dbOpenHelper;

    protected SQLiteDatabase wDB = null;
    protected SQLiteDatabase rDB = null;

    public DataBaseService(Context context) {
        dbOpenHelper = DataBaseHelper.getHelper(context);
    }

    public SQLiteDatabase openWDB() {
        wDB = dbOpenHelper.getWritableDatabase();
        return wDB;
    }

    public SQLiteDatabase openRDB() {
        rDB = dbOpenHelper.getReadableDatabase();
        return rDB;
    }

}
