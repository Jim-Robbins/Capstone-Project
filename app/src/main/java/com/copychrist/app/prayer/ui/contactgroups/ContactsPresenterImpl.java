package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.model.ContactGroup;
import com.copychrist.app.prayer.repository.DatabaseSerivce;

/**
 * Created by jim on 8/14/17.
 */

public class ContactsPresenterImpl implements ContactsPresenter, DatabaseSerivce.OnTransactionCallback {
    private final DatabaseSerivce realmService;
    private final int contactGroupId;
    private ContactGroup myContactGroup;

    private ContactsView myListView = new ContactsView.EmptyMyListView();

    private boolean contactsShown = false;
    private boolean contactGroupsShown = false;

    public ContactsPresenterImpl(final DatabaseSerivce realmService, int contactGroupId) {
        this.realmService = realmService;
        this.contactGroupId = contactGroupId;
    }

    @Override
    public void setView(final ContactsView view) {
        myListView = view;
        myContactGroup = realmService.getContactGroup(contactGroupId);
        showContacts();
        showContactGroupsTabs();
    }

    private void showContacts() {
        if(!contactsShown) {
            myListView.showContacts(myContactGroup.getContacts());
            contactsShown = true;
        }
    }

    private void showContactGroupsTabs() {
        if(!contactGroupsShown) {
            myListView.showContactGroupsTabs(realmService.getAllContactGroups(), myContactGroup);
            contactGroupsShown = true;
        }
    }

    @Override
    public void onContactGroupClicked(int contactGroupId) {
        if(contactGroupId != myContactGroup.getId()) {
            contactsShown = false;
            myContactGroup = realmService.getContactGroup(contactGroupId);
            showContacts();
        }
    }

    @Override
    public void onAddNewContactGroupClick() {
        myListView.showAddContactGroupDialog();
    }

    @Override
    public void onEditContactGroupClick() {
        myListView.showEditContactGroupDialog(myContactGroup);
    }

    @Override
    public void onSaveContactGroupClick(int contactGroupId, String groupName, String groupDesc, int sortOrder) {
        if(contactGroupId > 0) {
            realmService.editContactGroup(contactGroupId, groupName, groupDesc, sortOrder);
        } else {
            realmService.addContactGroup(groupName, groupDesc);
        }
        contactGroupsShown = false;
        showContactGroupsTabs();
    }

    @Override
    public void onDeleteContactGroupClick() {
        myListView.showDeleteContactGroupDialog(myContactGroup);
    }

    @Override
    public void onDeleteContactGroupConfirmed() {
        realmService.deleteContactGroup(myContactGroup.getId());
        myContactGroup = realmService.getAllContactGroups().first();
        contactGroupsShown = false;
        contactsShown = false;
        showContactGroupsTabs();
        showContacts();
    }

    @Override
    public void onAddNewContactClick() {
        myListView.showAddNewContactView(myContactGroup.getName());
    }

    @Override
    public void onSaveContactClick(String firstName, String lastName, String groupName, String pictureUrl) {
        realmService.addContact(firstName, lastName, pictureUrl, groupName, this);
    }

    @Override
    public void onContactClick(int id) {
        myListView.showContactDetailView(id);
    }

    @Override
    public void onPrayerRequestClick(int requestId) {
        myListView.showPrayerRequestDetailView(requestId);
    }

    @Override
    public void onRealmSuccess() {
        myListView.showRealmResultMessage("Contact Added");
    }

    @Override
    public void onRealmError(Throwable e) {
        myListView.showRealmResultMessage("Failed to add contact");
    }

    @Override
    public void clearView() {
        myListView = new ContactsView.EmptyMyListView();
    }

    @Override
    public void closeRealm() {
        realmService.closeRealm();
    }
}
