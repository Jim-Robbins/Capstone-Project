package com.copychrist.app.prayer.ui.contact;

import android.content.Context;

import com.copychrist.app.prayer.data.AppDataSource;
import com.copychrist.app.prayer.data.model.Contact;

/**
 * Created by jim on 8/19/17.
 */

public class ContactPresenterImpl implements ContactPresenter {
    //RealmService.OnTransactionCallback {

//    private final AppDataSource appDataSource;
    private final long contactId;
    private ContactView myListView = new ContactView.EmptyMyListView();
    private Contact myContact;

    private boolean requestsShown = false;
    private boolean archivesShown = false;

    public ContactPresenterImpl(final long contactId) {
//        this.appDataSource = appDataSource;
        this.contactId = contactId;
    }

    @Override
    public void start() {

    }

    //    @Override
//    public void setView(ContactView view) {
//        myListView = view;
////        appDataSource.getContact(contactId, this);
//        myListView.showContactDetail(myContact);
//        showActivePrayerRequests();
//    }
//
//    @Override
//    public void onContactLoaded(Contact contact) {
//
//    }
//
//    @Override
//    public void onContactDataNotAvailable() {
//
//    }

    private void showActivePrayerRequests() {
        if(!requestsShown) {
//            myListView.showPrayerRequests(appDataSource.getActiveRequestsByContact(myContact));
            requestsShown = true;
            archivesShown = false;
        }
    }

    private void showArchivedPrayerRequests() {
        if(!archivesShown) {
//            myListView.showPrayerRequests(appDataSource.getArchivedRequestsByContact(myContact));
            archivesShown = true;
            requestsShown = false;
        }
    }

//    @Override
//    public void clearView() {
//        myListView = new ContactView.EmptyMyListView();
//    }

    @Override
    public void onPrayerRequestClick(long requestId) {
        myListView.showPrayerRequestDetailView(requestId);
    }

    @Override
    public void onAddNewRequestClick() {
        myListView.showAddNewPrayerRequestView(contactId);
    }

    @Override
    public void onContactEditClick(long contactId) {
        myListView.showContactDetailEditView(contactId);
    }

    @Override
    public void onEditClick(long contactId, String firstName, String lastName, String pictureUrl) {
//        appDataSource.editContact(contactId, firstName, lastName, pictureUrl, myContact.getGroup().getName(), this);
    }

    @Override
    public void onContactDeleteClick(long id) {
        myListView.showDeleteContactDialog(myContact);
    }

    @Override
    public void onDeleteConfirm(long id) {
//        appDataSource.deleteContact(id);
        myListView.finish();
    }

    @Override
    public void onActiveRequestsClick() {
        showActivePrayerRequests();
    }

    @Override
    public void onArchiveClick() {
        showArchivedPrayerRequests();
    }

//    @Override
//    public void onDataSuccess() {
//        myListView.showRealmResultMessage("ContactEntry Added");
//    }
//
//    @Override
//    public void onDataError(Throwable e) {
//        myListView.showRealmResultMessage("Failed to add contact");
//    }
}
