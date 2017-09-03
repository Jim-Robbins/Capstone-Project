package com.copychrist.app.prayer.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Singleton;

/**
 * Created by jim on 8/30/17.
 */

@Singleton
public class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "pray_with_dedication.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        try {
            db.execSQL(DatabaseContract.BibleVerseEntry.CREATE_TABLE_QUERY);
            db.execSQL(DatabaseContract.ContactEntry.CREATE_TABLE_QUERY);
            db.execSQL(DatabaseContract.ContactGroupEntry.CREATE_TABLE_QUERY);
            db.execSQL(DatabaseContract.PrayerListEntry.CREATE_TABLE_QUERY);
            db.execSQL(DatabaseContract.PrayerRequestEntry.CREATE_TABLE_QUERY);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
        db.execSQL(DatabaseContract.BibleVerseEntry.DROP_TABLE_QUERY);
        db.execSQL(DatabaseContract.ContactEntry.DROP_TABLE_QUERY);
        db.execSQL(DatabaseContract.ContactGroupEntry.DROP_TABLE_QUERY);
        db.execSQL(DatabaseContract.PrayerListEntry.DROP_TABLE_QUERY);
        db.execSQL(DatabaseContract.PrayerRequestEntry.DROP_TABLE_QUERY);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
        super.onDowngrade(db, oldVersion, newVersion);
    }

}
