package com.example.fuyg.androidbrowser.database;

/**
 * Created by fuyg on 20/01/2017.
 */

public class SqlStr {


    public static final String FAVORITE_HISTORY_DB_NAME = "favorite_history.db";
    public static final String TABLE_FAVORITE_NAME = "favorite";
    public static final String TABLE_HISTORY_NAME = "history";
    public static final String CTEATE_TABLE_FAVORITE = "CREATE TABLE " + TABLE_FAVORITE_NAME + "(id INTEGER PRIMARY KEY, name TEXT NOT NULL, url TEXT NOT NULL)";
    public static final String CTEATE_TABLE_HISTORY = "CREATE TABLE " + TABLE_HISTORY_NAME + "(id INTEGER PRIMARY KEY, name TEXT NOT NULL, url TEXT NOT NULL, date LONG NOT NULL)";

}
