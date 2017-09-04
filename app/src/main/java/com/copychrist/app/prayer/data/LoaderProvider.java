package com.copychrist.app.prayer.data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.copychrist.app.prayer.data.local.DatabaseContract;
import com.copychrist.app.prayer.ui.contactgroups.ContactGroupFilter;

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

    public Loader<Cursor> createFilteredContactGroupsLoader(ContactGroupFilter contactGroupFilter) {
        String selection = null;
        String[] selectionArgs = null;

        switch (contactGroupFilter.getFilterType()) {
            case ALL:
            default:
                selection = null;
                selectionArgs = null;
                break;
        }

        return new CursorLoader(
                context,
                DatabaseContract.ContactGroupEntry.buildUri(),
                DatabaseContract.ContactGroupEntry.CONTACT_GROUP_COLUMNS, selection, selectionArgs, null
        );
    }

    public Loader<Cursor> createContactGroupLoader(String taskId) {
        return new CursorLoader(context, DatabaseContract.ContactGroupEntry.buildWithIdUri(taskId),
                null,
                null,
                new String[]{String.valueOf(taskId)}, null
        );
    }
}
