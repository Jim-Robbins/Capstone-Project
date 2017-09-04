package com.copychrist.app.prayer.data.remote;

import android.support.annotation.NonNull;

import com.copychrist.app.prayer.data.AppDataSource;
import com.copychrist.app.prayer.data.model.ContactGroup;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jim on 9/3/17.
 */

@Singleton
public class AppRemoteDataSource implements AppDataSource {

//    @Inject
    public AppRemoteDataSource() {
    }

    @Override
    public void getContactGroups(@NonNull GetContactGroupsCallback callback) {

    }

    @Override
    public void getContactGroup(@NonNull long groupId, @NonNull GetContactGroupCallback callback) {

    }

    @Override
    public void getContactGroup(@NonNull String groupId, @NonNull GetContactGroupCallback callback) {

    }

    @Override
    public String saveContactGroup(@NonNull ContactGroup contactGroup) {
        return "";
    }

    @Override
    public void sortContactGroups(@NonNull List<ContactGroup> contactGroups) {

    }

    @Override
    public void refreshContactGroups() {

    }

    @Override
    public void deleteAllContactGroups() {
    }

    @Override
    public void deleteContactGroup(@NonNull ContactGroup contactGroup) {
    }

}
