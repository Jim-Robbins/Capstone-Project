package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;

import java.util.List;

import timber.log.Timber;

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
        contactGroupService.getValues(this);
    }

    @Override
    public void onSaveContactGroupClick(ContactGroup contactGroup) {
        contactGroupService.saveValue(contactGroup);
    }

    @Override
    public void onDeleteContactGroupConfirmed() {
        contactGroupService.deleteValue();
    }

    @Override
    public void onContactGroupClicked(ContactGroup contactGroup) {
        Timber.d("onContactGroupClicked() called with: contactGroup = [" + contactGroup.toString() + "]");
        selectedContactGroup = contactGroup;
        contactGroupService.getContactValues(contactGroup);
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
        contactGroupService.saveContactValue(contact);
    }

    @Override
    public void onDataResultMessage(String message) {
        contactGroupView.showDatabaseResultMessage(message);
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
