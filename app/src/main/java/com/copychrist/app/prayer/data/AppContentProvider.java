package com.copychrist.app.prayer.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.copychrist.app.prayer.data.local.DatabaseContract;
import com.copychrist.app.prayer.data.local.DatabaseHelper;

import static android.database.DatabaseUtils.longForQuery;

/**
 * Created by jim on 8/29/17.
 */

public class AppContentProvider extends ContentProvider {
    private static final String TAG = "[AppContentProvider] ";

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private DatabaseHelper dbHelper;

    public static final int BIBLE_VERSE = 100;
    public static final int CONTACT = 200;
    public static final int CONTACT_GROUP = 300;
    public static final int CONTACT_GROUP_ITEM = 301;
    public static final int PRAYER_LIST = 400;
    public static final int PRAYER_REQUEST = 500;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        // For each type of URI create a corresponding code.
        matcher.addURI(authority, DatabaseContract.BibleVerseEntry.TABLE_NAME.toLowerCase() + "/*", BIBLE_VERSE);
        matcher.addURI(authority, DatabaseContract.ContactEntry.TABLE_NAME.toLowerCase() + "/#", CONTACT);

        matcher.addURI(authority, DatabaseContract.ContactGroupEntry.TABLE_NAME.toLowerCase(), CONTACT_GROUP);
        matcher.addURI(authority, DatabaseContract.ContactGroupEntry.TABLE_NAME.toLowerCase() + "/#", CONTACT_GROUP_ITEM);

