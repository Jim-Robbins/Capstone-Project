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

import javax.inject.Singleton;

import static com.copychrist.app.prayer.data.local.DatabaseContract.ContactEntry;
import static com.copychrist.app.prayer.data.local.DatabaseContract.ContactGroupEntry;
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
                    ContactGroupEntry.buildUri(),
                    values,
                    ContactGroupEntry._ID + " = ? ",
                    new String[] { Long.toString(contactGroup.getId()) }
            );
            uri = ContactGroupEntry.buildWithIdUri(rowId);
        } else {
            uri = contentResolver.insert(ContactGroupEntry.buildUri(), values);
        }
        return ContactGroupEntry.getResultFromUri(uri);
    }

    @Override
    public void sortContactGroups(@NonNull List<ContactGroup> contactGroups) {
        //Todo: Let User Sort groups
    }

    @Override
    public void deleteAllContactGroups() {
        contentResolver.delete(ContactGroupEntry.buildUri(), null, null);
    }

    @Override
    public void deleteContactGroup(@NonNull ContactGroup contactGroup) {
        String selection = ContactGroupEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(contactGroup.getId())};

        contentResolver.delete(ContactGroupEntry.buildUri(), selection, selectionArgs);
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
        callback.onContactLoaded(null);
    }

    @Override
    public void getContact(@NonNull String contactId, @NonNull GetContactCallback callback) {
        callback.onContactLoaded(null);
    }

    @Override
    public String saveContact(@NonNull Contact contact) {
        ContentValues values = Contact.from(contact);
        Uri uri;
        if(contact.getId() > 0) {
            int rowId = contentResolver.update(
                    ContactEntry.buildUri(),
                    values,
                    ContactGroupEntry._ID + " = ? ",
                    new String[] { Long.toString(contact.getId()) }
            );
            uri = ContactEntry.buildWithIdUri(rowId);
        } else {
            uri = contentResolver.insert(ContactEntry.buildUri(), values);
        }
        return ContactEntry.getIdFromUri(uri);
    }

    @Override
    public void deleteContact(@NonNull Contact contact) {
        String selection = ContactEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(contact.getId())};

        contentResolver.delete(ContactEntry.buildWithIdUri(contact.getId()), selection, selectionArgs);
    }
}
