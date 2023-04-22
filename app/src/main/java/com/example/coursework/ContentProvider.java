package com.example.coursework;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContentProvider extends android.content.ContentProvider {

    public static final int GOAL = 100;
    public static final int TYPE = 200;
    public static final int JOURNAL = 300;
    public static final int ALARM = 400;
    public static final UriMatcher myUriMatcher = buildUriMatcher();
    public static DatabaseHelper myDBHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.Goal_Table.TABLE_NAME, GOAL);
        matcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.Type_Table.TABLE_NAME, TYPE);
        matcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.Journal_Table.TABLE_NAME, JOURNAL);
        matcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.Alarm_Table.TABLE_NAME, ALARM);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        myDBHelper = new DatabaseHelper(getContext(), DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match_code = myUriMatcher.match(uri);
        Cursor cursor;
        SQLiteDatabase db = myDBHelper.getReadableDatabase();

        switch(match_code) {
            case GOAL: {
                cursor = db.query(
                        DatabaseContract.Goal_Table.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                break;
            }
            case TYPE: {
                cursor = db.query(
                        DatabaseContract.Type_Table.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                break;
            }
            case JOURNAL: {
                cursor = db.query(
                        DatabaseContract.Journal_Table.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                break;
            }
            case ALARM: {
                cursor = db.query(
                        DatabaseContract.Alarm_Table.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match_code = myUriMatcher.match(uri);

        switch (match_code) {
            case GOAL: {
                return DatabaseContract.Goal_Table.CONTENT_TYPE_DIR;
            }
            case TYPE: {
                return DatabaseContract.Type_Table.CONTENT_TYPE_DIR;
            }
            case JOURNAL: {
                return DatabaseContract.Journal_Table.CONTENT_TYPE_DIR;
            }
            case ALARM: {
                return DatabaseContract.Alarm_Table.CONTENT_TYPE_DIR;
            }
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match_code = myUriMatcher.match(uri);
        Uri returnUri = null;
        SQLiteDatabase db = myDBHelper.getWritableDatabase();

        switch(match_code) {
            case GOAL: {
                long _id = db.insert(DatabaseContract.Goal_Table.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = DatabaseContract.Goal_Table.buildWithID(_id);
                } else {
                    throw new SQLException("Failed to insert");
                }
                break;
            }
            case TYPE: {
                long _id = db.insert(DatabaseContract.Type_Table.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = DatabaseContract.Type_Table.buildWithID(_id);
                } else {
                    throw new SQLException("Failed to insert");
                }
                break;
            }
            case JOURNAL: {
                long _id = db.insert(DatabaseContract.Journal_Table.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = DatabaseContract.Journal_Table.buildWithID(_id);
                } else {
                    throw new SQLException("Failed to insert");
                }
                break;
            }
            case ALARM: {
                long _id = db.insert(DatabaseContract.Alarm_Table.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = DatabaseContract.Alarm_Table.buildWithID(_id);
                } else {
                    throw new SQLException("Failed to insert");
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match_code = myUriMatcher.match(uri);
        SQLiteDatabase db = myDBHelper.getWritableDatabase();

        switch(match_code) {
            case GOAL: {
                db.delete(DatabaseContract.Goal_Table.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TYPE: {
                db.delete(DatabaseContract.Type_Table.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case JOURNAL: {
                db.delete(DatabaseContract.Journal_Table.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case ALARM: {
                db.delete(DatabaseContract.Alarm_Table.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return 1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match_code = myUriMatcher.match(uri);
        int num_rows = 0;
        SQLiteDatabase db = myDBHelper.getWritableDatabase();

        switch(match_code) {
            case GOAL: {
                num_rows = db.update(DatabaseContract.Goal_Table.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }
            case TYPE: {
                num_rows = db.update(DatabaseContract.Type_Table.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }
            case JOURNAL: {
                System.out.println(contentValues.toString());
                System.out.println(selection);
                System.out.println(selectionArgs);
                num_rows = db.update(DatabaseContract.Journal_Table.TABLE_NAME, contentValues, selection, selectionArgs);
                if (num_rows == 0) {
                    db.insert(DatabaseContract.Journal_Table.TABLE_NAME, null, contentValues);
                    num_rows = 1;
                }
                break;
            }
            case ALARM: {
                num_rows = db.update(DatabaseContract.Alarm_Table.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        return num_rows;
    }
}
