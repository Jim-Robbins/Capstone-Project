package com.copychrist.app.prayer.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.copychrist.app.prayer.data.local.DatabaseContract;
import com.copychrist.app.prayer.data.local.DatabaseContract.BibleVerseEntry;
import com.copychrist.app.prayer.data.local.DatabaseContract.ContactEntry;
import com.copychrist.app.prayer.data.local.DatabaseContract.ContactGroupEntry;
import com.copychrist.app.prayer.data.local.DatabaseContract.PrayerRequestEntry;
import com.copychrist.app.prayer.data.local.DatabaseContract.PrayerListEntry;
import com.copychrist.app.prayer.data.local.DatabaseContract.PrayerListRequestEntry;
import com.copychrist.app.prayer.data.local.DatabaseContract.PrayerRequestVerseEntry;
import com.copychrist.app.prayer.data.local.DatabaseHelper;
import com.copychrist.app.prayer.data.model.PrayerRequest;

/**
 * Created by jim on 9/5/17.
 */

public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();
    private static final String TEST_PASSAGE = "John 3:16";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    private void deleteAllRecords() {
        deleteAllRecordsFromProviderByTable(PrayerRequestVerseEntry.CONTENT_URI, PrayerRequestVerseEntry.TABLE_NAME);
        deleteAllRecordsFromProviderByTable(PrayerListRequestEntry.CONTENT_URI, PrayerListRequestEntry.TABLE_NAME);
        deleteAllRecordsFromProviderByTable(BibleVerseEntry.CONTENT_URI, BibleVerseEntry.TABLE_NAME);
        deleteAllRecordsFromProviderByTable(ContactGroupEntry.CONTENT_URI, ContactGroupEntry.TABLE_NAME);
        deleteAllRecordsFromProviderByTable(ContactEntry.CONTENT_URI, ContactEntry.TABLE_NAME);
        deleteAllRecordsFromProviderByTable(PrayerRequestEntry.CONTENT_URI, PrayerRequestEntry.TABLE_NAME);
        deleteAllRecordsFromProviderByTable(PrayerListEntry.CONTENT_URI, PrayerListEntry.TABLE_NAME);
    }

    private void deleteAllRecordsFromProviderByTable(Uri uri, String tableName) {
        mContext.getContentResolver().delete(uri, null, null);
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        assertEquals("Error: Records not deleted from " + tableName +" table during delete", 0, cursor.getCount());
        cursor.close();
    }

    /*
        This test checks to make sure that the content provider is registered correctly.
     */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        
        
        ComponentName componentName = new ComponentName(
                                                mContext.getPackageName(),
                                                AppContentProvider.class.getName()
                                            );
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            assertEquals("Error: AppContentProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + DatabaseContract.CONTENT_AUTHORITY,
                    providerInfo.authority, DatabaseContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: AppContentProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
        This test doesn't touch the database.  It verifies that the ContentProvider returns
        the correct type for each type of URI that it can handle.
     */
    public void testGetType() {
        // Verify CONTENT_TYPES
        assertUriType(BibleVerseEntry.CONTENT_URI, BibleVerseEntry.CONTENT_TYPE);
        assertUriType(ContactEntry.CONTENT_URI, ContactEntry.CONTENT_TYPE);
        assertUriType(ContactGroupEntry.CONTENT_URI, ContactGroupEntry.CONTENT_TYPE);
        assertUriType(PrayerRequestEntry.CONTENT_URI, PrayerRequestEntry.CONTENT_TYPE);
        assertUriType(PrayerListEntry.CONTENT_URI, PrayerListEntry.CONTENT_TYPE);
        assertUriType(PrayerRequestVerseEntry.CONTENT_URI, PrayerRequestVerseEntry.CONTENT_TYPE);
        assertUriType(PrayerListRequestEntry.CONTENT_URI, PrayerListRequestEntry.CONTENT_TYPE);

        assertUriType(BibleVerseEntry.buildWithPassageUri(TEST_PASSAGE), BibleVerseEntry.CONTENT_ITEM_TYPE);

        assertUriType(ContactEntry.buildExistsUri(), ContactEntry.CONTENT_TYPE);
        assertUriType(ContactEntry.buildUri(), ContactEntry.CONTENT_TYPE);
        assertUriType(ContactEntry.buildWithIdUri(1), ContactEntry.CONTENT_ITEM_TYPE);

        assertUriType(ContactGroupEntry.buildExistsUri(), ContactGroupEntry.CONTENT_TYPE);
        assertUriType(ContactGroupEntry.buildUri(), ContactGroupEntry.CONTENT_TYPE);
        assertUriType(ContactGroupEntry.buildWithIdUri(1), ContactGroupEntry.CONTENT_ITEM_TYPE);
        assertUriType(ContactGroupEntry.buildWithIdUri("1"), ContactGroupEntry.CONTENT_ITEM_TYPE);

        assertUriType(PrayerRequestEntry.buildWithIdUri(1), PrayerRequestEntry.CONTENT_ITEM_TYPE);

        assertUriType(PrayerListEntry.buildWithIdUri(1), PrayerListEntry.CONTENT_ITEM_TYPE);

        assertUriType(PrayerRequestVerseEntry.buildWithIdUri(1), PrayerRequestVerseEntry.CONTENT_ITEM_TYPE);
        assertUriType(PrayerListRequestEntry.buildWithIdUri(1), PrayerListRequestEntry.CONTENT_ITEM_TYPE);
    }

    private void assertUriType(Uri uri, String entryType) {
        String type = mContext.getContentResolver().getType(uri);

        assertEquals("Error: the " + uri.getLastPathSegment().toUpperCase() + " CONTENT_URI should return " + entryType,
                entryType, type);
    }

    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.
     */
    public void testBasicQueries() {

        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        testBasicQueryByTable(db, BibleVerseEntry.CONTENT_URI,
                BibleVerseEntry.TABLE_NAME, TestUtilities.createBibleVerseValues());
        testBasicQueryByTable(db, ContactEntry.CONTENT_URI,
                ContactEntry.TABLE_NAME, TestUtilities.createContactValues());
        testBasicQueryByTable(db, ContactGroupEntry.CONTENT_URI,
                ContactGroupEntry.TABLE_NAME, TestUtilities.createContactGroupValues());
        testBasicQueryByTable(db, PrayerRequestEntry.CONTENT_URI,
                PrayerRequestEntry.TABLE_NAME, TestUtilities.createPrayerRequestValues());
        testBasicQueryByTable(db, PrayerListRequestEntry.CONTENT_URI,
                PrayerListRequestEntry.TABLE_NAME, TestUtilities.createPrayerListRequestValues());
        testBasicQueryByTable(db, PrayerRequestVerseEntry.CONTENT_URI,
                PrayerRequestVerseEntry.TABLE_NAME, TestUtilities.createPrayerRequestVerseValues());
        testBasicQueryByTable(db, PrayerListEntry.CONTENT_URI,
                PrayerListEntry.TABLE_NAME, TestUtilities.createPrayerListValues());
    }

    private void testBasicQueryByTable(SQLiteDatabase db, Uri uri, String tableName, ContentValues contentValues) {
        long rowId = db.insert(tableName, null, contentValues);
        assertTrue("Unable to Insert " + tableName + "Entry into the Database", rowId != -1);
        db.close();
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        TestUtilities.validateCursor("testBasicQuery for " + tableName, cursor, contentValues);
    }

    /*
        This test uses the provider to insert and then update the data.
     */
    public void testUpdates() {
        long rowId = assertInsertByTable(PrayerRequestEntry.CONTENT_URI, TestUtilities.createPrayerRequestValues());
        Uri updateUri = PrayerRequestEntry.buildWithIdUri(rowId);
        Log.d(LOG_TAG, "updateUri: " + updateUri.getPath());
        assertUpdateByTable(PrayerRequestEntry.TABLE_NAME, updateUri, PrayerRequestEntry._ID, TestUtilities.updatePrayerRequestValues());
    }

    private long assertInsertByTable(Uri uri, ContentValues insertValues) {
        Uri insertUri = mContext.getContentResolver().insert(uri, insertValues);
        Log.d(LOG_TAG, "insertUri: " + insertUri.getPath());
        long rowId = ContentUris.parseId(insertUri);
        assertTrue(rowId != -1);
        Log.d(LOG_TAG, "New row id: " + rowId);
        return rowId;
    }

    private void assertUpdateByTable(String tableName, Uri uri, String column,
                                     ContentValues updateValues) {

        Cursor updateCursor = mContext.getContentResolver().query(uri, null, null, null, null);
        long rowId = Long.parseLong(uri.getLastPathSegment());

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        updateCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                uri,
                updateValues,
                column + "= ?",
                new String[] { Long.toString(rowId) });
        assertEquals(count, 1);
        tco.waitForNotificationOrFail();
        updateCursor.unregisterContentObserver(tco);
        updateCursor.close();

        Cursor cursor = mContext.getContentResolver().query(
                uri,
                null,
                column + " = " + Long.toString(rowId),
                null,
                null
        );

        TestUtilities.validateCursor("testUpdate"+tableName+".  Error validating entry update.",
                cursor, updateValues);

        cursor.close();
    }


    public void testInsertReadProvider() {

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        Uri uri = PrayerRequestEntry.CONTENT_URI;
        ContentValues contentValues = TestUtilities.createPrayerRequestValues();

        mContext.getContentResolver().registerContentObserver(uri, true, tco);

        Uri insertUri = mContext.getContentResolver().insert(uri, contentValues);
        assertTrue(insertUri != null);
        
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        
        Cursor PrayerRequestsCursor = mContext.getContentResolver().query(
                uri,  
                null, 
                null, 
                null, 
                null 
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating PrayerRequestEntry insert.",
                PrayerRequestsCursor, contentValues);

        
        PrayerRequestsCursor = mContext.getContentResolver().query(
                uri,
                null, 
                null, 
                null, 
                null  
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined PrayerRequests and Location Data.",
                PrayerRequestsCursor, contentValues);
    }

    
    
    
    
    
    public void testDeleteRecords() {
        testInsertReadProvider();

        
        TestUtilities.TestContentObserver PrayerRequestsObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(PrayerRequestEntry.CONTENT_URI, true, PrayerRequestsObserver);

        deleteAllRecords();

        PrayerRequestsObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(PrayerRequestsObserver);
    }


    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertcontentValues() {
        String currentTestID = "1";
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, currentTestID+= "1" ) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PrayerRequestEntry.COLUMN_TITLE, "Once Upon a Time..."+i);
            contentValues.put(PrayerRequestEntry.COLUMN_DESC, "PrayerRequest " + i);
            contentValues.put(PrayerRequestEntry.COLUMN_CONTACT, 1);
            if(i>5) {
                contentValues.put(PrayerRequestEntry.COLUMN_END_DATE, 1504462605 + i);
                contentValues.put(PrayerRequestEntry.COLUMN_ANSWERED, 1504462605 + (2 * i));
            }
            returnContentValues[i] = contentValues;
        }
        return returnContentValues;
    }

    public void testBulkInsert() {
        ContentValues[] bulkInsertContentValues = createBulkInsertcontentValues();

        TestUtilities.TestContentObserver PrayerRequestsObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(PrayerRequestEntry.CONTENT_URI, true, PrayerRequestsObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(PrayerRequestEntry.CONTENT_URI, bulkInsertContentValues);

        PrayerRequestsObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(PrayerRequestsObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        Cursor cursor = mContext.getContentResolver().query(
                PrayerRequestEntry.CONTENT_URI,
                null, 
                null, 
                null, 
                null
        );

        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating PrayerRequestEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}
