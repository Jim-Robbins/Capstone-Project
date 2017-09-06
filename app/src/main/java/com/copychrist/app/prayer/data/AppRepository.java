package com.copychrist.app.prayer.data;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.copychrist.app.prayer.data.local.Local;
import com.copychrist.app.prayer.data.model.Contact;
import com.copychrist.app.prayer.data.model.ContactGroup;
import com.copychrist.app.prayer.data.remote.Remote;

import java.util.List;

import javax.inject.Singleton;

import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jim on 9/3/17.
 */

@Singleton
public class AppRepository implements AppDataSource {

    private final AppDataSource remoteDataSource;
    private final AppDataSource localDataSource;

    public AppRepository(@Remote AppDataSource tasksRemoteDataSource,
                    @Local AppDataSource tasksLocalDataSource) {
        remoteDataSource = tasksRemoteDataSource;
        localDataSource = tasksLocalDataSource;
    }

    /**
     * Gets contactGroups from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p/>
     * Note: {@link GetContactGroupsCallback#onContactGroupDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getContactGroups(@NonNull final GetContactGroupsCallback callback) {
        //Todo: Load from server
        localDataSource.getContactGroups(new GetContactGroupsCallback() {
            @Override
            public void onContactGroupsLoaded(List<ContactGroup> contactGroups) {
//                refreshLocalDataSource(contactGroups);
                callback.onContactGroupsLoaded(null);
            }

            @Override
            public void onContactGroupDataNotAvailable() {
                callback.onContactGroupDataNotAvailable();
            }
        });
    }

    @Override
    public void getContactGroup(@NonNull long groupId, @NonNull final GetContactGroupCallback callback) {
        // Todo:Load from server
        localDataSource.getContactGroup(groupId, new GetContactGroupCallback() {

            @Override
            public void onContactGroupLoaded(ContactGroup contactGroup) {
                callback.onContactGroupLoaded(contactGroup);
            }

            @Override
            public void onContactGroupDataNotAvailable() {
                callback.onContactGroupDataNotAvailable();
            }
        });
    }

    @Override
    public void getContactGroup(@NonNull String groupId, @NonNull GetContactGroupCallback callback) {
        getContactGroup(Long.getLong(groupId), callback);
    }

    @Override
    public String saveContactGroup(@NonNull ContactGroup contactGroup) {
        String result;
        remoteDataSource.saveContactGroup(checkNotNull(contactGroup));
        result = localDataSource.saveContactGroup(checkNotNull(contactGroup));
        return result;
    }

    @Override
    public void sortContactGroups(@NonNull List<ContactGroup> contactGroups) {
        remoteDataSource.sortContactGroups(checkNotNull(contactGroups));
        localDataSource.sortContactGroups(checkNotNull(contactGroups));
    }

    @Override
    public void deleteAllContactGroups() {
        remoteDataSource.deleteAllContactGroups();
        localDataSource.deleteAllContactGroups();
    }

    @Override
    public void deleteContactGroup(@NonNull ContactGroup contactGroup) {
        remoteDataSource.deleteContactGroup(checkNotNull(contactGroup));
        localDataSource.deleteContactGroup(checkNotNull(contactGroup));
    }

    private void refreshLocalDataSource(List<ContactGroup> contactGroups) {
        for (ContactGroup contactGroup : contactGroups) {
            localDataSource.saveContactGroup(contactGroup);
        }
    }

    @Override
    public void getContacts(@NonNull final GetContactsCallback callback) {
        localDataSource.getContacts(new GetContactsCallback() {
            @Override
            public void onContactsLoaded(List<Contact> contacts) {
//                refreshLocalDataSource(contacts);
                callback.onContactsLoaded(null);
            }

            @Override
            public void onContactDataNotAvailable() {
                callback.onContactDataNotAvailable();
            }
        });
    }

    private static final String TAG = "AppRepository";
    @Override
    public void getContact(@NonNull long contactId, @NonNull final GetContactCallback callback) {
        Timber.d(TAG, "getContact() called with: contactId = [" + contactId + "], callback = [" + callback + "]");
        localDataSource.getContact(contactId, new GetContactCallback() {

            @Override
            public void onContactLoaded(Contact contact) {
                Timber.d(TAG, "GetContactCallback().onContactLoaded()");
                callback.onContactLoaded(contact);
            }

            @Override
            public void onContactDataNotAvailable() {
                Timber.d(TAG, "GetContactCallback().onContactDataNotAvailable()");
                callback.onContactDataNotAvailable();
            }
        });
    }

    @Override
    public void getContact(@NonNull String contactId, @NonNull GetContactCallback callback) {
        getContact(Long.getLong(contactId), callback);
    }

    @Override
    public String saveContact(@NonNull Contact contact) {
        String result;
        remoteDataSource.saveContact(checkNotNull(contact));
        result = localDataSource.saveContact(checkNotNull(contact));
        return result;
    }

    @Override
    public void deleteContact(@NonNull Contact contact) {
        remoteDataSource.deleteContact(checkNotNull(contact));
        localDataSource.deleteContact(checkNotNull(contact));
    }

    public interface ContactsActivityLoadDataCallback {
        void onContactGroupDataLoaded(Cursor data);
        void onContactGroupDataEmpty();
        void onContactGroupDataNotAvailable();
        void onContactGroupDataReset();

        void onContactDataLoaded(Cursor data);
        void onContactDataEmpty();
        void onContactDataNotAvailable();
        void onContactDataReset();
    }

    //================ Contact Detail View ==============

    public interface ContactDetailActivityLoadDataCallback {
        void onContactDetailDataLoaded(Cursor data);
        void onContactDetailDataEmpty();
        void onContactDetailDataNotAvailable();
        void onContactDetailDataReset();
    }
}
