package com.example.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "coursework.db";
    public static final int DB_VERSION = 12;
    public DatabaseHelper(@Nullable Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Type table
        String query = "CREATE TABLE " + DatabaseContract.Type_Table.TABLE_NAME +
                " (" + DatabaseContract.Type_Table._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.Type_Table.COLUMN_TYPE + " TEXT," +
                DatabaseContract.Type_Table.COLUMN_COLOUR + " TEXT);";
        db.execSQL(query);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.Type_Table.COLUMN_TYPE, "Academic");
        contentValues.put(DatabaseContract.Type_Table.COLUMN_COLOUR, "#f6be85");
        db.insert(DatabaseContract.Type_Table.TABLE_NAME, null, contentValues);
        contentValues.put(DatabaseContract.Type_Table.COLUMN_TYPE, "Financial");
        contentValues.put(DatabaseContract.Type_Table.COLUMN_COLOUR, "#93cd89");
        db.insert(DatabaseContract.Type_Table.TABLE_NAME, null, contentValues);
        contentValues.put(DatabaseContract.Type_Table.COLUMN_TYPE, "Mental");
        contentValues.put(DatabaseContract.Type_Table.COLUMN_COLOUR, "#e298e8");
        db.insert(DatabaseContract.Type_Table.TABLE_NAME, null, contentValues);
        contentValues.put(DatabaseContract.Type_Table.COLUMN_TYPE, "Physical");
        contentValues.put(DatabaseContract.Type_Table.COLUMN_COLOUR, "#90E0F3");
        db.insert(DatabaseContract.Type_Table.TABLE_NAME, null, contentValues);
        contentValues.put(DatabaseContract.Type_Table.COLUMN_TYPE, "Personal");
        contentValues.put(DatabaseContract.Type_Table.COLUMN_COLOUR, "#CE656A");
        db.insert(DatabaseContract.Type_Table.TABLE_NAME, null, contentValues);



        // Goal table
        query = "CREATE TABLE " + DatabaseContract.Goal_Table.TABLE_NAME +
                " (" + DatabaseContract.Goal_Table._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.Goal_Table.COLUMN_NAME + " TEXT, " +
                DatabaseContract.Goal_Table.COLUMN_DESCRIPTION + " TEXT, " +
                DatabaseContract.Goal_Table.COLUMN_PROGRESS + " INT, " +
                DatabaseContract.Goal_Table.COLUMN_TYPE + " INT, " +
                DatabaseContract.Goal_Table.COLUMN_PARENT + " INT, " +
                "FOREIGN KEY(" + DatabaseContract.Goal_Table.COLUMN_TYPE + ") REFERENCES " + DatabaseContract.Type_Table.COLUMN_TYPE + ");";
        db.execSQL(query);

        // Alarm table
        query = "CREATE TABLE " + DatabaseContract.Alarm_Table.TABLE_NAME +
                " (" + DatabaseContract.Alarm_Table._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.Alarm_Table.COLUMN_TIME_TEXT + " TEXT, " +
                DatabaseContract.Alarm_Table.COLUMN_TIME + " INT, " +
                DatabaseContract.Alarm_Table.COLUMN_ACTIVE + " BOOL);";
        db.execSQL(query);

        // Journal Table
        query = "CREATE TABLE " + DatabaseContract.Journal_Table.TABLE_NAME +
                " (" + DatabaseContract.Journal_Table._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.Journal_Table.COLUMN_DATE + " TEXT, " +
                DatabaseContract.Journal_Table.COLUMN_JOURNAL + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Goal_Table.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Type_Table.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Alarm_Table.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS "+ DatabaseContract.Journal_Table.TABLE_NAME + ";");
        onCreate(db);
    }
}
