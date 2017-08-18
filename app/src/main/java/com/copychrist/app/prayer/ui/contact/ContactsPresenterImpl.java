package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.repository.RealmService;

/**
 * Created by jim on 8/14/17.
 */

public class ContactsPresenterImpl implements ContactsPresenter, RealmService.OnTransactionCallback {
    private final RealmService realmService;
    private ContactsView myListView = new ContactsView.EmptyMyListView();

    private boolean contactsShown = false;

    public ContactsPresenterImpl(final RealmService realmService) {
        this.realmService = realmService;
    }

    @Override
    public void setView(final ContactsView view) {
        myListView = view;
        showContacts();
    }

    private void showContacts() {
        if(!contactsShown) {
//            TODO: create realm query to get contacts for group
//            myListView.showContacts(realmService.getContactsForGroup);
            contactsShown = true;
        }
    }

    @Override
    public void clearView() {
        myListView = new ContactsView.EmptyMyListView();
    }

    @Override
    public void closeRealm() {
        realmService.closeRealm();
    }

    @Override
    public void onContactClick(int id) {
        myListView.showContactDetailView(id);
    }

    @Override
    public void onAddNewContactClick() {
        myListView.showAddNewContactView();
    }

    @Override
    public void onAddClick(String firstName, String lastName, String groupName, String pictureUrl) {
        realmService.addContact(firstName, lastName, pictureUrl, groupName, this);
    }

    @Override
    public void onRealmSuccess() {

    }

    @Override
    public void onRealmError(Throwable e) {

    }
}
