package com.innopolis.example.assignment1;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ProjectContract{

    public ProjectContract(){}

    public static abstract class ProjectEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://" +
                ProjectContentProvider.AUTHORITY + "/projects");

        public static final String TABLE_NAME = "projects";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_RATING = "rate";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    public static void addProject(Context context, String title, String author, String link, String desc, int rating) {
        final ContentResolver resolver = context.getContentResolver();

        final ContentValues values = new ContentValues();
        values.put(ProjectEntry.COLUMN_NAME_TITLE, title);
        values.put(ProjectEntry.COLUMN_NAME_AUTHOR, author);
        values.put(ProjectEntry.COLUMN_NAME_LINK, link);
        values.put(ProjectEntry.COLUMN_NAME_DESCRIPTION, desc);
        //values.put(ProjectEntry.COLUMN_NAME_RATING, rating);

        resolver.insert(ProjectEntry.CONTENT_URI, values);
    }

    public static Cursor getProject(Context context, int i) {
        final ContentResolver resolver = context.getContentResolver();

        String[] mProjection =
                {
                        ProjectEntry.COLUMN_NAME_TITLE,
                        ProjectEntry.COLUMN_NAME_AUTHOR,
                        ProjectEntry.COLUMN_NAME_LINK,
                        ProjectEntry.COLUMN_NAME_DESCRIPTION,
                        //ProjectEntry.COLUMN_NAME_RATING,
                };

        return resolver.query(
                ProjectEntry.CONTENT_URI,
                mProjection,
                null,
                null,
                null);
    }

    public static void removeProject(Context context, long i) {
        final ContentResolver resolver = context.getContentResolver();

        resolver.delete(ProjectEntry.CONTENT_URI, ProjectEntry._ID + "=?",
                new String[]{String.valueOf(i)});
    }
}
