package com.example.fuyg.androidbrowser.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by fuyg on 20/01/2017.
 */

public interface IDatabase {

    public boolean add(SQLiteDatabase sqLiteDatabase, String tableName, String name, String url, long date);

    public boolean delete(SQLiteDatabase sqLiteDatabase, String tableName, String id);

    public boolean deleteAll(SQLiteDatabase sqLiteDatabase, String tableName);

    public boolean modify(SQLiteDatabase sqLiteDatabase, String tableName, String id, String name, String url);

    public Cursor getAll(SQLiteDatabase sqLiteDatabase, String tableName);

    public boolean isExist(SQLiteDatabase sqLiteDatabase, String tableName, String url);

    void transactionAround(boolean readOnly, Callback callback);


}
