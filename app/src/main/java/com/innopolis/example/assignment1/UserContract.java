package com.innopolis.example.assignment1;

import android.provider.BaseColumns;

public final class UserContract {

    private UserContract() {}

    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_LOGIN = "login";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_EMAIL = "email";
    }
}