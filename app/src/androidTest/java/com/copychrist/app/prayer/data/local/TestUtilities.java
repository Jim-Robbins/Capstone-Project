package com.copychrist.app.prayer.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

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
        ContentValues bibleVerseValues = new ContentValues();
        bibleVerseValues.put(DatabaseContract.BibleVerseEntry.COLUMN_BOOK, "John");
        bibleVerseValues.put(DatabaseContract.BibleVerseEntry.COLUMN_CHAPTER, 3);
        bibleVerseValues.put(DatabaseContract.BibleVerseEntry.COLUMN_VERSE, "16-17");
        bibleVerseValues.put(DatabaseContract.BibleVerseEntry.COLUMN_TEXT, "6 For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life.\n" +
                "\n" +
                "17 For God sent not his Son into the world to condemn the world; but that the world through him might be saved.");
        bibleVerseValues.put(DatabaseContract.BibleVerseEntry.COLUMN_VERSION, "KJV");
        bibleVerseValues.put(DatabaseContract.BibleVerseEntry.COLUMN_API_URL, "https://www.biblegateway.com/passage/?search=John+3:16-17&version=KJV");

        return bibleVerseValues;
    }
}
