package com.example.coursework;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    // URI for ContentProvider
    public static final String CONTENT_AUTHORITY = "com.example.coursework";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class Goal_Table implements BaseColumns {
        public static final String TABLE_NAME = "GOAL_TABLE";
        public static final String COLUMN_NAME = "GOAL_NAME";
        public static final String COLUMN_DESCRIPTION = "GOAL_DESCRIPTION";
        public static final String COLUMN_PROGRESS = "GOAL_PROGRESS";
        public static final String COLUMN_TYPE = "GOAL_TYPE";
        public static final String COLUMN_PARENT = "GOAL_PARENT";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + Goal_Table.TABLE_NAME;
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + Goal_Table.TABLE_NAME;

        public static Uri buildWithID(long ID) {
            return ContentUris.withAppendedId(CONTENT_URI, ID);
        }
    }

    public static class Type_Table implements BaseColumns {
        public static final String TABLE_NAME = "TYPE_TABLE";
        public static final String COLUMN_TYPE = "TYPE_TYPE";
        public static final String COLUMN_COLOUR = "TYPE_COLOUR";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + Goal_Table.TABLE_NAME;
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + Goal_Table.TABLE_NAME;

        public static Uri buildWithID(long ID) {
            return ContentUris.withAppendedId(CONTENT_URI, ID);
        }
    }

    public static class Alarm_Table implements BaseColumns {
        public static final String TABLE_NAME = "ALARM_TABLE";
        public static final String COLUMN_TIME_TEXT = "ALARM_TIME_TEXT";
        public static final String COLUMN_TIME = "ALARM_TIME";
        public static final String COLUMN_ACTIVE = "ALARM_ACTIVE";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + Goal_Table.TABLE_NAME;
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + Goal_Table.TABLE_NAME;

        public static Uri buildWithID(long ID) {
            return ContentUris.withAppendedId(CONTENT_URI, ID);
        }
    }

    public static class Journal_Table implements BaseColumns {
        public static final String TABLE_NAME = "JOURNAL_TABLE";
        public static final String COLUMN_DATE = "JOURNAL_DATE";
        public static final String COLUMN_JOURNAL = "JOURNAL_JOURNAL";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + Goal_Table.TABLE_NAME;
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + Goal_Table.TABLE_NAME;

        public static Uri buildWithID(long ID) {
            return ContentUris.withAppendedId(CONTENT_URI, ID);
        }
    }




}
