package com.copychrist.app.prayer.ui.contactgroups;

import android.database.Cursor;

import com.copychrist.app.prayer.data.model.ContactGroup;

import java.util.List;

/**
 * Created by jim on 8/14/17.
 */

public interface ContactsView {
    void showContactGroupsTabs(List<ContactGroup>  contactGroups, ContactGroup contactGroup);
    void showContacts(Cursor contacts);
    void showAddContactGroupDialog();
    void showEditContactGroupDialog(ContactGroup contactGroup);
    void showDeleteContactGroupDialog(ContactGroup contactGroup);
    void showAddNewContactView(String contactGroupName);
    void showContactDetailView(long contactId);
    void showPrayerRequestDetailView(long contactId);
    void showDBResultMessage(String message);

    class EmptyMyListView implements ContactsView {
        @Override
        public void showContactGroupsTabs(List<ContactGroup> contactGroups, ContactGroup contactGroup) {

        }

        @Override
        public void showContacts(Cursor contacts) {

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
        public void showContactDetailView(long contactId) {

        }

        @Override
        public void showPrayerRequestDetailView(long contactId) {

        }

        @Override
        public void showDBResultMessage(String message) {

        }
    }
}
