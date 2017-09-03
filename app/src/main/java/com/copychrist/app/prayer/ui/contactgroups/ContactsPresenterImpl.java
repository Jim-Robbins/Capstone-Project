package com.copychrist.app.prayer.ui.contactgroups;

import android.support.annotation.NonNull;

import com.copychrist.app.prayer.data.AppDataSource;
import com.copychrist.app.prayer.data.model.ContactGroup;

/**
 * Created by jim on 8/14/17.
 */

public class ContactsPresenterImpl implements ContactsPresenter {

//    private final AppDataSource dataSource;
    private final long contactGroupId;
    private ContactGroup myContactGroup;

    private ContactsView myListView = new ContactsView.EmptyMyListView();

    private boolean contactsShown = false;
    private boolean contactGroupsShown = false;

    public ContactsPresenterImpl(long contactGroupId) {
        this.contactGroupId = contactGroupId;
//        this.dataSource = dataSource;
    }

    @Override
    public void setView(final ContactsView view) {
        myListView = view;
//        myContactGroup = dataSource.getContactGroup(contactGroupId);
        showContacts();
        showContactGroupsTabs();
    }

    private void showContacts() {
        if(!contactsShown) {
//            myListView.showContacts(myContactGroup.getContacts());
            contactsShown = true;
        }
    }

    private void showContactGroupsTabs() {
        if(!contactGroupsShown) {
//            myListView.showContactGroupsTabs(dataSource.getAllContactGroups(), myContactGroup);
            contactGroupsShown = true;
        }
    }

    @Override
    public void onContactGroupClicked(long contactGroupId) {
        if(contactGroupId != myContactGroup.getId()) {
            contactsShown = false;
  //          myContactGroup = dataSource.getContactGroup(contactGroupId);
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
    public void onSaveContactGroupClick(long contactGroupId, String groupName, String groupDesc, int sortOrder) {
        if(contactGroupId > 0) {
            //dataSource.editContactGroup(contactGroupId, groupName, groupDesc, sortOrder);
        } else {
            //dataSource.addContactGroup(groupName, groupDesc);
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
        //dataSource.deleteContactGroup(myContactGroup.getId());
        //myContactGroup = dataSource.getAllContactGroups().first();
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
        //dataSource.addContact(firstName, lastName, pictureUrl, groupName, this);
    }

    @Override
    public void onContactClick(long id) {
        myListView.showContactDetailView(id);
    }

    @Override
    public void onPrayerRequestClick(long requestId) {
        myListView.showPrayerRequestDetailView(requestId);
    }
//
//    @Override
//    public void onDBSuccess() {
//        myListView.showRealmResultMessage("ContactContract Added");
//    }

//    @Override
//    public void onDBError(Throwable e) {
//        myListView.showRealmResultMessage("Failed to add contact");
//    }

    @Override
    public void clearView() {
        myListView = new ContactsView.EmptyMyListView();
    }
}
