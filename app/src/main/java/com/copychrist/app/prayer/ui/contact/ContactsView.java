package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.model.Contact;

import io.realm.RealmResults;

/**
 * Created by jim on 8/14/17.
 */

public interface ContactsView {

    void showContacts(RealmResults<Contact> contacts);
    void showContactDetailView(int id);
    void showAddNewContactView();

    class EmptyMyListView implements ContactsView {
        @Override
        public void showContacts(RealmResults<Contact> contacts) {

        }

        @Override
        public void showContactDetailView(int id) {

        }

        @Override
        public void showAddNewContactView() {

        }
    }
}
