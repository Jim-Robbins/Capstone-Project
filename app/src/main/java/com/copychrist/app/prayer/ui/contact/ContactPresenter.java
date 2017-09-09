package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.model.Contact;

/**
 * Created by jim on 8/19/17.
 */

public class ContactPresenter implements ContactContract.Presenter {

    private final ContactService dataService;
    private final String contactKey;
    private ContactContract.View contactView;
    private Contact selectedContact;

    private boolean requestsShown = false;
    private boolean archivesShown = false;

    public ContactPresenter(final ContactService dataService, final String contactKey) {
        this.dataService = dataService;
        this.contactKey = contactKey;
    }

    @Override
    public void setView(ContactContract.View view) {
        contactView = view;
        dataService.getValue(this, contactKey);
    }

    @Override
    public void onGetContactResult(Contact contact) {
        selectedContact = contact;
        contactView.showContactDetail(selectedContact);
        //showActivePrayerRequests();
    }

    @Override
    public void clearView() {
        contactView = null;
        dataService.destroy();
    }

    @Override
    public void onSaveContactClick(Contact contact) {
        dataService.saveValue(contact);
    }

    @Override
    public void onDeleteConfirm() {
        dataService.deleteValue();
    }

    @Override
    public void onDeleteCompleted() {
        contactView.finish();
    }

    @Override
    public void onActiveRequestsClick() {
        if(!requestsShown) {
            //contactView.showPrayerRequests(dataService.getActiveRequestsByContact(selectedContact));
            requestsShown = true;
            archivesShown = false;
        }
    }

    @Override
    public void onArchiveClick() {
        if(!archivesShown) {
            // contactView.showPrayerRequests(dataService.getArchivedRequestsByContact(selectedContact));
            archivesShown = true;
            requestsShown = false;
        }
    }

    @Override
    public void onDataResultMessage(String message) {
        contactView.showDatabaseResultMessage(message);
    }
}
