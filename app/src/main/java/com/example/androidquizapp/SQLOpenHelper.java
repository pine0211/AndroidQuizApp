package com.example.androidquizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//SQLiteで作成する点数管理用データベースヘルパー

class SQLOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=3;

    private static final String DATABASE_NAME="ResultDB.db";
    private static final String TABLE_NAME="resultdb";
    private static final String _ID="_id";
    private static final String COLUMN_NAME_TITLE="title";
    private static final String COLUMN_NAME_SUBTITLE="score";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_SUBTITLE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    SQLOpenHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);

        //デフォルトを-1にすることで未回答を判定する。
        saveData(db,"地理問題：",-1);
        saveData(db,"計算問題：",-1);
        saveData(db,"画像問題：",-1);
        saveData(db,"音当て問題：",-1);
        saveData(db,"合計得点：",-1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    private void saveData(SQLiteDatabase db, String title, int score){
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("score", score);

        db.insert("resultdb", null, values);
    }
}
