package com.copychrist.app.prayer.ui.contactgroups;

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
import com.copychrist.app.prayer.ui.contact.ContactFilter;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jim on 8/14/17.
 */

public class ContactsPresenter implements ContactGroupContract.Presenter,
        LoaderManager.LoaderCallbacks<Cursor>, AppRepository.ContactsActivityLoadDataCallback,
        AppDataSource.GetContactGroupsCallback,
        AppDataSource.GetContactsCallback {

    public final static int CONTACT_GROUP_LOADER = 1;
    public final static int CONTACT_LOADER = 2;

    private ContactGroupContract.View view;

    @NonNull
    private final AppRepository appRepository;

    @NonNull
    private final LoaderManager loaderManager;

    @NonNull
    private final LoaderProvider loaderProvider;

    private ContactFilter currentFiltering;

    private ContactGroup selectedContactGroup;

    public ContactsPresenter(@NonNull LoaderProvider loaderProvider,
                             @NonNull LoaderManager loaderManager,
                             @NonNull AppRepository repository,
                             @NonNull ContactGroupContract.View view,
                             @NonNull ContactFilter filter,
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
        loadContacts();
    }

    @Override
    public void start() {
        loadContactGroups(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case CONTACT_LOADER:
                return loaderProvider.createFilteredContactsLoader(selectedContactGroup.getId());
            case CONTACT_GROUP_LOADER:
            default:
                return loaderProvider.createContactGroupsLoader();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case CONTACT_LOADER:
                if (data != null) {
                    if (data.moveToLast()) {
                        onContactDataLoaded(data);
                    } else {
                        onContactDataEmpty();
                    }
                } else {
                    onContactDataNotAvailable();
                }
                break;
            case CONTACT_GROUP_LOADER:
            default:
                if (data != null) {
                    if (data.moveToLast()) {
                        onContactGroupDataLoaded(data);
                    } else {
                        onContactGroupDataEmpty();
                    }
                } else {
                    onContactGroupDataNotAvailable();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case CONTACT_LOADER:
                onContactDataReset();
                break;
            case CONTACT_GROUP_LOADER:
            default:
                onContactGroupDataReset();
                break;
        }
    }

    @Override
    public void onContactGroupsLoaded(List<ContactGroup> contactGroups) {
        // we don't care about the result since the CursorLoader will load the data for us
        if (loaderManager.getLoader(CONTACT_GROUP_LOADER) == null) {
            loaderManager.initLoader(CONTACT_GROUP_LOADER, null, this);
        } else {
            loaderManager.restartLoader(CONTACT_GROUP_LOADER, null, this);
        }
    }

    @Override
    public void onContactsLoaded(List<Contact> contacts) {
        // we don't care about the result since the CursorLoader will load the data for us
        if (loaderManager.getLoader(CONTACT_LOADER) == null) {
            loaderManager.initLoader(CONTACT_LOADER, null, this);
        } else {
            loaderManager.restartLoader(CONTACT_LOADER, null, this);
        }
    }

    @Override
    public void loadContactGroups(boolean forceUpdate) {
        view.setLoadingIndicator(true);
        appRepository.getContactGroups(this);
    }

    @Override
    public void loadContacts() {
        view.setLoadingIndicator(true);
        appRepository.getContacts(this);
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
        view.showAddNewContactView(selectedContactGroup.getId());
    }

    @Override
    public void saveContact(@NonNull Contact contact) {
        String result = appRepository.saveContact(contact);
        if(result == DatabaseContract.ALREADY_EXISTS) {
            view.showLoadingError();
        } else {
            view.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void openContactDetails(@NonNull Contact contact) {
        checkNotNull(contact, "contact cannot be null!");
        view.showContactDetailView(contact);
    }

    @Override
    public void openPrayerRequestDetails(@NonNull PrayerRequest prayerRequest) {
        checkNotNull(prayerRequest, "prayerRequest cannot be null!");
        view.showPrayerRequestDetailView(prayerRequest.getId());
    }

    private void processEmptyTasks() {
        switch (currentFiltering.getFilterType()) {
            default:
                view.showNoContactGroups();
                break;
        }
    }

    @Override
    public void onContactGroupDataLoaded(Cursor data) {
        view.setLoadingIndicator(false);
        // Show the list of tasks
        view.showContactGroupsTabs(data, selectedContactGroup);
    }

    @Override
    public void onContactGroupDataEmpty() {
        view.setLoadingIndicator(false);
        // Show a message indicating there are no tasks for that filter type.
        processEmptyTasks();
    }

    @Override
    public void onContactGroupDataReset() {
        view.showContactGroupsTabs(null, selectedContactGroup);
    }

    @Override
    public void onContactGroupDataNotAvailable() {
        view.setLoadingIndicator(false);
        view.showLoadingError();
    }

    @Override
    public void onContactDataLoaded(Cursor data) {
        view.setLoadingIndicator(false);
        // Show the list of contacts
        view.showContacts(data);
    }

    @Override
    public void onContactDataEmpty() {
        view.setLoadingIndicator(false);
        // Show a message indicating there are no tasks for that filter type.
        view.showContacts(null);
    }

    @Override
    public void onContactDataNotAvailable() {
        view.setLoadingIndicator(false);
        view.showLoadingError();
    }

    @Override
    public void onContactDataReset() {
        view.showContacts(null);
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
