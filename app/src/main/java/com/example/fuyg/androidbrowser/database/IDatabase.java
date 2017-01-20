package com.example.fuyg.androidbrowser.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by fuyg on 20/01/2017.
 */

public interface IDatabase {

    public boolean addFavorite(SQLiteDatabase sqLiteDatabase, String name, String url);

    public boolean deleteFavorite(SQLiteDatabase sqLiteDatabase, String id);

    public boolean modifyFavorite(SQLiteDatabase sqLiteDatabase, String id, String name, String url);

    public Cursor getAllFavorite(SQLiteDatabase sqLiteDatabase);

    public boolean multiplyFavorite(SQLiteDatabase sqLiteDatabase, String url);

    void transactionAround(boolean readOnly, Callback callback);

}
