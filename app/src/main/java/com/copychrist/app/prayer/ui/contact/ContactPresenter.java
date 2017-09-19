package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.model.PresenterState;

import java.util.List;

/**
 * Created by jim on 8/19/17.
 */

public class ContactPresenter implements ContactContract.Presenter {

    private final ContactService dataService;
    private ContactContract.View contactView;
    private Contact selectedContact;
    private List<PrayerRequest> prayerRequests;

    public ContactPresenter(final ContactService dataService, final Contact contact) {
        this.dataService = dataService;
        this.selectedContact = contact;
    }

    @Override
    public void setView(ContactContract.View view) {
        contactView = view;
        dataService.setPresenter(this);
        contactView.showContactDetail(selectedContact);
        onPrayerRequestGetActiveClick();
    }

    @Override
    public void resetView(ContactContract.View view) {
        contactView = view;
        dataService.setPresenter(this);
        selectedContact = PresenterState.ContactState.selectedContact;
        prayerRequests = PresenterState.ContactState.prayerRequests;

        contactView.showContactDetail(selectedContact);
        contactView.showPrayerRequests(prayerRequests);
    }

    @Override
    public void saveState() {
        PresenterState.ContactState.selectedContact = selectedContact;
        PresenterState.ContactState.prayerRequests = prayerRequests;
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
        if(contactView != null) {
            contactView.finish();
        }
    }

    @Override
    public void onPrayerRequestGetActiveClick() {
        dataService.getContact(selectedContact, true);
    }

    @Override
    public void onPrayerRequestGetArchivedClick() {
        dataService.getContact(selectedContact, false);
    }

    @Override
    public void onPrayerRequestResults(List<PrayerRequest> prayerRequests) {
        if(prayerRequests != null && contactView != null) {
            this.prayerRequests = prayerRequests;
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
