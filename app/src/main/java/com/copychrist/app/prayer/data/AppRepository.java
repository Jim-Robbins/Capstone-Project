package com.copychrist.app.prayer.data;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.copychrist.app.prayer.data.local.Local;
import com.copychrist.app.prayer.data.model.ContactGroup;
import com.copychrist.app.prayer.data.remote.Remote;

import java.util.List;

import javax.inject.Singleton;

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
                refreshLocalDataSource(contactGroups);
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
    public void getContactGroup(@NonNull String groupId, @NonNull final GetContactGroupCallback callback) {
        // Load from server
        remoteDataSource.getContactGroup(groupId, new GetContactGroupCallback() {
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
    public void refreshContactGroups() {

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

    public interface LoadDataCallback {
        void onDataLoaded(Cursor data);

        void onDataEmpty();

        void onDataNotAvailable();

        void onDataReset();
    }
}
