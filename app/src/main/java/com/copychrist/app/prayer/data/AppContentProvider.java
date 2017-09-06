package com.copychrist.app.prayer.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.copychrist.app.prayer.data.local.DatabaseContract;
import com.copychrist.app.prayer.data.local.DatabaseContract.*;
import com.copychrist.app.prayer.data.local.DatabaseHelper;
import com.copychrist.app.prayer.data.model.BibleVerse;
import com.copychrist.app.prayer.data.model.PrayerRequest;

/**
 * Created by jim on 8/29/17.
 */

public class AppContentProvider extends ContentProvider {
    private static final String TAG = "[AppContentProvider] ";

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private DatabaseHelper dbHelper;

    public static final int BIBLE_VERSE = 100;
    public static final int BIBLE_VERSE_ITEM = 101;
    public static final int CONTACT = 200;
    public static final int CONTACT_ITEM = 201;
    public static final int CONTACT_GROUP = 300;
    public static final int CONTACT_GROUP_ITEM = 301;
    public static final int PRAYER_LIST = 400;
    public static final int PRAYER_LIST_ITEM = 401;
    public static final int PRAYER_REQUEST = 500;
    public static final int PRAYER_REQUEST_ITEM = 501;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.CONTENT_AUTHORITY;

        // For each type of URI create a corresponding code.
        matcher.addURI(authority, DatabaseContract.BibleVerseEntry.TABLE_NAME.toLowerCase() + "/*", BIBLE_VERSE);

        matcher.addURI(authority, DatabaseContract.ContactEntry.TABLE_NAME.toLowerCase(), CONTACT);
        matcher.addURI(authority, DatabaseContract.ContactEntry.TABLE_NAME.toLowerCase() + "/#", CONTACT_ITEM);

        matcher.addURI(authority, DatabaseContract.ContactGroupEntry.TABLE_NAME.toLowerCase(), CONTACT_GROUP);
        matcher.addURI(authority, DatabaseContract.ContactGroupEntry.TABLE_NAME.toLowerCase() + "/#", CONTACT_GROUP_ITEM);

        matcher.addURI(authority, DatabaseContract.PrayerListEntry.TABLE_NAME.toLowerCase(), PRAYER_LIST);
        matcher.addURI(authority, DatabaseContract.PrayerListEntry.TABLE_NAME.toLowerCase() + "/#", PRAYER_LIST_ITEM);

