package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;

import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 8/14/17.
 *
 */

public class ContactGroupPresenter implements ContactGroupContract.Presenter {
    private ContactGroup selectedContactGroup;

    private ContactGroupContract.View contactGroupView;
    private ContactGroupService contactGroupService;

    public ContactGroupPresenter(ContactGroupService contactGroupService, ContactGroup contactGroup) {
        this.selectedContactGroup = contactGroup;
        this.contactGroupService = contactGroupService;
    }

    @Override
    public void setView(final ContactGroupContract.View view) {
        contactGroupView = view;
        contactGroupService.getContactGroupValues(this);
    }

    @Override
    public void onContactGroupSaveClick(ContactGroup contactGroup) {
        contactGroupService.saveContactGroupValue(contactGroup);
    }

    @Override
    public void onContactGroupDeleteConfirmed() {
        contactGroupService.deleteContactGroup();
    }

    @Override
    public void onContactDeleteConfirmed(Contact contact) {
        contactGroupService.deleteContact(contact.getKey());
    }

    @Override
    public void onContactGroupClicked(ContactGroup contactGroup) {
        Timber.d("onContactGroupClicked() called with: contactGroup = [" + contactGroup.toString() + "]");
        selectedContactGroup = contactGroup;
        contactGroupService.getContactValues(contactGroup);
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
        contactGroupService.saveContactValue(contact);
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
    public void onContactGroupResults(List<ContactGroup> results, ContactGroup selectedContactGroup) {
        this.selectedContactGroup = selectedContactGroup;
        contactGroupView.showContactGroupsTabs(results, selectedContactGroup);
    }

    @Override
    public void onContactResults(List<Contact> results) {
        contactGroupView.showContacts(results);
    }

    @Override
    public void clearView() {
        contactGroupView = null;
        contactGroupService.destroy();
    }
}
