package com.copychrist.app.prayer.ui.contact;

import android.database.Cursor;

import com.copychrist.app.prayer.data.model.Contact;

/**
 * Created by jim on 8/19/17.
 */

public interface ContactView {

    void showPrayerRequests(Cursor requests);
    void showPrayerRequestDetailView(long id);
    void showAddNewPrayerRequestView(long contactId);
    void showContactDetail(Contact contact);
    void showContactDetailEditView(long contactId);
    void showDBResultMessage(String message);
    void showDeleteContactDialog(Contact myContact);
    void finish();

    class EmptyMyListView implements ContactView {
        @Override
        public void showPrayerRequests(Cursor requests) {

        }

        @Override
        public void showPrayerRequestDetailView(long id) {

        }

        @Override
        public void showAddNewPrayerRequestView(long contactId) {

        }

        @Override
        public void showContactDetail(Contact contact) {

        }

        @Override
        public void showContactDetailEditView(long contactId) {

        }

        @Override
        public void showDBResultMessage(String message) {

        }

        @Override
        public void showDeleteContactDialog(Contact myContact) {

        }

        @Override
        public void finish() {

        }
    }
}