        matcher.addURI(authority, DatabaseContract.PrayerListEntry.TABLE_NAME.toLowerCase() + "/#", PRAYER_LIST);
        matcher.addURI(authority, DatabaseContract.PrayerRequestEntry.TABLE_NAME.toLowerCase() + "/#", PRAYER_REQUEST);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BIBLE_VERSE:
                return DatabaseContract.BibleVerseEntry.CONTENT_ITEM_TYPE;
            case CONTACT:
                return DatabaseContract.ContactEntry.CONTENT_ITEM_TYPE;
            case CONTACT_GROUP:
                return DatabaseContract.ContactGroupEntry.CONTENT_TYPE;
            case CONTACT_GROUP_ITEM:
                return DatabaseContract.ContactGroupEntry.CONTENT_ITEM_TYPE;
            case PRAYER_LIST:
                return DatabaseContract.PrayerListEntry.CONTENT_ITEM_TYPE;
            case PRAYER_REQUEST:
                return DatabaseContract.PrayerRequestEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            case BIBLE_VERSE:
                retCursor = null; //Todo: getBibleVerseByPassage
                break;
            case CONTACT_GROUP:
                retCursor = dbHelper.getReadableDatabase().query(
                        DatabaseContract.ContactGroupEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CONTACT_GROUP_ITEM:
                String[] where = {uri.getLastPathSegment()};
                retCursor = dbHelper.getReadableDatabase().query(
                        DatabaseContract.ContactGroupEntry.TABLE_NAME,
                        projection,
                        DatabaseContract.ContactGroupEntry._ID + " = ?",
                        where,
                        null,
                        null,
                        sortOrder
                );
                break;
            //Todo: getContactGroups
            //Todo: getContactGroupById
            //Todo: getContactsByContactGroupId
            //Todo: getPrayerRequestsByContactId
            //Todo: getContactById
            //Todo: getActivePrayerRequestById
            //Todo: getArchiPrayerRequestById
            //Todo: getPrayerLists
            //Todo: getPrayerListsById
            //Todo: getPrayerRequestsByPrayerListId
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        switch (uriMatcher.match(uri)) {
//            case BIBLE_VERSE:
//                return doBulkInsert(uri, values);
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private int doBulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int returnCount = 0;

        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                long _id = insertOrReplace(
                        db,
                        uri,
                        value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri = null;
        String tableName;
        long insertId;

        switch (uriMatcher.match(uri)) {
            case BIBLE_VERSE:
                insertId = db.insert(DatabaseContract.BibleVerseEntry.TABLE_NAME, null, contentValues);
                if(insertId > 0) {
                    String passage = contentValues.getAsString(DatabaseContract.BibleVerseEntry.COLUMN_BOOK) + " " +
                            contentValues.getAsString(DatabaseContract.BibleVerseEntry.COLUMN_CHAPTER) + ":" +
                            contentValues.getAsString(DatabaseContract.BibleVerseEntry.COLUMN_VERSE);
                    returnUri = DatabaseContract.BibleVerseEntry.buildWithPassageUri(passage);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case CONTACT:
                insertId = db.insert(DatabaseContract.ContactEntry.TABLE_NAME, null, contentValues);
                if(insertId > 0) {
                    returnUri = DatabaseContract.ContactEntry.buildWithIdUri(insertId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case CONTACT_GROUP:
                tableName =  DatabaseContract.ContactGroupEntry.TABLE_NAME;
                Boolean exists = alreadyExists(db, tableName,
                        new String[] {
                                DatabaseContract.ContactGroupEntry._ID,
                                DatabaseContract.ContactGroupEntry.COLUMN_NAME
                        },
                        DatabaseContract.ContactGroupEntry.COLUMN_NAME + " = ?",
                        new String[] { contentValues.getAsString(DatabaseContract.ContactGroupEntry.COLUMN_NAME) });
                if (exists) {
                    returnUri = DatabaseContract.ContactGroupEntry.buildExistsUri();
                } else {
                    Long count = getRowCount(db, tableName);
                    contentValues.put(DatabaseContract.ContactGroupEntry.COLUMN_SORT_ORDER, count);
                    //Insert Entry
                    long _id = db.insert(tableName, null, contentValues);
                    if (_id > 0) {
                        returnUri = DatabaseContract.ContactGroupEntry.buildWithIdUri(_id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                }
                break;
            case PRAYER_LIST:
                insertId = db.insert(DatabaseContract.PrayerListEntry.TABLE_NAME, null, contentValues);
                if(insertId > 0) {
                    returnUri = DatabaseContract.PrayerListEntry.buildWithIdUri(insertId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case PRAYER_REQUEST:
                insertId = db.insert(DatabaseContract.PrayerRequestEntry.TABLE_NAME, null, contentValues);
                if(insertId > 0) {
                    returnUri = DatabaseContract.PrayerRequestEntry.buildWithIdUri(insertId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    private long insertOrReplace(SQLiteDatabase db, Uri uri, ContentValues values) throws SQLException {
        long returnId = -1;
        switch (uriMatcher.match(uri)) {
//            case BIBLE_VERSE:
//                try {
//                    returnId = db.replaceOrThrow(DatabaseContract.BibleVerseEntry.TABLE_NAME, null, values);
//                } catch (SQLiteConstraintException e) {
//                    throw e;
//                }
//                break;
        }

        return returnId;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated;

        String tableName;
        switch (uriMatcher.match(uri)) {
            case BIBLE_VERSE:
                tableName =  DatabaseContract.BibleVerseEntry.TABLE_NAME;
                break;
            case CONTACT:
                tableName =  DatabaseContract.ContactEntry.TABLE_NAME;
                break;
            case CONTACT_GROUP:
                tableName =  DatabaseContract.ContactGroupEntry.TABLE_NAME;
                break;
            case PRAYER_LIST:
                tableName =  DatabaseContract.PrayerListEntry.TABLE_NAME;
                break;
            case PRAYER_REQUEST:
                tableName =  DatabaseContract.PrayerRequestEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        rowsUpdated = db.update(tableName, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;
        String tableName;
        // this makes it delete all rows, return the number of rows deleted
        if (null == selection) selection = "1";

        switch (uriMatcher.match(uri)) {
            case BIBLE_VERSE:
                tableName =  DatabaseContract.BibleVerseEntry.TABLE_NAME;
                break;
            case CONTACT:
                tableName =  DatabaseContract.ContactEntry.TABLE_NAME;
                break;
            case CONTACT_GROUP:
                tableName =  DatabaseContract.ContactGroupEntry.TABLE_NAME;
                break;
            case PRAYER_LIST:
                tableName =  DatabaseContract.PrayerListEntry.TABLE_NAME;
                break;
            case PRAYER_REQUEST:
                tableName =  DatabaseContract.PrayerRequestEntry.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        rowsDeleted =  db.delete(tableName, selection, selectionArgs);

        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    public long getRowCount(@NonNull SQLiteDatabase db, @NonNull String tableName) {
        return DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM " + tableName, null);
    }

    public boolean alreadyExists(@NonNull SQLiteDatabase db, @NonNull String tableName,
                                  String[] columns, String selection, String[] selectionArgs) {
        boolean entryFound = false;
        Cursor exists = db.query(
                tableName,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (exists.moveToLast()) {
            entryFound = true;
        }
        exists.close();
        return entryFound;
    }


    @Override
    public void shutdown() {
        //Todo:: DBHelper close
        super.shutdown();
    }
}
