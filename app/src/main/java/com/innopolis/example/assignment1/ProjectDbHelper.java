package com.innopolis.example.assignment1;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.innopolis.example.assignment1.ProjectContract.ProjectEntry;

public class ProjectDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Projects.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ProjectEntry.TABLE_NAME + " (" +
                    ProjectEntry._ID + " INTEGER PRIMARY KEY," +
                    ProjectEntry.COLUMN_NAME_TITLE + " TEXT," +
                    ProjectEntry.COLUMN_NAME_IMAGE + " INTEGER," +
                    ProjectEntry.COLUMN_NAME_DESCRIPTION + " TEXT" +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ProjectEntry.TABLE_NAME;

    public ProjectDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onClean(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}