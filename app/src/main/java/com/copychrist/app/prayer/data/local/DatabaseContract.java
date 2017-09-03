package com.copychrist.app.prayer.data.local;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jim on 8/29/17.
 */

public class DatabaseContract {
    public static final String CONTENT_AUTHORITY = "com.copychrist.app.prayer.PrayerListContract";
    private static final String CONTENT_SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);

    private static final String BASE_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/";
    private static final String BASE_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" ;

    public static final String PATH_BIBLE_VERSE = "bible_verse";
    public static final String PATH_CONTACT = "contact";
    public static final String PATH_CONTACT_GROUP = "contact_group";
    public static final String PATH_PRAYER_LIST = "prayer_list";
    public static final String PATH_PRAYER_REQUEST = "prayer_request";

    public static abstract class BibleVerseContract implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BIBLE_VERSE).build();

        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + PATH_BIBLE_VERSE;
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + PATH_BIBLE_VERSE;

        public static final String TABLE_NAME = "BibleVerseContract";

        public static final String COLUMN_BOOK      = "book";
        public static final String COLUMN_CHAPTER   = "chapter";
        public static final String COLUMN_VERSE     = "verse";
        public static final String COLUMN_TEXT      = "text";
        public static final String COLUMN_VERSION   = "version";
        public static final String COLUMN_API_URL   = "apiUrl";
        public static final String COLUMN_CREATED = "created";

        public static Uri buildWithPassageUri(String passage) {
            return CONTENT_URI.buildUpon().appendPath(passage).build();
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

    public static final class ContactContract implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTACT).build();

        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + PATH_CONTACT;
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + PATH_CONTACT;

        public static final String TABLE_NAME = "ContactContract";

        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_PICTURE_URL = "pictureUrl";
        public static final String COLUMN_REQUESTS = "requests";
        public static final String COLUMN_GROUP = "group";
        public static final String COLUMN_CREATED = "created";

        public static Uri buildWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_PICTURE_URL + " TEXT, " +
                COLUMN_REQUESTS + " TEXT, " +
                COLUMN_GROUP + " INTEGER, " +
                COLUMN_CREATED  + " NUMERIC DEFAULT CURRENT_TIMESTAMP " +
                " );";

        public static String DROP_TABLE_QUERY = getDropTableQuery(TABLE_NAME);
    }

    public static final class ContactGroupContract implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTACT_GROUP).build();

        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + PATH_CONTACT_GROUP;
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + PATH_CONTACT_GROUP;

        public static final String TABLE_NAME = "ContactGroupContract";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_SORT_ORDER = "order";
        public static final String COLUMN_CONTACTS = "contacts";
        public static final String COLUMN_CREATED = "created";

        public static Uri buildWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_DESC + " TEXT, " +
                COLUMN_SORT_ORDER + " INTEGER NOT NULL, " +
                COLUMN_CONTACTS + " TEXT, " +
                COLUMN_CREATED  + " NUMERIC DEFAULT CURRENT_TIMESTAMP " +
                " );";

        public static String DROP_TABLE_QUERY = getDropTableQuery(TABLE_NAME);
    }

    public static final class PrayerListContract implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRAYER_LIST).build();

        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + PATH_PRAYER_LIST;
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + PATH_PRAYER_LIST;

        public static final String TABLE_NAME = "PrayerListContract";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SORT_ORDER = "order";
        public static final String COLUMN_REQUESTS = "requests";
        public static final String COLUMN_CREATED = "created";

        public static Uri buildWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_SORT_ORDER + " INTEGER NOT NULL, " +
                COLUMN_REQUESTS + " TEXT, " +
                COLUMN_CREATED  + " NUMERIC DEFAULT CURRENT_TIMESTAMP " +
                " );";

        public static String DROP_TABLE_QUERY = getDropTableQuery(TABLE_NAME);
    }

    public static final class PrayerRequestContract implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRAYER_REQUEST).build();

        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + PATH_PRAYER_REQUEST;
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + PATH_PRAYER_REQUEST;

        public static final String TABLE_NAME = "PrayerRequestContract";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_VERSES = "verses";
        public static final String COLUMN_END_DATE = "end_date";
        public static final String COLUMN_ANSWERED = "answered";
        public static final String COLUMN_CREATED = "created";
        public static final String COLUMN_CONTACT = "contact";
        public static final String COLUMN_PRAYED_FOR = "prayed_for_on";

        public static Uri buildWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_DESC + " TEXT, " +
                COLUMN_CONTACT + " INTEGER NOT NULL, " +
                COLUMN_VERSES + " TEXT, " +
                COLUMN_END_DATE + " NUMERIC, " +
                COLUMN_ANSWERED + " NUMERIC, " +
                COLUMN_PRAYED_FOR + " TEXT, " +
                COLUMN_CREATED  + " NUMERIC DEFAULT CURRENT_TIMESTAMP " +
                " );";

        public static String DROP_TABLE_QUERY = getDropTableQuery(TABLE_NAME);
    }

    public static String getDropTableQuery(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }

}
