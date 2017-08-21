package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.repository.RealmService;

/**
 * Created by jim on 8/19/17.
 */

public class ContactPresenterImpl implements ContactPresenter {

    private final RealmService realmService;
    private final int contactId;
    private ContactView myListView = new ContactView.EmptyMyListView();
    private Contact myContact;

    private boolean requestsShown = false;

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
        }
    }

    private void showArchievedPrayerRequests() {
        if(!requestsShown) {
            myListView.showPrayerRequests(realmService.getArchivedRequestsByContact(myContact));
            requestsShown = true;
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
}
