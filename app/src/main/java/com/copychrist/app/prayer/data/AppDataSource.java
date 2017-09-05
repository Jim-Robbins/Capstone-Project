package com.copychrist.app.prayer.data;

import android.support.annotation.NonNull;

import com.copychrist.app.prayer.data.model.Contact;
import com.copychrist.app.prayer.data.model.ContactGroup;

import java.util.List;

/**
 * Created by jim on 8/29/17.
 */

public interface AppDataSource {

    /**
     * ContactGroup Interfaces
     */
    interface GetContactGroupsCallback {
        void onContactGroupsLoaded(List<ContactGroup> contactGroups);
        void onContactGroupDataNotAvailable();
    }
    interface GetContactGroupCallback {
        void onContactGroupLoaded(ContactGroup contactGroup);
        void onContactGroupDataNotAvailable();
    }
    void getContactGroups(@NonNull GetContactGroupsCallback callback);
    void getContactGroup(@NonNull long groupId, @NonNull GetContactGroupCallback callback);
    void getContactGroup(@NonNull String groupId, @NonNull GetContactGroupCallback callback);
    String saveContactGroup(@NonNull ContactGroup contactGroup);
    void sortContactGroups(@NonNull List<ContactGroup> contactGroups);
    void deleteAllContactGroups();
    void deleteContactGroup(@NonNull ContactGroup contactGroup);

    /**
     * Contact Interfaces
     */
    interface GetContactsCallback {
        void onContactsLoaded(List<Contact> contacts);
        void onContactDataNotAvailable();
    }
    interface GetContactCallback {
        void onContactLoaded(Contact contact);
        void onContactDataNotAvailable();
    }

    void getContacts(@NonNull GetContactsCallback callback);
    void getContact(@NonNull long contactId, @NonNull GetContactCallback callback);
    void getContact(@NonNull String contactId, @NonNull GetContactCallback callback);
    String saveContact(@NonNull Contact contact);
    void deleteContact(@NonNull Contact contact);
}
