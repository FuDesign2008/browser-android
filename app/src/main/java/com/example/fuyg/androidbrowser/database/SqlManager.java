package com.example.fuyg.androidbrowser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by fuyg on 20/01/2017.
 */

public class SqlManager extends SQLiteOpenHelper implements IDatabase {

    private static final String TAG = "webbrowser_SqlManager";

    public SqlManager(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SqlStr.CTEATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO
    }

    @Override
    public boolean addFavorite(SQLiteDatabase sqLiteDatabase, String name, String url) throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("url", url);

        long id = sqLiteDatabase.insert(SqlStr.TABLE_FAVORITE_NAME, null, contentValues);
        return id >-1;
    }

    @Override
    public boolean deleteFavorite(SQLiteDatabase sqLiteDatabase, String id) {
        Log.d(TAG, "delete id: " + id);
        int count = sqLiteDatabase.delete(SqlStr.TABLE_FAVORITE_NAME, "id=?", new String[]{id});
        Log.d(TAG, "delete id: " + id + " result: " + count);

        return count > 0;
    }

    @Override
    public boolean modifyFavorite(SQLiteDatabase sqLiteDatabase, String id, String name, String url) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("url", url);
        Log.d(TAG, "modify id:" + id + " name: " + name + " url: " + url);
        int count = sqLiteDatabase.update(SqlStr.TABLE_FAVORITE_NAME, contentValues, "id=?", new String[]{id});
        return count > 0;
    }

    @Override
    public Cursor getAllFavorite(SQLiteDatabase sqLiteDatabase) {
        String[] returnColumns = new String[] {
                "id as _id",
                "name",
                "url"
        };
        Cursor result = sqLiteDatabase.query(SqlStr.TABLE_FAVORITE_NAME, returnColumns, null, null, null, null, "id");

        while (result.moveToNext()) {
            long id = result.getInt(result.getColumnIndex("_id"));
            String name = result.getString(result.getColumnIndex("name"));
            String url = result.getString(result.getColumnIndex("url"));

            Log.d(TAG, "id: " + id + ", name: " + name + ", url:" + url);
        }

        return result;
    }

    @Override
    public boolean multiplyFavorite(SQLiteDatabase sqLiteDatabase, String url) {
        Cursor cursor = sqLiteDatabase.query(SqlStr.TABLE_FAVORITE_NAME, null, "url=?", new  String[]{url}, null, null, null);

        while (cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String urlStr = cursor.getString(cursor.getColumnIndex("url"));

            Log.d(TAG, "id / name / url : " + id + " / " + name + " / " + urlStr);
        }

        boolean isMultiple = cursor.getCount() > 0;
        cursor.close();
        return isMultiple;
    }

    @Override
    public void transactionAround(boolean readOnly, Callback callback) {
        SQLiteDatabase sqLiteDatabase = null;

        if (readOnly) {
            sqLiteDatabase = getReadableDatabase();
        } else {
            sqLiteDatabase = getWritableDatabase();
        }
        Log.d(TAG, "sqlite --->" + sqLiteDatabase.toString());
        sqLiteDatabase.beginTransaction();
        callback.doSomething(sqLiteDatabase);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }
}
