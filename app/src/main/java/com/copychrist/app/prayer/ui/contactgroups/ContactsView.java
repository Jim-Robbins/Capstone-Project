package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by jim on 8/14/17.
 */

public interface ContactsView {
    void showContactGroupsTabs(RealmResults<ContactGroup> contactGroups, ContactGroup contactGroup);
    void showContacts(RealmList<Contact> contacts);
    void showAddContactGroupDialog();
    void showEditContactGroupDialog(ContactGroup contactGroup);
    void showDeleteContactGroupDialog(ContactGroup contactGroup);
    void showAddNewContactView(String contactGroupName);
    void showContactDetailView(int contactId);
    void showPrayerRequestDetailView(int contactId);
    void showRealmResultMessage(String message);

    class EmptyMyListView implements ContactsView {
        @Override
        public void showContactGroupsTabs(RealmResults<ContactGroup> contactGroups, ContactGroup contactGroup) {

        }

        @Override
        public void showContacts(RealmList<Contact> contacts) {

        }

        @Override
        public void showAddContactGroupDialog() {

        }

        @Override
        public void showEditContactGroupDialog(ContactGroup contactGroup) {

        }

        @Override
        public void showDeleteContactGroupDialog(ContactGroup contactGroup) {

        }

        @Override
        public void showAddNewContactView(String contactGroupName) {

        }

        @Override
        public void showContactDetailView(int contactId) {

        }

        @Override
        public void showPrayerRequestDetailView(int contactId) {

        }

        @Override
        public void showRealmResultMessage(String message) {

        }
    }
}
