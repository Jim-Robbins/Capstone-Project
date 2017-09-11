package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;

import java.util.List;

/**
 * Created by jim on 8/19/17.
 */

public class ContactPresenter implements ContactContract.Presenter {

    private final ContactService dataService;
    private ContactContract.View contactView;
    private Contact selectedContact;

    public ContactPresenter(final ContactService dataService, final Contact contact) {
        this.dataService = dataService;
        this.selectedContact = contact;
    }

    @Override
    public void setView(ContactContract.View view) {
        contactView = view;
        contactView.showContactDetail(selectedContact);
        onPrayerRequestGetActiveClick();
    }

    @Override
    public void clearView() {
        contactView = null;
        dataService.destroy();
    }

    @Override
    public void onContactUpdated(Contact contact) {
        if(contactView != null)
            contactView.showContactDetail(contact);
    }

    @Override
    public void onContactSaveClick(Contact contact) {
        dataService.saveContact(contact);
    }

    @Override
    public void onContactDeleteConfirm() {
        dataService.deleteContact(selectedContact);
    }

    @Override
    public void onContactDeleteCompleted() {
        contactView.finish();
    }

    @Override
    public void onPrayerRequestGetActiveClick() {
        dataService.getContact(this, selectedContact, true);
    }

    @Override
    public void onPrayerRequestGetArchivedClick() {
        dataService.getContact(this, selectedContact, false);
    }

    @Override
    public void onPrayerRequestResults(List<PrayerRequest> prayerRequests) {
        if(prayerRequests != null && contactView != null) {
            contactView.showPrayerRequests(prayerRequests);
        }
    }

    @Override
    public void onDataResultMessage(String message) {
        if(contactView != null)
            contactView.showDatabaseResultMessage(message);
    }

    @Override
    public void onDataResultMessage(int messageResId) {
        if(contactView != null)
            contactView.showDatabaseResultMessage(messageResId);
    }
}
