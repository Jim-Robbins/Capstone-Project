package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;

/**
 * Created by jim on 8/14/17.
 */

public class ContactGroupPresenter implements ContactGroupContract.Presenter {
    private final String selectedContactGroupKey;
    private ContactGroup selectedContactGroup;

    private ContactGroupContract.View contactGroupView;
    private ContactGroupService contactGroupService;

    public ContactGroupPresenter(ContactGroupService contactGroupService, String contactGroupKey) {
        this.selectedContactGroupKey = contactGroupKey;
        this.contactGroupService = contactGroupService;
    }

    @Override
    public void setView(final ContactGroupContract.View view) {
        contactGroupView = view;
        contactGroupService.getValues(contactGroupView);
    }

    @Override
    public void onSaveContactGroupClick(ContactGroup contactGroup) {
        contactGroupService.saveValue(contactGroup);
    }

    @Override
    public void onDeleteContactGroupConfirmed() {
        contactGroupService.deleteValue(contactGroupView, selectedContactGroup.getKey());
    }

    private void getContactValues() {
        contactGroupService.getContactValues(selectedContactGroup.getKey());
    }

    @Override
    public void onContactGroupClicked(ContactGroup contactGroup) {
        selectedContactGroup = contactGroup;
        contactGroupService.selectedContactGroup = contactGroup;
        getContactValues();
    }

    @Override
    public void onAddNewContactGroupClick() {
        contactGroupView.showAddContactGroupDialog();
    }

    @Override
    public void onEditContactGroupClick() {
        contactGroupView.showEditContactGroupDialog(selectedContactGroup);
    }

    @Override
    public void onDeleteContactGroupClick() {
        contactGroupView.showDeleteContactGroupDialog(selectedContactGroup);
    }

    @Override
    public void onAddNewContactClick() {
        contactGroupView.showAddNewContactView(selectedContactGroup.getName());
    }

    @Override
    public void onSaveContactClick(Contact contact) {
//        databaseService.addContact(firstName, lastName, pictureUrl, groupName, this);
    }

    @Override
    public void onContactClick(String id) {
        contactGroupView.showContactDetailView(id);
    }

    @Override
    public void onPrayerRequestClick(String requestId) {
        contactGroupView.showPrayerRequestDetailView(requestId);
    }


    @Override
    public void clearView() {
        contactGroupView = null;
        contactGroupService.destroy();
    }
}
