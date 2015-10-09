package com.innopolis.example.assignment1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ProjectContentProvider extends ContentProvider {

    private SQLiteDatabase db;

    public static final String AUTHORITY = "ru.innopolis.project.data";

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, ProjectContract.ProjectEntry.TABLE_NAME, 1);
        sUriMatcher.addURI(AUTHORITY, UserContract.UserEntry.TABLE_NAME, 2);
    }

    public ProjectContentProvider() { }

    @Override
    public boolean onCreate() {
        final ProjectDbHelper dbHelper = new ProjectDbHelper(getContext());
        db = dbHelper.getWritableDatabase();
        return !db.isReadOnly();
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case 1:
                return ProjectContract.ProjectEntry.TABLE_NAME;
            case 2:
                return UserContract.UserEntry.TABLE_NAME;
        }
        return "";
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int deletedEntries = db.delete(getType(uri), selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedEntries;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final long id = db.insert(getType(uri), null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return db.query(getType(uri), projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final int updatedEntries = db.update(getType(uri), values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedEntries;
    }

}