        matcher.addURI(authority, DatabaseContract.PrayerRequestEntry.TABLE_NAME.toLowerCase(), PRAYER_REQUEST);
        matcher.addURI(authority, DatabaseContract.PrayerRequestEntry.TABLE_NAME.toLowerCase() + "/#", PRAYER_REQUEST_ITEM);

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
                return DatabaseContract.BibleVerseEntry.CONTENT_TYPE;
            case BIBLE_VERSE_ITEM:
                return DatabaseContract.BibleVerseEntry.CONTENT_ITEM_TYPE;
            case CONTACT:
                return DatabaseContract.ContactEntry.CONTENT_TYPE;
            case CONTACT_ITEM:
                return DatabaseContract.ContactEntry.CONTENT_ITEM_TYPE;
            case CONTACT_GROUP:
                return DatabaseContract.ContactGroupEntry.CONTENT_TYPE;
            case CONTACT_GROUP_ITEM:
                return DatabaseContract.ContactGroupEntry.CONTENT_ITEM_TYPE;
            case PRAYER_LIST:
                return DatabaseContract.PrayerListEntry.CONTENT_TYPE;
            case PRAYER_LIST_ITEM:
                return DatabaseContract.PrayerListEntry.CONTENT_ITEM_TYPE;
            case PRAYER_REQUEST:
                return DatabaseContract.PrayerRequestEntry.CONTENT_TYPE;
            case PRAYER_REQUEST_ITEM:
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
                String[] contact_group_where = {uri.getLastPathSegment()};
                retCursor = dbHelper.getReadableDatabase().query(
                        DatabaseContract.ContactGroupEntry.TABLE_NAME,
                        projection,
                        DatabaseContract.ContactGroupEntry._ID + " = ?",
                        contact_group_where,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CONTACT:
                retCursor = dbHelper.getReadableDatabase().query(
                        DatabaseContract.ContactEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CONTACT_ITEM:
                String[] contact_where = {uri.getLastPathSegment()};
                retCursor = dbHelper.getReadableDatabase().query(
                        DatabaseContract.ContactEntry.TABLE_NAME,
                        projection,
                        DatabaseContract.ContactEntry._ID + " = ?",
                        contact_where,
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
        Uri returnUri;
        String tableName;
        long insertId;
        Boolean exists;

        switch (uriMatcher.match(uri)) {
            case BIBLE_VERSE:
                insertId = db.insert(BibleVerseEntry.TABLE_NAME, null, contentValues);
                if(insertId > 0) {
                    String passage = contentValues.getAsString(BibleVerseEntry.COLUMN_BOOK) + " " +
                            contentValues.getAsString(BibleVerseEntry.COLUMN_CHAPTER) + ":" +
                            contentValues.getAsString(BibleVerseEntry.COLUMN_VERSE);
                    returnUri = BibleVerseEntry.buildWithPassageUri(passage);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case CONTACT:
                tableName =  ContactEntry.TABLE_NAME;
                exists = alreadyExists(db, tableName,
                        new String[] {
                                ContactEntry._ID,
                                ContactEntry.COLUMN_FIRST_NAME,
                                ContactEntry.COLUMN_LAST_NAME
                        },
                        ContactEntry.COLUMN_FIRST_NAME + " = ? AND " +
                                ContactEntry.COLUMN_LAST_NAME + " = ?",
                        new String[] { contentValues.getAsString(ContactEntry.COLUMN_FIRST_NAME),
                                contentValues.getAsString(ContactEntry.COLUMN_LAST_NAME)});
                if (exists) {
                    returnUri = ContactEntry.buildExistsUri();
                } else {
                    Long count = getRowCount(db, tableName, ContactEntry.COLUMN_GROUP + " = " + contentValues.getAsString(ContactEntry.COLUMN_GROUP));
                    contentValues.put(ContactEntry.COLUMN_SORT_ORDER, count);
                    //Insert Entry
                    long _id = db.insert(tableName, null, contentValues);
                    if (_id > 0) {
                        returnUri = ContactEntry.buildWithIdUri(_id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                }
                break;
            case CONTACT_GROUP:
                tableName =  ContactGroupEntry.TABLE_NAME;
                exists = alreadyExists(db, tableName,
                        new String[] {
                                ContactGroupEntry._ID,
                                ContactGroupEntry.COLUMN_NAME
                        },
                        ContactGroupEntry.COLUMN_NAME + " = ?",
                        new String[] { contentValues.getAsString(ContactGroupEntry.COLUMN_NAME) });
                if (exists) {
                    returnUri = ContactGroupEntry.buildExistsUri();
                } else {
                    Long count = getRowCount(db, tableName, "");
                    contentValues.put(ContactGroupEntry.COLUMN_SORT_ORDER, count);
                    //Insert Entry
                    long _id = db.insert(tableName, null, contentValues);
                    if (_id > 0) {
                        returnUri = ContactGroupEntry.buildWithIdUri(_id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                }
                break;
            case PRAYER_LIST:
                insertId = db.insert(PrayerListEntry.TABLE_NAME, null, contentValues);
                if(insertId > 0) {
                    returnUri = PrayerListEntry.buildWithIdUri(insertId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case PRAYER_REQUEST:
                insertId = db.insert(PrayerRequestEntry.TABLE_NAME, null, contentValues);
                if(insertId > 0) {
                    returnUri = PrayerRequestEntry.buildWithIdUri(insertId);
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
//                    returnId = db.replaceOrThrow(BibleVerseEntry.TABLE_NAME, null, values);
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
                tableName =  BibleVerseEntry.TABLE_NAME;
                break;
            case CONTACT:
                tableName =  ContactEntry.TABLE_NAME;
                break;
            case CONTACT_GROUP:
                tableName =  ContactGroupEntry.TABLE_NAME;
                break;
            case PRAYER_LIST:
                tableName =  PrayerListEntry.TABLE_NAME;
                break;
            case PRAYER_REQUEST:
                tableName =  PrayerRequestEntry.TABLE_NAME;
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
                tableName =  BibleVerseEntry.TABLE_NAME;
                break;
            case CONTACT:
                tableName =  ContactEntry.TABLE_NAME;
                break;
            case CONTACT_GROUP:
                tableName =  ContactGroupEntry.TABLE_NAME;
                break;
            case PRAYER_LIST:
                tableName =  PrayerListEntry.TABLE_NAME;
                break;
            case PRAYER_REQUEST:
                tableName =  PrayerRequestEntry.TABLE_NAME;
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

    public long getRowCount(@NonNull SQLiteDatabase db, @NonNull String tableName, String whereParam) {
        String where = "";
        if (!whereParam.isEmpty()) {
            where = " WHERE " + whereParam;
        }
        return DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM " + tableName + where, null);
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
        dbHelper.close();
        super.shutdown();
    }

    private static final SQLiteQueryBuilder contactDetailQueryBuilder;
    static {
        contactDetailQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //movies LEFT OUTER JOIN favorite_movies ON movies.movie_id = favorite_movies.movie_id
        contactDetailQueryBuilder.setTables(
                ContactEntry.TABLE_NAME + " LEFT OUTER JOIN " +
                        PrayerRequestEntry.TABLE_NAME +
                        " ON " + ContactEntry.TABLE_NAME +
                        "." + ContactEntry._ID +
                        " = " + PrayerRequestEntry.TABLE_NAME +
                        "." + PrayerRequestEntry.COLUMN_CONTACT);
    }

    private Cursor getContactWithRequests(Uri uri, String[] projection, String selection, String[] selectionArgs) {
        Cursor cursor = contactDetailQueryBuilder.query(dbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return cursor;
    }
}
