package com.copychrist.app.prayer.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.copychrist.app.prayer.data.local.DatabaseContract;
import com.copychrist.app.prayer.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by jim on 9/2/17.
 */

public class TestUtilities extends AndroidTestCase {

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createBibleVerseValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.BibleVerseEntry.COLUMN_BOOK, "john");
        contentValues.put(DatabaseContract.BibleVerseEntry.COLUMN_CHAPTER, 3);
        contentValues.put(DatabaseContract.BibleVerseEntry.COLUMN_VERSE, "16");
        contentValues.put(DatabaseContract.BibleVerseEntry.COLUMN_TEXT, "For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life");
        contentValues.put(DatabaseContract.BibleVerseEntry.COLUMN_VERSION, "");
        contentValues.put(DatabaseContract.BibleVerseEntry.COLUMN_API_URL, "https://www.biblegateway.com/passage/?search=John+3:16&version=KJV");

        return contentValues;
    }

    static ContentValues updateBibleVerseValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.BibleVerseEntry.COLUMN_BOOK, "John");
        contentValues.put(DatabaseContract.BibleVerseEntry.COLUMN_CHAPTER, 3);
        contentValues.put(DatabaseContract.BibleVerseEntry.COLUMN_VERSE, "16");
        contentValues.put(DatabaseContract.BibleVerseEntry.COLUMN_TEXT, "16 For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life.\n" +
                "\n" +
                "17 For God sent not his Son into the world to condemn the world; but that the world through him might be saved.");
        contentValues.put(DatabaseContract.BibleVerseEntry.COLUMN_VERSION, "KJV");
        contentValues.put(DatabaseContract.BibleVerseEntry.COLUMN_API_URL, "https://www.biblegateway.com/passage/?search=John+3:16-17&version=KJV");

        return contentValues;
    }

    static ContentValues createContactValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.ContactEntry.COLUMN_FIRST_NAME, "J");
        contentValues.put(DatabaseContract.ContactEntry.COLUMN_LAST_NAME, "C");
        contentValues.put(DatabaseContract.ContactEntry.COLUMN_PICTURE_URL, "");
        contentValues.put(DatabaseContract.ContactEntry.COLUMN_SORT_ORDER, 2);
        contentValues.put(DatabaseContract.ContactEntry.COLUMN_GROUP, 1);

        return contentValues;
    }

    static ContentValues udpateContactValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.ContactEntry.COLUMN_FIRST_NAME, "John");
        contentValues.put(DatabaseContract.ContactEntry.COLUMN_LAST_NAME, "Calvin");
        contentValues.put(DatabaseContract.ContactEntry.COLUMN_PICTURE_URL, "https://en.wikipedia.org/wiki/John_Calvin#/media/File:John_Calvin_by_Holbein.png");
        contentValues.put(DatabaseContract.ContactEntry.COLUMN_SORT_ORDER, 2);
        contentValues.put(DatabaseContract.ContactEntry.COLUMN_GROUP, 1);

        return contentValues;
    }

    static ContentValues createContactGroupValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.ContactGroupEntry.COLUMN_NAME, "Fam");
        contentValues.put(DatabaseContract.ContactGroupEntry.COLUMN_DESC, "This is them");
        contentValues.put(DatabaseContract.ContactGroupEntry.COLUMN_SORT_ORDER, 1);

        return contentValues;
    }

    static ContentValues udpateContactGroupValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.ContactGroupEntry.COLUMN_NAME, "Family");
        contentValues.put(DatabaseContract.ContactGroupEntry.COLUMN_DESC, "This is my wonderful family");
        contentValues.put(DatabaseContract.ContactGroupEntry.COLUMN_SORT_ORDER, 2);

        return contentValues;
    }

    static ContentValues createPrayerListValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.PrayerListEntry.COLUMN_NAME, "Family List");
        contentValues.put(DatabaseContract.PrayerListEntry.COLUMN_SORT_ORDER, 1);

        return contentValues;
    }

    static ContentValues updatePrayerListValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.PrayerListEntry.COLUMN_NAME, "Family Prayer List");
        contentValues.put(DatabaseContract.PrayerListEntry.COLUMN_SORT_ORDER, 2);

        return contentValues;
    }

    static ContentValues createPrayerRequestValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.PrayerRequestEntry.COLUMN_CONTACT, 1);
        contentValues.put(DatabaseContract.PrayerRequestEntry.COLUMN_TITLE, "John in prison");
        contentValues.put(DatabaseContract.PrayerRequestEntry.COLUMN_DESC, "John was put in prison.");
        contentValues.put(DatabaseContract.PrayerRequestEntry.COLUMN_PRAYED_FOR, "1504462604");
        return contentValues;
    }

    static ContentValues updatePrayerRequestValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.PrayerRequestEntry.COLUMN_TITLE, "John's release from prison");
        contentValues.put(DatabaseContract.PrayerRequestEntry.COLUMN_DESC, "John was put in prison for preaching the true gospel.");
        contentValues.put(DatabaseContract.PrayerRequestEntry.COLUMN_END_DATE, 1504462605);
        contentValues.put(DatabaseContract.PrayerRequestEntry.COLUMN_ANSWERED, 1504462705);
        contentValues.put(DatabaseContract.PrayerRequestEntry.COLUMN_PRAYED_FOR, "1504462604,1504462605");
        return contentValues;
    }

    static ContentValues createPrayerListRequestValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.PrayerListRequestEntry.COLUMN_LIST_ID, 1);
        contentValues.put(DatabaseContract.PrayerListRequestEntry.COLUMN_REQUEST_ID, 1);
        contentValues.put(DatabaseContract.PrayerListRequestEntry.COLUMN_SORT_ORDER, 1);

        return contentValues;
    }
    static ContentValues udpatePrayerListRequestValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.PrayerListRequestEntry.COLUMN_LIST_ID, 2);
        contentValues.put(DatabaseContract.PrayerListRequestEntry.COLUMN_REQUEST_ID, 2);
        contentValues.put(DatabaseContract.PrayerListRequestEntry.COLUMN_SORT_ORDER, 2);

        return contentValues;
    }


    static ContentValues createPrayerRequestVerseValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.PrayerRequestVerseEntry.COLUMN_VERSE, "John 3:16");
        contentValues.put(DatabaseContract.PrayerRequestVerseEntry.COLUMN_REQUEST_ID, 1);
        contentValues.put(DatabaseContract.PrayerRequestVerseEntry.COLUMN_SORT_ORDER, 1);

        return contentValues;
    }

    static ContentValues updatePrayerRequestVerseValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.PrayerRequestVerseEntry.COLUMN_VERSE, "John 3:16-17");
        contentValues.put(DatabaseContract.PrayerRequestVerseEntry.COLUMN_REQUEST_ID, 2);
        contentValues.put(DatabaseContract.PrayerRequestVerseEntry.COLUMN_SORT_ORDER, 2);

        return contentValues;
    }

    /*
    Test the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
    CTS tests.

    Note that this only tests that the onChange function is called; it does not test that the
    correct Uri is returned.
 */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
