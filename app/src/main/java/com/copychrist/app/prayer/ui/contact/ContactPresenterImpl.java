package com.copychrist.app.prayer.ui.contact;

import android.support.v7.widget.RecyclerView;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.repository.RealmService;

/**
 * Created by jim on 8/19/17.
 */

public class ContactPresenterImpl implements ContactPresenter, RealmService.OnTransactionCallback {

    private final RealmService realmService;
    private final int contactId;
    private ContactView myListView = new ContactView.EmptyMyListView();
    private Contact myContact;

    private boolean requestsShown = false;
    private boolean archivesShown = false;

    public ContactPresenterImpl(final RealmService realmService, final int contactId) {
        this.realmService = realmService;
        this.contactId = contactId;
    }

    @Override
    public void setView(ContactView view) {
        myListView = view;
        myContact = realmService.getContact(contactId);
        myListView.showContactDetail(myContact);
        showActivePrayerRequests();
    }

    private void showActivePrayerRequests() {
        if(!requestsShown) {
            myListView.showPrayerRequests(realmService.getActiveRequestsByContact(myContact));
            requestsShown = true;
            archivesShown = false;
        }
    }

    private void showArchivedPrayerRequests() {
        if(!archivesShown) {
            myListView.showPrayerRequests(realmService.getArchivedRequestsByContact(myContact));
            archivesShown = true;
            requestsShown = false;
        }
    }

    @Override
    public void clearView() {
        myListView = new ContactView.EmptyMyListView();
    }

    @Override
    public void closeRealm() {
        realmService.closeRealm();
    }

    @Override
    public void onPrayerRequestClick(int requestId) {
        myListView.showPrayerRequestDetailView(requestId);
    }

    @Override
    public void onAddNewRequestClick() {
        myListView.showAddNewPrayerRequestView(contactId);
    }

    @Override
    public void onContactEditClick(int contactId) {
        myListView.showContactDetailEditView(contactId);
    }

    @Override
    public void onEditClick(int contactId, String firstName, String lastName, String pictureUrl) {
        realmService.editContact(contactId, firstName, lastName, pictureUrl, myContact.getGroup().getName(), this);
    }

    @Override
    public void onActiveRequestsClick() {
        showActivePrayerRequests();
    }

    @Override
    public void onArchiveClick() {
        showArchivedPrayerRequests();
    }

    @Override
    public void onRealmSuccess() {
        myListView.showRealmResultMessage("Contact Added");
    }

    @Override
    public void onRealmError(Throwable e) {
        myListView.showRealmResultMessage("Failed to add contact");
    }
}
