package com.copychrist.app.prayer.data.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.copychrist.app.prayer.data.AppDataSource;
import com.copychrist.app.prayer.data.model.Contact;
import com.copychrist.app.prayer.data.model.ContactGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.copychrist.app.prayer.data.local.DatabaseContract.ContactGroupEntry.getResultFromUri;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jim on 9/3/17.
 */

@Singleton
public class AppLocalDataSource implements AppDataSource {

    private ContentResolver contentResolver;

    public AppLocalDataSource(@NonNull ContentResolver contentResolver) {
        checkNotNull(contentResolver);
        this.contentResolver = contentResolver;
    }

    /**
     * ContactGroup Interfaces
     */
    @Override
    public void getContactGroups(@NonNull GetContactGroupsCallback callback) {
        callback.onContactGroupsLoaded(new ArrayList<ContactGroup>());
    }

    @Override
    public void getContactGroup(@NonNull long groupId, @NonNull GetContactGroupCallback callback) {
        // no-op since the data is loaded via Cursor Loader
    }

    @Override
    public void getContactGroup(@NonNull String groupId, @NonNull GetContactGroupCallback callback) {
        // no-op since the data is loaded via Cursor Loader
    }

    @Override
    public String saveContactGroup(@NonNull ContactGroup contactGroup) {
        ContentValues values = ContactGroup.from(contactGroup);
        Uri uri;
        if(contactGroup.getId() > 0) {
            int rowId = contentResolver.update(
                    DatabaseContract.ContactGroupEntry.buildUri(),
                    values,
                    DatabaseContract.ContactGroupEntry._ID + " = ? ",
                    new String[] { Long.toString(contactGroup.getId()) }
            );
            uri = DatabaseContract.ContactGroupEntry.buildWithIdUri(rowId);
        } else {
            uri = contentResolver.insert(DatabaseContract.ContactGroupEntry.buildUri(), values);
        }
        return getResultFromUri(uri);
    }

    @Override
    public void sortContactGroups(@NonNull List<ContactGroup> contactGroups) {
        //Todo: Let User Sort groups
    }

    @Override
    public void deleteAllContactGroups() {
        contentResolver.delete(DatabaseContract.ContactGroupEntry.buildUri(), null, null);
    }

    @Override
    public void deleteContactGroup(@NonNull ContactGroup contactGroup) {
        String selection = DatabaseContract.ContactGroupEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(contactGroup.getId())};

        contentResolver.delete(DatabaseContract.ContactGroupEntry.buildUri(), selection, selectionArgs);
    }

    /**
     * Contact Interfaces
     */
    @Override
    public void getContacts(@NonNull GetContactsCallback callback) {
        callback.onContactsLoaded(new ArrayList<Contact>());
    }

    @Override
    public void getContact(@NonNull long contactId, @NonNull GetContactCallback callback) {

    }

    @Override
    public void getContact(@NonNull String contactId, @NonNull GetContactCallback callback) {

    }

    @Override
    public String saveContact(@NonNull Contact contact) {
        ContentValues values = Contact.from(contact);
        Uri uri;
        if(contact.getId() > 0) {
            int rowId = contentResolver.update(
                    DatabaseContract.ContactEntry.buildUri(),
                    values,
                    DatabaseContract.ContactGroupEntry._ID + " = ? ",
                    new String[] { Long.toString(contact.getId()) }
            );
            uri = DatabaseContract.ContactEntry.buildWithIdUri(rowId);
        } else {
            uri = contentResolver.insert(DatabaseContract.ContactEntry.buildUri(), values);
        }
        return getResultFromUri(uri);
    }

    @Override
    public void deleteContact(@NonNull Contact contact) {
        String selection = DatabaseContract.ContactEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(contact.getId())};

        contentResolver.delete(DatabaseContract.ContactEntry.buildWithIdUri(contact.getId()), selection, selectionArgs);
    }
}
