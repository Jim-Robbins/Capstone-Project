package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.model.ContactGroup;

/**
 * Created by jim on 8/14/17.
 */

public class ContactGroupPresenter implements ContactGroupContract.Presenter {
    private final int contactGroupId;
    private ContactGroup myContactGroup;

    private ContactGroupContract.View myListView;
    private ContactGroupService contactGroupService;

    private boolean contactsShown = false;

    public ContactGroupPresenter(ContactGroupService contactGroupService, int contactGroupId) {
        this.contactGroupId = contactGroupId;
        this.contactGroupService = contactGroupService;
    }

    @Override
    public void setView(final ContactGroupContract.View view) {
        myListView = view;
        myContactGroup = getContactGroup(contactGroupId);
        contactGroupService.getValue(myListView, myContactGroup);
        showContacts();
    }

    @Override
    public void onSaveContactGroupClick(ContactGroup contactGroup) {
        contactGroupService.saveValue(contactGroup);
    }

    @Override
    public void onDeleteContactGroupConfirmed() {
        contactGroupService.deleteValue(myContactGroup.getId());
    }

    private ContactGroup getContactGroup(int contactGroupId) {
        return null;
    }

    private void showContacts() {
        if(!contactsShown) {
           // myListView.showContacts(myContactGroup.getContacts());
            contactsShown = true;
        }
    }

    @Override
    public void onContactGroupClicked(ContactGroup contactGroup) {
        if(myContactGroup == null || contactGroup.getName() != myContactGroup.getName()) {
            contactsShown = false;
            myContactGroup = contactGroup;
//            myContactGroup = databaseService.getContactGroup(contactGroupId);
//            showContacts();
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
    public void onDeleteContactGroupClick() {
        myListView.showDeleteContactGroupDialog(myContactGroup);
    }

    @Override
    public void onAddNewContactClick() {
        myListView.showAddNewContactContract.View(myContactGroup.getName());
    }

    @Override
    public void onSaveContactClick(String firstName, String lastName, String groupName, String pictureUrl) {
//        databaseService.addContact(firstName, lastName, pictureUrl, groupName, this);
    }

    @Override
    public void onContactClick(String id) {
        myListView.showContactDetailView(id);
    }

    @Override
    public void onPrayerRequestClick(String requestId) {
        myListView.showPrayerRequestDetailView(requestId);
    }


    @Override
    public void clearView() {
        myListView = null;
        contactGroupService.destroy();
    }
}
