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

public class FavoriteManager {

    private static final String TAG = "FavoriteManager";

    private IDatabase database;
    private boolean flag = false;
    private Cursor resultCursor;

    public FavoriteManager(Context context) {
        database = new SqlManager(context, "favorite_db", null, 1);
    }

    public boolean addFavorite(final String name, final String url) {
        flag = false;
        database.transactionAround(false, new Callback() {
            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                boolean hasMultiple = database.multiplyFavorite(sqLiteDatabase, url);
                if (hasMultiple) {
                    Log.d(TAG, "url already in data base: " + url);
                    flag = false;
                } else {
                    flag = database.addFavorite(sqLiteDatabase, name, url);
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
                flag = database.deleteFavorite(sqLiteDatabase, id);
            }
        });

        return flag;
    }

    public boolean modifyFavorite(final String id, final String name, final String url) {
        flag = false;
        database.transactionAround(false, new Callback() {
            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
               flag = database.modifyFavorite(sqLiteDatabase, id, name, url);
            }
        });

        return flag;
    }

    public Cursor getAllFavorites () {
        database.transactionAround(true, new Callback() {
            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                resultCursor = database.getAllFavorite(sqLiteDatabase);
            }
        });

        return resultCursor;
    }
}
