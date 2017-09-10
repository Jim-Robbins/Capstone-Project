package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;

import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 8/19/17.
 */

public class ContactPresenter implements ContactContract.Presenter {

    private final ContactService dataService;
    private final String contactKey;
    private ContactContract.View contactView;
    private Contact selectedContact;

    public ContactPresenter(final ContactService dataService, final String contactKey) {
        this.dataService = dataService;
        this.contactKey = contactKey;
    }

    @Override
    public void setView(ContactContract.View view) {
        contactView = view;
        onPrayerRequestGetActiveClick();
    }

    @Override
    public void onContactResults(Contact contact) {
        selectedContact = contact;
        contactView.showContactDetail(selectedContact);
    }

    @Override
    public void clearView() {
        contactView = null;
        dataService.destroy();
    }

    @Override
    public void onContactSaveClick(Contact contact) {
        dataService.saveContact(contact);
    }

    @Override
    public void onContactDeleteConfirm() {
        dataService.deleteContact();
    }

    @Override
    public void onContactDeleteCompleted() {
        contactView.finish();
    }

    @Override
    public void onPrayerRequestGetActiveClick() {
        dataService.getContact(this, contactKey, true);
    }

    @Override
    public void onPrayerRequestGetArchivedClick() {
        dataService.getContact(this, contactKey, false);
    }

    @Override
    public void onPrayerRequestResults(List<PrayerRequest> prayerRequests) {
        if(prayerRequests == null) return;
        Timber.d("onPrayerRequestResults() called with: prayerRequests = [" + prayerRequests + "]");
        contactView.showPrayerRequests(prayerRequests);
    }

    @Override
    public void onDataResultMessage(String message) {
        contactView.showDatabaseResultMessage(message);
    }

    @Override
    public void onDataResultMessage(int messageResId) {
        contactView.showDatabaseResultMessage(messageResId);
    }
}
