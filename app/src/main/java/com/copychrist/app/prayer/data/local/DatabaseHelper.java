package com.copychrist.app.prayer.data.local;

import android.content.Context;
import android.database.SQLException;
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
        try {
            db.execSQL(DatabaseContract.BibleVerseContract.CREATE_TABLE_QUERY);
            db.execSQL(DatabaseContract.ContactContract.CREATE_TABLE_QUERY);
            db.execSQL(DatabaseContract.ContactGroupContract.CREATE_TABLE_QUERY);
            db.execSQL(DatabaseContract.PrayerListContract.CREATE_TABLE_QUERY);
            db.execSQL(DatabaseContract.PrayerRequestContract.CREATE_TABLE_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        db.execSQL(DatabaseContract.BibleVerseContract.DROP_TABLE_QUERY);
        db.execSQL(DatabaseContract.ContactContract.DROP_TABLE_QUERY);
        db.execSQL(DatabaseContract.ContactGroupContract.DROP_TABLE_QUERY);
        db.execSQL(DatabaseContract.PrayerListContract.DROP_TABLE_QUERY);
        db.execSQL(DatabaseContract.PrayerRequestContract.DROP_TABLE_QUERY);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
        super.onDowngrade(db, oldVersion, newVersion);
    }

}
