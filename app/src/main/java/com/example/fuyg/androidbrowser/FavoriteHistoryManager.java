package com.example.fuyg.androidbrowser;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fuyg.androidbrowser.database.Callback;
import com.example.fuyg.androidbrowser.database.IDatabase;
import com.example.fuyg.androidbrowser.database.SqlManager;
import com.example.fuyg.androidbrowser.database.SqlStr;

/**
 * Created by fuyg on 20/01/2017.
 */

public class FavoriteHistoryManager {

    private static final String TAG = "FavoriteHistoryManager";

    private IDatabase database;
    private boolean flag = false;
    private Cursor resultCursor;

    public FavoriteHistoryManager(Context context) {
        database = new SqlManager(context, SqlStr.FAVORITE_HISTORY_DB_NAME, null, 1);
    }

    public boolean addFavorite(final String name, final String url) {
        if (url == null || url.isEmpty() || name == null ||name.isEmpty()) {
            return false;
        }

        flag = false;
        database.transactionAround(false, new Callback() {
            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                boolean hasMultiple = database.isExist(sqLiteDatabase, SqlStr.TABLE_FAVORITE_NAME, url);
                if (hasMultiple) {
                    Log.d(TAG, "url already in data base: " + url);
                    flag = false;
                } else {
                    flag = database.add(sqLiteDatabase, SqlStr.TABLE_FAVORITE_NAME, name, url, 0);
                }
            }
        });
        return flag;
    }

    public boolean deleteFavorite(final String id) {
        flag = false;

        database.transactionAround(false, new Callback() {
            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                flag = database.delete(sqLiteDatabase, SqlStr.TABLE_FAVORITE_NAME, id);
            }
        });

        return flag;
    }

    public boolean modifyFavorite(final String id, final String name, final String url) {
        if (url == null || url.isEmpty() || name == null ||name.isEmpty()) {
            return false;
        }
        flag = false;
        database.transactionAround(false, new Callback() {
            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
               flag = database.modify(sqLiteDatabase, SqlStr.TABLE_FAVORITE_NAME, id, name, url);
            }
        });

        return flag;
    }

    public Cursor getAllFavority() {
        database.transactionAround(true, new Callback() {
            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                resultCursor = database.getAll(sqLiteDatabase, SqlStr.TABLE_FAVORITE_NAME);
            }
        });

        return resultCursor;
    }

    public boolean addHistory(final String name, final String url, final long date) {
        if (url == null || url.isEmpty() || name == null || name.isEmpty()) {
            return false;
        }

        flag = false;

        database.transactionAround(false, new Callback() {
            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                boolean isExist = database.isExist(sqLiteDatabase, SqlStr.TABLE_HISTORY_NAME, url);
                if (isExist) {
                    Log.d(TAG, "url is already in data base: " + url);
                    flag = false;
                } else {
                    flag = database.add(sqLiteDatabase, SqlStr.TABLE_HISTORY_NAME, name, url, date);
                }
            }
        });

        return flag;
    }

    public boolean deleteHistory(final String id) {
        flag = false;

        database.transactionAround(false, new Callback() {
            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                flag = database.delete(sqLiteDatabase, SqlStr.TABLE_HISTORY_NAME, id);
            }
        });

        return flag;
    }

    public boolean deleteAllHistory() {
        flag = false;

        database.transactionAround(false, new Callback() {
            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                flag = database.deleteAll(sqLiteDatabase, SqlStr.TABLE_HISTORY_NAME);
            }
        });
        return flag;
    }

    public Cursor getAllHistory() {

        database.transactionAround(true, new Callback() {
            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                resultCursor = database.getAll(sqLiteDatabase, SqlStr.TABLE_HISTORY_NAME);
            }
        });

        return resultCursor;
    }

}
