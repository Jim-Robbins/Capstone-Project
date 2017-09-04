package com.copychrist.app.prayer.ui.contactgroups;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.copychrist.app.prayer.data.AppDataSource;
import com.copychrist.app.prayer.data.LoaderProvider;
import com.copychrist.app.prayer.data.AppRepository;
import com.copychrist.app.prayer.data.local.DatabaseContract;
import com.copychrist.app.prayer.data.model.Contact;
import com.copychrist.app.prayer.data.model.ContactGroup;
import com.copychrist.app.prayer.data.model.PrayerRequest;

import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jim on 8/14/17.
 */

public class ContactsPresenter implements ContactGroupContract.Presenter, AppRepository.LoadDataCallback,
        AppDataSource.GetContactGroupsCallback, LoaderManager.LoaderCallbacks<Cursor> {

    public final static int DATA_LOADER = 1;

    private ContactGroupContract.View view;

    @Inject
    Context context;

    @NonNull
    private final AppRepository appRepository;

    @NonNull
    private final LoaderManager loaderManager;

    @NonNull
    private final LoaderProvider loaderProvider;

    private ContactGroupFilter currentFiltering;

    private ContactGroup selectedContactGroup;

    public ContactsPresenter(@NonNull LoaderProvider loaderProvider,
                             @NonNull LoaderManager loaderManager,
                             @NonNull AppRepository repository,
                             @NonNull ContactGroupContract.View view,
                             @NonNull ContactGroupFilter filter,
                             @NonNull long contactGroupId) {
        this.loaderProvider = checkNotNull(loaderProvider, "loaderProvider provider cannot be null");
        this.loaderManager = checkNotNull(loaderManager, "loaderManager provider cannot be null");
        this.appRepository = checkNotNull(repository, "repository provider cannot be null");
        this.view = checkNotNull(view, "view cannot be null!");
        this.currentFiltering = filter;
        this.view.setPresenter(this);

        selectedContactGroup = new ContactGroup(contactGroupId,"","",0);
    }

    @Override
    public void setContactGroup(@NonNull ContactGroup contactGroup) {
        selectedContactGroup = contactGroup;
    }

//    @Override
//    public void result(int requestCode, int resultCode) {
//        // If a task was successfully added, show snackbar
//        if (AddContactGroupDialogFragment.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
//            view.showSuccessfullySavedMessage();
//        }
//    }

    @Override
    public void start() {
        loadContactGroups(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return loaderProvider.createFilteredContactGroupsLoader(currentFiltering);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            if (data.moveToLast()) {
                onDataLoaded(data);
            } else {
                onDataEmpty();
            }
        } else {
            onDataNotAvailable();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        onDataReset();
    }

//    @Override
//    public void setView(ContactGroupContract.View view) {
//
//    }
//
//    @Override
//    public void clearView() {
//
//    }

    @Override
    public void onContactGroupsLoaded(List<ContactGroup> contactGroups) {
        // we don't care about the result since the CursorLoader will load the data for us
        if (loaderManager.getLoader(DATA_LOADER) == null) {
            loaderManager.initLoader(DATA_LOADER, null, this);
        } else {
            loaderManager.restartLoader(DATA_LOADER, null, this);
        }
    }

    @Override
    public void onContactGroupDataNotAvailable() {
        view.setLoadingIndicator(false);
        view.showLoadingError();
    }

    @Override
    public void loadContactGroups(boolean forceUpdate) {
        view.setLoadingIndicator(true);
        appRepository.getContactGroups(this);
    }

    @Override
    public void addNewContactGroup() {
        view.showAddContactGroupDialog();
    }

    @Override
    public void openContactGroupDetails() {
        view.showEditContactGroupDialog(selectedContactGroup);
    }

    @Override
    public void saveContactGroup(@NonNull ContactGroup contactGroup) {
        String result = appRepository.saveContactGroup(contactGroup);
        if(result == DatabaseContract.ALREADY_EXISTS) {
            view.showLoadingError();
        } else {
            view.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void deleteContactGroup(@NonNull ContactGroup contactGroup) {
        appRepository.deleteContactGroup(contactGroup);
    }

    @Override
    public void deleteContactGroupConfirm() {
        checkNotNull(selectedContactGroup, "contact group cannot be null!");
        view.showDeleteContactGroupDialog(selectedContactGroup);
    }

    @Override
    public void addNewContact() {
        checkNotNull(selectedContactGroup, "contact group cannot be null!");
        view.showAddNewContactView(selectedContactGroup.getName());
    }

    @Override
    public void saveContact(@NonNull Contact contact) {

    }

    @Override
    public void openContactDetails(@NonNull Contact contact) {
        checkNotNull(contact, "contact cannot be null!");
        view.showContactDetailView(contact.getId());
    }

    @Override
    public void openPrayerRequestDetails(@NonNull PrayerRequest prayerRequest) {
        checkNotNull(prayerRequest, "prayerRequest cannot be null!");
        view.showPrayerRequestDetailView(prayerRequest.getId());
    }

    @Override
    public void onDataLoaded(Cursor data) {
        view.setLoadingIndicator(false);
        // Show the list of tasks
        view.showContactGroupsTabs(data, selectedContactGroup);
    }

    @Override
    public void onDataEmpty() {
        view.setLoadingIndicator(false);
        // Show a message indicating there are no tasks for that filter type.
        processEmptyTasks();
    }

    private void processEmptyTasks() {
        switch (currentFiltering.getFilterType()) {
            default:
                view.showNoContactGroups();
                break;
        }
    }

    @Override
    public void onDataNotAvailable() {
        view.setLoadingIndicator(false);
        view.showLoadingError();
    }

    @Override
    public void onDataReset() {
        view.showContactGroupsTabs(null, selectedContactGroup);
    }

//    @Override
//    public void setView(final ContactGroupContract.View view) {
////        this.view = view;
//////        selectedContactGroup = dataSource.getContactGroup(contactGroupId);
////        showContacts();
////        showContactGroupsTabs();
//    }
//
//    private void showContacts() {
//        if(!contactsShown) {
////            view.showContacts(selectedContactGroup.getContacts());
//            contactsShown = true;
//        }
//    }
//
//    private void showContactGroupsTabs() {
//        if(!contactGroupsShown) {
////            view.showContactGroupsTabs(dataSource.getAllContactGroups(), selectedContactGroup);
//            contactGroupsShown = true;
//        }
//    }
//
//    @Override
//    public void onContactGroupClicked(long contactGroupId) {
//        if(contactGroupId != selectedContactGroup.getId()) {
//            contactsShown = false;
//  //          selectedContactGroup = dataSource.getContactGroup(contactGroupId);
//            showContacts();
//        }
//    }
//
//    @Override
//    public void onAddNewContactGroupClick() {
//        view.showAddContactGroupDialog();
//    }
//
//    @Override
//    public void onEditContactGroupClick() {
//        view.showEditContactGroupDialog(selectedContactGroup);
//    }
//
//    @Override
//    public void onSaveContactGroupClick(long contactGroupId, String groupName, String groupDesc, int sortOrder) {
//        if(contactGroupId > 0) {
//            //dataSource.editContactGroup(contactGroupId, groupName, groupDesc, sortOrder);
//        } else {
//            //dataSource.addContactGroup(groupName, groupDesc);
//        }
//        contactGroupsShown = false;
//        showContactGroupsTabs();
//    }
//
//    @Override
//    public void onDeleteContactGroupClick() {
//        view.showDeleteContactGroupDialog(selectedContactGroup);
//    }
//
//    @Override
//    public void onDeleteContactGroupConfirmed() {
//        //dataSource.deleteContactGroup(selectedContactGroup.getId());
//        //selectedContactGroup = dataSource.getAllContactGroups().first();
//        contactGroupsShown = false;
//        contactsShown = false;
//        showContactGroupsTabs();
//        showContacts();
//    }
//
//    @Override
//    public void onAddNewContactClick() {
//        view.showAddNewContactView(selectedContactGroup.getName());
//    }
//
//    @Override
//    public void onSaveContactClick(String firstName, String lastName, String groupName, String pictureUrl) {
//        //dataSource.addContact(firstName, lastName, pictureUrl, groupName, this);
//    }
//
//    @Override
//    public void onContactClick(long id) {
//        view.showContactDetailView(id);
//    }
//
//    @Override
//    public void onPrayerRequestClick(long requestId) {
//        view.showPrayerRequestDetailView(requestId);
//    }
////
////    @Override
////    public void onDBSuccess() {
////        view.showRealmResultMessage("ContactEntry Added");
////    }
//
////    @Override
////    public void onDBError(Throwable e) {
////        view.showRealmResultMessage("Failed to add contact");
////    }
//
//    @Override
//    public void clearView() {
//        view = new ContactsView.EmptyMyListView();
//    }
}
