package com.innopolis.example.assignment1;

import android.provider.BaseColumns;

public final class ProjectContract{

    public ProjectContract(){}

    public static abstract class ProjectEntry implements BaseColumns {

        public static final String TABLE_NAME = "projects";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }
}
