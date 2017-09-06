package com.copychrist.app.prayer.data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.copychrist.app.prayer.data.local.DatabaseContract.ContactEntry;
import com.copychrist.app.prayer.data.local.DatabaseContract.ContactGroupEntry;
import com.copychrist.app.prayer.data.local.DatabaseContract.PrayerRequestEntry;
import com.copychrist.app.prayer.ui.contact.ContactFilter;

import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jim on 9/3/17.
 */

public class LoaderProvider {
    private static final String TAG = "LoaderProvider";
    @NonNull
    private final Context context;

    public LoaderProvider(@NonNull Context context) {
        this.context = checkNotNull(context, "context cannot be null");
    }

    public Loader<Cursor> createContactGroupsLoader() {
        String selection = null;
        String[] selectionArgs = null;

        return new CursorLoader(
                context,
                ContactGroupEntry.buildUri(),
                ContactGroupEntry.CONTACT_GROUP_COLUMNS,
                selection, selectionArgs,
                null
        );
    }

    public Loader<Cursor> createFilteredContactsLoader(Long groupId) {
        String selection = ContactEntry.COLUMN_GROUP + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(groupId)};

        return new CursorLoader(
                context,
                ContactEntry.buildUri(),
                ContactEntry.CONTACT_COLUMNS,
                selection, selectionArgs,
                null
        );
    }

    public Loader<Cursor> createFilteredContactLoader(Long contactId, ContactFilter currentFilter) {
        Timber.d(TAG, "createFilteredContactLoader() called with: contactId = [" + contactId + "], currentFilter = [" + currentFilter + "]");
        String selection = null;
        String[] selectionArgs = null;

        switch (currentFilter.getFilterType()) {
            case ALL:
                selection =  ContactEntry.TABLE_NAME + "." + ContactEntry._ID + " = ? ";
                selectionArgs = new String[]{String.valueOf(contactId)};
                break;
            case REQUESTS:
                selection = "(" + PrayerRequestEntry.COLUMN_ANSWERED + " is null OR " +
                        PrayerRequestEntry.COLUMN_ANSWERED + " = ? ) AND " +
                        ContactEntry.TABLE_NAME + "." + ContactEntry._ID + " = ? ";
                selectionArgs = new String[]{"", String.valueOf(contactId)};
                break;
            case ARCHIVED:
                selection = "(" + PrayerRequestEntry.COLUMN_ANSWERED + " is not null AND " +
                        PrayerRequestEntry.COLUMN_ANSWERED + " != ? ) AND " +
                        ContactEntry.TABLE_NAME + "." + ContactEntry._ID + " = ? ";
                selectionArgs = new String[]{"", String.valueOf(contactId)};
                break;
        }

        return new CursorLoader(
                context,
                ContactEntry.buildWithIdUri(contactId),
                ContactEntry.CONTACT_REQUEST_COLUMNS,
                selection, selectionArgs,
                null
        );
    }
}
