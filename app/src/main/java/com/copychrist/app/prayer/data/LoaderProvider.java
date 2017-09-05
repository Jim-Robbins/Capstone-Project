package com.copychrist.app.prayer.data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.copychrist.app.prayer.data.local.DatabaseContract;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jim on 9/3/17.
 */

public class LoaderProvider {
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
                DatabaseContract.ContactGroupEntry.buildUri(),
                DatabaseContract.ContactGroupEntry.CONTACT_GROUP_COLUMNS,
                selection, selectionArgs,
                null
        );
    }

    public Loader<Cursor> createFilteredContactsLoader(Long groupId) {
        String selection = DatabaseContract.ContactEntry.COLUMN_GROUP + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(groupId)};

        return new CursorLoader(
                context,
                DatabaseContract.ContactEntry.buildUri(),
                DatabaseContract.ContactEntry.CONTACT_COLUMNS,
                selection, selectionArgs,
                null
        );
    }
}
