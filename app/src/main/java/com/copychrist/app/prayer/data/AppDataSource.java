package com.copychrist.app.prayer.data;

import android.support.annotation.NonNull;

import com.copychrist.app.prayer.data.model.Contact;
import com.copychrist.app.prayer.data.model.ContactGroup;

import java.util.List;

/**
 * Created by jim on 8/29/17.
 */

public interface AppDataSource {

    interface LoadContactGroupsCallback {
        void onContactGroupsLoaded(List<ContactGroup> contactGroups);
        void onContactGroupDataNotAvailable();
    }

    void getContactGroups(@NonNull LoadContactGroupsCallback callback);

    interface GetContactCallback {
        void onContactLoaded(Contact contact);
        void onContactDataNotAvailable();
    }

    void getContact(@NonNull long contactId, @NonNull GetContactCallback callback);
}
