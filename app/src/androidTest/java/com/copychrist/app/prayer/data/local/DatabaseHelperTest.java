package com.copychrist.app.prayer.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import org.junit.Test;

import java.util.HashSet;

/**
 * Created by jim on 9/1/17.
 */
//@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest extends AndroidTestCase  {

    private static final String TAG = "[DatabaseHelperTest] ";

    public void setUp() throws Exception {
        deleteTheDatabase();
    }

    void deleteTheDatabase() {
        mContext.deleteDatabase(DatabaseHelper.DATABASE_NAME);
    }

    @Test
    public void testOnCreate() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(DatabaseContract.BibleVerseEntry.TABLE_NAME);
        tableNameHashSet.add(DatabaseContract.ContactEntry.TABLE_NAME);
        tableNameHashSet.add(DatabaseContract.ContactGroupEntry.TABLE_NAME);
        tableNameHashSet.add(DatabaseContract.PrayerListEntry.TABLE_NAME);
        tableNameHashSet.add(DatabaseContract.PrayerRequestEntry.TABLE_NAME);
        tableNameHashSet.add(DatabaseContract.PrayerListRequestEntry.TABLE_NAME);
        tableNameHashSet.add(DatabaseContract.PrayerRequestVerseEntry.TABLE_NAME);

       mContext.deleteDatabase(DatabaseHelper.DATABASE_NAME);
        SQLiteDatabase db = new DatabaseHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain the tables
        assertTrue("Error: Your database was created without all tables",
                tableNameHashSet.isEmpty());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> bibleVerseColumnHashSet = new HashSet<String>();
        bibleVerseColumnHashSet.add(DatabaseContract.BibleVerseEntry.COLUMN_BOOK);
        bibleVerseColumnHashSet.add(DatabaseContract.BibleVerseEntry.COLUMN_CHAPTER);
        bibleVerseColumnHashSet.add(DatabaseContract.BibleVerseEntry.COLUMN_VERSE);
        bibleVerseColumnHashSet.add(DatabaseContract.BibleVerseEntry.COLUMN_TEXT);
        bibleVerseColumnHashSet.add(DatabaseContract.BibleVerseEntry.COLUMN_VERSION);
        bibleVerseColumnHashSet.add(DatabaseContract.BibleVerseEntry.COLUMN_API_URL);
        bibleVerseColumnHashSet.add(DatabaseContract.BibleVerseEntry.COLUMN_CREATED);

        checkColumns(DatabaseContract.BibleVerseEntry.TABLE_NAME, c, db, bibleVerseColumnHashSet);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> contactColumnHashSet = new HashSet<String>();
        contactColumnHashSet.add(DatabaseContract.ContactEntry._ID);
        contactColumnHashSet.add(DatabaseContract.ContactEntry.COLUMN_FIRST_NAME);
        contactColumnHashSet.add(DatabaseContract.ContactEntry.COLUMN_LAST_NAME);
        contactColumnHashSet.add(DatabaseContract.ContactEntry.COLUMN_PICTURE_URL);
        contactColumnHashSet.add(DatabaseContract.ContactEntry.COLUMN_GROUP);
        contactColumnHashSet.add(DatabaseContract.ContactEntry.COLUMN_CREATED);

        checkColumns(DatabaseContract.ContactEntry.TABLE_NAME, c, db, contactColumnHashSet);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> contactGroupColumnHashSet = new HashSet<String>();
        contactGroupColumnHashSet.add(DatabaseContract.ContactGroupEntry._ID);
        contactGroupColumnHashSet.add(DatabaseContract.ContactGroupEntry.COLUMN_NAME);
        contactGroupColumnHashSet.add(DatabaseContract.ContactGroupEntry.COLUMN_DESC);
        contactGroupColumnHashSet.add(DatabaseContract.ContactGroupEntry.COLUMN_SORT_ORDER);
        contactGroupColumnHashSet.add(DatabaseContract.ContactGroupEntry.COLUMN_CREATED);

        checkColumns(DatabaseContract.ContactGroupEntry.TABLE_NAME, c, db, contactGroupColumnHashSet);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> prayerListColumnHashSet = new HashSet<String>();
        prayerListColumnHashSet.add(DatabaseContract.PrayerListEntry._ID);
        prayerListColumnHashSet.add(DatabaseContract.PrayerListEntry.COLUMN_NAME);
        prayerListColumnHashSet.add(DatabaseContract.PrayerListEntry.COLUMN_SORT_ORDER);
        prayerListColumnHashSet.add(DatabaseContract.PrayerListEntry.COLUMN_CREATED);

        checkColumns(DatabaseContract.PrayerListEntry.TABLE_NAME, c, db, prayerListColumnHashSet);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> prayerRequestColumnHashSet = new HashSet<String>();
        prayerRequestColumnHashSet.add(DatabaseContract.PrayerRequestEntry._ID);
        prayerRequestColumnHashSet.add(DatabaseContract.PrayerRequestEntry.COLUMN_TITLE);
        prayerRequestColumnHashSet.add(DatabaseContract.PrayerRequestEntry.COLUMN_DESC);
        prayerRequestColumnHashSet.add(DatabaseContract.PrayerRequestEntry.COLUMN_END_DATE);
        prayerRequestColumnHashSet.add(DatabaseContract.PrayerRequestEntry.COLUMN_ANSWERED);
        prayerRequestColumnHashSet.add(DatabaseContract.PrayerRequestEntry.COLUMN_CREATED);
        prayerRequestColumnHashSet.add(DatabaseContract.PrayerRequestEntry.COLUMN_CONTACT);
        prayerRequestColumnHashSet.add(DatabaseContract.PrayerRequestEntry.COLUMN_PRAYED_FOR);

        checkColumns(DatabaseContract.PrayerRequestEntry.TABLE_NAME, c, db, prayerRequestColumnHashSet);


        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> prayerListRequestColumnHashSet = new HashSet<String>();
        prayerListColumnHashSet.add(DatabaseContract.PrayerListRequestEntry.COLUMN_LIST_ID);
        prayerListColumnHashSet.add(DatabaseContract.PrayerListRequestEntry.COLUMN_REQUEST_ID);
        prayerListColumnHashSet.add(DatabaseContract.PrayerListRequestEntry.COLUMN_SORT_ORDER);

        checkColumns(DatabaseContract.PrayerListRequestEntry.TABLE_NAME, c, db, prayerListRequestColumnHashSet);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> prayerRequestVerseColumnHashSet = new HashSet<String>();
        prayerListColumnHashSet.add(DatabaseContract.PrayerRequestVerseEntry.COLUMN_VERSE);
        prayerListColumnHashSet.add(DatabaseContract.PrayerRequestVerseEntry.COLUMN_REQUEST_ID);
        prayerListColumnHashSet.add(DatabaseContract.PrayerRequestVerseEntry.COLUMN_SORT_ORDER);

        checkColumns(DatabaseContract.PrayerRequestVerseEntry.TABLE_NAME, c, db, prayerRequestVerseColumnHashSet);

        db.close();
    }

    private void checkColumns(String tableName, Cursor c, SQLiteDatabase db, HashSet<String> columnHashSet)
    {
        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + tableName + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());
        String columnName;
        int columnNameIndex = c.getColumnIndex("name");
        do {
            columnName = c.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, means that the database doesn't contain all of the required entry columns
        assertTrue("Error: The database table " + tableName +
                        ", doesn't contain all of the required entry columns: " + columnName,
                columnHashSet.isEmpty());
    }

    @Test
    public void testBibleVerseTable() {
        testDBTable(DatabaseContract.BibleVerseEntry.TABLE_NAME, TestUtilities.createBibleVerseValues());
    }

    @Test
    public void testContactTable() {
        testDBTable(DatabaseContract.ContactEntry.TABLE_NAME, TestUtilities.createContactValues());
    }

    @Test
    public void testContactGroupTable() {
        testDBTable(DatabaseContract.ContactGroupEntry.TABLE_NAME, TestUtilities.createContactGroupValues());
    }

    @Test
    public void testPrayerListTable() {
        testDBTable(DatabaseContract.PrayerListEntry.TABLE_NAME, TestUtilities.createPrayerListValues());
    }

    @Test
    public void testPrayerRequestTable() {
        testDBTable(DatabaseContract.PrayerRequestEntry.TABLE_NAME, TestUtilities.createPrayerRequestValues());
    }

    @Test
    public void testPrayerListRequestTable() {
        testDBTable(DatabaseContract.PrayerListRequestEntry.TABLE_NAME, TestUtilities.createPrayerListRequestValues());
    }

    @Test
    public void testPrayerRequestVerseTable() {
        testDBTable(DatabaseContract.PrayerRequestVerseEntry.TABLE_NAME, TestUtilities.createPrayerRequestVerseValues());
    }

    @Test
    public void testAppTables() {
        testDBTable(DatabaseContract.BibleVerseEntry.TABLE_NAME, TestUtilities.createBibleVerseValues());
        testDBTable(DatabaseContract.ContactEntry.TABLE_NAME, TestUtilities.createContactValues());
        testDBTable(DatabaseContract.ContactGroupEntry.TABLE_NAME, TestUtilities.createContactGroupValues());
        testDBTable(DatabaseContract.PrayerListEntry.TABLE_NAME, TestUtilities.createPrayerListValues());
        testDBTable(DatabaseContract.PrayerRequestEntry.TABLE_NAME, TestUtilities.createPrayerRequestValues());
        testDBTable(DatabaseContract.PrayerListRequestEntry.TABLE_NAME, TestUtilities.createPrayerListRequestValues());
        testDBTable(DatabaseContract.PrayerRequestVerseEntry.TABLE_NAME, TestUtilities.createPrayerRequestVerseValues());
    }

    private void testDBTable(String tableName, ContentValues contentValues) {
        // First step: Get reference to writable database
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step (Movies): Insert ContentValues into database and get a row ID back
        long rowId = db.insert(tableName, null, contentValues);
        assertTrue(rowId != -1);

        // Third Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                tableName,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from movies_list query", cursor.moveToFirst() );

        // Fourth Step: Validate the movies_list Query
        TestUtilities.validateCurrentRecord("testInsertReadDb " + tableName + " failed to validate",
                cursor, contentValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from " + tableName + " query",
                cursor.moveToNext() );

        // Fifth Step: Close cursor and database
        cursor.close();
        dbHelper.close();
    }

}