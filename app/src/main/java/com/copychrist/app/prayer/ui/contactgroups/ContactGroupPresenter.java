package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;
import com.copychrist.app.prayer.model.PresenterState;

import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 8/14/17.
 *
 */

public class ContactGroupPresenter implements ContactGroupContract.Presenter {
    private ContactGroup selectedContactGroup;

    private ContactGroupContract.View contactGroupView;
    private ContactGroupService dataService;
    private List<ContactGroup> contactGroups;
    private List<Contact> contacts;

    public ContactGroupPresenter(ContactGroupService dataService, ContactGroup contactGroup) {
        this.selectedContactGroup = contactGroup;
        this.dataService = dataService;
    }

    @Override
    public void setView(final ContactGroupContract.View view) {
        contactGroupView = view;
        dataService.setPresenter(this);
        dataService.getContactGroupValues();
    }

    @Override
    public void resetView(ContactGroupContract.View view) {
        contactGroupView = view;
        dataService.setPresenter(this);

        this.contacts = PresenterState.ContactGroupState.contacts;
        this.contactGroups = PresenterState.ContactGroupState.contactGroups;
        this.selectedContactGroup = PresenterState.ContactGroupState.selectedContactGroup;

        contactGroupView.showContactGroupsTabs(contactGroups, selectedContactGroup);
        contactGroupView.showContacts(contacts);
    }

    @Override
    public void saveState() {
        PresenterState.ContactGroupState.contacts = this.contacts;
        PresenterState.ContactGroupState.contactGroups = this.contactGroups;
        PresenterState.ContactGroupState.selectedContactGroup = this.selectedContactGroup;
    }

    @Override
    public void onContactGroupSaveClick(ContactGroup contactGroup) {
        dataService.saveContactGroupValue(contactGroup);
    }

    @Override
    public void onContactGroupDeleteConfirmed() {
        dataService.deleteContactGroup();
    }

    @Override
    public void onContactDeleteConfirmed(Contact contact) {
        dataService.deleteContact(contact.getKey());
    }

    @Override
    public void onContactGroupClicked(ContactGroup contactGroup) {
        Timber.d("onContactGroupClicked() called with: contactGroup = [" + contactGroup.toString() + "]");
        selectedContactGroup = contactGroup;
        dataService.getContactValues(contactGroup);
    }

    @Override
    public void onContactGroupAddClick() {
        contactGroupView.showContactGroupDialogAdd();
    }

    @Override
    public void onContactGroupEditClick() {
        contactGroupView.showContactGroupDialogEdit(selectedContactGroup);
    }

    @Override
    public void onContactGroupDeleteClick() {
        contactGroupView.showContactGroupDialogDelete(selectedContactGroup);
    }

    @Override
    public void onContactAddClick() {
        contactGroupView.showContactAddDialog(selectedContactGroup);
    }

    @Override
    public void onContactSaveClick(Contact contact) {
        dataService.saveContactValue(contact);
    }

    @Override
    public void onDataResultMessage(String message) {
        contactGroupView.showDatabaseResultMessage(message);
    }

    @Override
    public void onDataResultMessage(int messageResId) {
        contactGroupView.showDatabaseResultMessage(messageResId);
    }

    @Override
    public void onContactGroupResults(List<ContactGroup> contactGroups, ContactGroup selectedContactGroup) {
        this.selectedContactGroup = selectedContactGroup;
        this.contactGroups = contactGroups;
        contactGroupView.showContactGroupsTabs(contactGroups, selectedContactGroup);
    }

    @Override
    public void onContactResults(List<Contact> contacts) {
        this.contacts = contacts;
        if(contacts != null && contactGroupView != null) {
            contactGroupView.showContacts(contacts);
        }
    }

    @Override
    public void clearView() {
        contactGroupView = null;
        dataService.destroy();
    }
}
