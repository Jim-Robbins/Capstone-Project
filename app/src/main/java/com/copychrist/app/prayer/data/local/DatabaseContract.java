package com.copychrist.app.prayer.data.local;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.copychrist.app.prayer.data.model.BibleVerse;

/**
 * Created by jim on 8/29/17.
 */

public class DatabaseContract {
    public static final String CONTENT_AUTHORITY = "com.copychrist.app.prayer.PrayerList";
    private static final String CONTENT_SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);

    private static final String SEPARATOR = "/";
    public static final String ALREADY_EXISTS = "exists";
    private static final String BASE_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + SEPARATOR + CONTENT_AUTHORITY + SEPARATOR;
    private static final String BASE_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + SEPARATOR + CONTENT_AUTHORITY + SEPARATOR ;


    public static abstract class BibleVerseEntry implements BaseColumns {
        private BibleVerseEntry() {}

        public static final String TABLE_NAME = "BibleVerse";

        public static final String COLUMN_BOOK      = "book";
        public static final String COLUMN_CHAPTER   = "chapter";
        public static final String COLUMN_VERSE     = "verse";
        public static final String COLUMN_TEXT      = "text";
        public static final String COLUMN_VERSION   = "version";
        public static final String COLUMN_API_URL   = "api_url";
        public static final String COLUMN_CREATED   = "create_date";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME.toLowerCase());

        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + TABLE_NAME.toLowerCase();
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + TABLE_NAME.toLowerCase();

        public static Uri buildWithPassageUri(String passage) {
            String encodedPassage = BibleVerse.getUrlEncodedPassage(passage);

            return CONTENT_URI.buildUpon().appendPath(encodedPassage).build();
        }

        public static String getPassageFromUri(Uri uri) {
            String encodedPassage = uri.getLastPathSegment();
            return BibleVerse.getUrlDecodedPassage(encodedPassage);
        }

        public static Uri buildExistsUri() {
            return CONTENT_URI.buildUpon().appendPath(ALREADY_EXISTS).build();
        }

        public static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_BOOK + " TEXT NOT NULL, " +
                COLUMN_CHAPTER + " INTEGER NOT NULL, " +
                COLUMN_VERSE + " TEXT NOT NULL, " +
                COLUMN_TEXT + " TEXT, " +
                COLUMN_VERSION + " TEXT, " +
                COLUMN_API_URL + " TEXT, " +
                COLUMN_CREATED  + " NUMERIC DEFAULT CURRENT_TIMESTAMP, " +
                "PRIMARY KEY (" + COLUMN_BOOK + "," + COLUMN_CHAPTER + "," + COLUMN_VERSE + ")" +
                " );";

        public static String DROP_TABLE_QUERY = getDropTableQuery(TABLE_NAME);
    }

    public static final class ContactEntry implements BaseColumns {
        private ContactEntry() {}

        public static final String TABLE_NAME = "Contact";

        public static final String COLUMN_FIRST_NAME    = "first_name";
        public static final String COLUMN_LAST_NAME     = "last_name";
        public static final String COLUMN_PICTURE_URL   = "picture_url";
        public static final String COLUMN_GROUP         = "group_id";
        public static final String COLUMN_SORT_ORDER    = "sort_order";
        public static final String COLUMN_CREATED       = "create_date";
        public static String[] CONTACT_COLUMNS = new String[]{
                _ID,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_PICTURE_URL,
                COLUMN_GROUP};

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME.toLowerCase());

        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + TABLE_NAME.toLowerCase();
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + TABLE_NAME.toLowerCase();

        public static Uri buildWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildUri() {
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildExistsUri() {
            return CONTENT_URI.buildUpon().appendPath(ALREADY_EXISTS).build();
        }

        public static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_PICTURE_URL + " TEXT, " +
                COLUMN_GROUP + " INTEGER NOT NULL, " +
                COLUMN_SORT_ORDER + " INTEGER NOT NULL, " +
                COLUMN_CREATED  + " INTEGER DEFAULT CURRENT_TIMESTAMP " +
                " );";

        public static String DROP_TABLE_QUERY = getDropTableQuery(TABLE_NAME);

        public static String[] CONTACT_REQUEST_COLUMNS = new String[]{
                TABLE_NAME+"."+_ID,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_PICTURE_URL,
                COLUMN_GROUP,
                PrayerRequestEntry.TABLE_NAME+"."+PrayerRequestEntry._ID + " AS request_id",
                PrayerRequestEntry.COLUMN_TITLE,
                PrayerRequestEntry.COLUMN_DESC,
                PrayerRequestEntry.COLUMN_END_DATE,
                PrayerRequestEntry.COLUMN_ANSWERED
                };

        public static String getIdFromUri(Uri uri) {
                return uri.getLastPathSegment();
        }
    }

    public static final class ContactGroupEntry implements BaseColumns {
        private ContactGroupEntry() {}

        public static final String TABLE_NAME = "ContactGroup";

        public static final String COLUMN_NAME       = "name";
        public static final String COLUMN_DESC       = "description";
        public static final String COLUMN_SORT_ORDER = "sort_order";
        public static final String COLUMN_CREATED    = "create_date";
        public static String[] CONTACT_GROUP_COLUMNS = new String[]{
                _ID,
                COLUMN_NAME,
                COLUMN_DESC,
                COLUMN_SORT_ORDER,
                COLUMN_CREATED};

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME.toLowerCase());

        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + TABLE_NAME.toLowerCase();
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + TABLE_NAME.toLowerCase();

        public static Uri buildWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildWithIdUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static Uri buildUri() {
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildExistsUri() {
            return CONTENT_URI.buildUpon().appendPath(ALREADY_EXISTS).build();
        }

        public static String getResultFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_DESC + " TEXT, " +
                COLUMN_SORT_ORDER + " INTEGER, " +
                COLUMN_CREATED  + " NUMERIC DEFAULT CURRENT_TIMESTAMP " +
                " );";

        public static String DROP_TABLE_QUERY = getDropTableQuery(TABLE_NAME);
    }

    public static final class PrayerListEntry implements BaseColumns {

        public static final String TABLE_NAME = "PrayerList";

        public static final String COLUMN_NAME       = "name";
        public static final String COLUMN_SORT_ORDER = "sort_order";
        public static final String COLUMN_CREATED    = "create_date";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME.toLowerCase());

        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + TABLE_NAME.toLowerCase();
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + TABLE_NAME.toLowerCase();

        public static Uri buildWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildExistsUri() {
            return CONTENT_URI.buildUpon().appendPath(ALREADY_EXISTS).build();
        }

        public static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_SORT_ORDER + " INTEGER NOT NULL, " +
                COLUMN_CREATED  + " NUMERIC DEFAULT CURRENT_TIMESTAMP " +
                " );";

        public static String DROP_TABLE_QUERY = getDropTableQuery(TABLE_NAME);
    }

    public static final class PrayerRequestEntry implements BaseColumns {

        public static final String TABLE_NAME = "PrayerRequest";

        public static final String COLUMN_TITLE     = "title";
        public static final String COLUMN_DESC      = "description";
        public static final String COLUMN_END_DATE  = "end_date";
        public static final String COLUMN_ANSWERED  = "answered_date";
        public static final String COLUMN_CREATED   = "create_date";
        public static final String COLUMN_CONTACT   = "contact_id";
        public static final String COLUMN_PRAYED_FOR = "prayed_for_on";
        public static String[] PRAYER_REQUEST_COLUMNS = new String[]{
                _ID,
                COLUMN_TITLE,
                COLUMN_DESC,
                COLUMN_END_DATE,
                COLUMN_ANSWERED,
                COLUMN_CONTACT
        };

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME.toLowerCase());

        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + TABLE_NAME.toLowerCase();
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + TABLE_NAME.toLowerCase();

        public static Uri buildWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildExistsUri() {
            return CONTENT_URI.buildUpon().appendPath(ALREADY_EXISTS).build();
        }

        public static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_DESC + " TEXT, " +
                COLUMN_CONTACT + " INTEGER NOT NULL, " +
                COLUMN_END_DATE + " NUMERIC, " +
                COLUMN_ANSWERED + " NUMERIC, " +
                COLUMN_PRAYED_FOR + " TEXT, " +
                COLUMN_CREATED  + " NUMERIC DEFAULT CURRENT_TIMESTAMP " +
                " );";

        public static String DROP_TABLE_QUERY = getDropTableQuery(TABLE_NAME);
    }

    public static final class PrayerListRequestEntry implements BaseColumns {

        public static final String TABLE_NAME = "PrayerListRequest";

        public static final String COLUMN_LIST_ID    = "prayer_list_id";
        public static final String COLUMN_REQUEST_ID = "prayer_request_id";
        public static final String COLUMN_SORT_ORDER = "sort_order";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME.toLowerCase());

        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + TABLE_NAME.toLowerCase();
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + TABLE_NAME.toLowerCase();

        // Accepts a prayerList id to filter by
        public static Uri buildWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildDeleteUri(long listId, long requestId) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(listId)).appendPath(encodedPassage).build();
        }

        public static Uri buildExistsUri() {
            return CONTENT_URI.buildUpon().appendPath(ALREADY_EXISTS).build();
        }

        public static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_LIST_ID + " INTEGER NOT NULL, " +
                COLUMN_REQUEST_ID + " INTEGER NOT NULL, " +
                COLUMN_SORT_ORDER + " INTEGER NOT NULL, " +
                "PRIMARY KEY (" + COLUMN_LIST_ID + "," + COLUMN_REQUEST_ID + "," + COLUMN_SORT_ORDER + ")" +
                " );";

        public static String DROP_TABLE_QUERY = getDropTableQuery(TABLE_NAME);
    }

    public static final class PrayerRequestVerseEntry implements BaseColumns {

        public static final String TABLE_NAME = "PrayerRequestVerse";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME.toLowerCase());

        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + TABLE_NAME.toLowerCase();
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + TABLE_NAME.toLowerCase();

        public static final String COLUMN_VERSE      = "bible_verse";
        public static final String COLUMN_REQUEST_ID = "prayer_request_id";
        public static final String COLUMN_SORT_ORDER = "sort_order";

        public static Uri buildWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildDeleteUri(long requestId, String passage) {
            return ContentUris.appendId(CONTENT_URI.buildUpon(), requestId).appendEncodedPath(passage).build();
        }

        public static String getPassageFromUri(Uri uri) {
            String encodedPassage = uri.getEncodedPath()
            return BibleVerse.getUrlDecodedPassage(encodedPassage);
        }

        public static long getRequestIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static Uri buildExistsUri() {
            return CONTENT_URI.buildUpon().appendPath(ALREADY_EXISTS).build();
        }

        public static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_VERSE + " TEXT NOT NULL, " +
                COLUMN_REQUEST_ID + " INTEGER NOT NULL, " +
                COLUMN_SORT_ORDER + " INTEGER NOT NULL, " +
                "PRIMARY KEY (" + COLUMN_VERSE + "," + COLUMN_REQUEST_ID + "," + COLUMN_SORT_ORDER + ")" +
                " );";

        public static String DROP_TABLE_QUERY = getDropTableQuery(TABLE_NAME);
    }

    public static String getDropTableQuery(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }

}
