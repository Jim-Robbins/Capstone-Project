package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.model.Contact;

/**
 * Created by jim on 8/19/17.
 */

public class ContactPresenter implements ContactContract.Presenter {

    private final ContactService dataService;
    private final String contactId;
    private ContactContract.View myListView;
    private Contact myContact;

    private boolean requestsShown = false;
    private boolean archivesShown = false;

    public ContactPresenter(final ContactService dataService, final String contactId) {
        this.dataService = dataService;
        this.contactId = contactId;
    }

    @Override
    public void setView(ContactContract.View view) {
        myListView = view;
        //myContact = dataService.getContact(contactId);
        myListView.showContactDetail(myContact);
        showActivePrayerRequests();
    }

    private void showActivePrayerRequests() {
        if(!requestsShown) {
            //myListView.showPrayerRequests(dataService.getActiveRequestsByContact(myContact));
            requestsShown = true;
            archivesShown = false;
        }
    }

    private void showArchivedPrayerRequests() {
        if(!archivesShown) {
           // myListView.showPrayerRequests(dataService.getArchivedRequestsByContact(myContact));
            archivesShown = true;
            requestsShown = false;
        }
    }

    @Override
    public void clearView() {
        myListView = null;
    }

    @Override
    public void onPrayerRequestClick(String requestId) {
        myListView.showPrayerRequestDetailView(requestId);
    }

    @Override
    public void onAddNewRequestClick() {
        myListView.showAddNewPrayerRequestView(contactId);
    }

    @Override
    public void onContactEditClick(String contactId) {
        myListView.showContactDetailEditView(contactId);
    }

    @Override
    public void onSaveClick(Contact contact) {
        //dataService.editContact(contactId, firstName, lastName, pictureUrl, myContact.getGroupKey().getName(), this);
    }

    @Override
    public void onContactDeleteClick(String id) {
        myListView.showDeleteContactDialog(myContact);
    }

    @Override
    public void onDeleteConfirm(String id) {
        //dataService.deleteContact(id);
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
//    public void onRealmSuccess() {
//        myListView.showRealmResultMessage("Contact Added");
//    }
//
//    @Override
//    public void onRealmError(Throwable e) {
//        myListView.showRealmResultMessage("Failed to add contact");
//    }
}
