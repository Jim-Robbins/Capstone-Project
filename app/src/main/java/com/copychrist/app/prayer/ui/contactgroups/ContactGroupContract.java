package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;
import com.copychrist.app.prayer.ui.BasePresenter;

import java.util.List;


/**
 * Created by jim on 8/14/17.
 */

public class ContactGroupContract {
    public interface View {
        void showContactGroupsTabs(List<ContactGroup> contactGroups, final ContactGroup selectedGroup);
        void showContacts(List<Contact> contacts);
        void showAddContactGroupDialog();
        void showEditContactGroupDialog(ContactGroup contactGroup);
        void showDeleteContactGroupDialog(ContactGroup contactGroup);
        void showAddNewContactView(String contactGroupName);
        void showContactDetailView(String contactId);
        void showPrayerRequestDetailView(String contactId);
        void showDatabaseResultMessage(String message);
    }

    public interface Presenter extends BasePresenter<ContactGroupContract.View> {
        void onContactGroupClicked(ContactGroup contactGroup);
        void onAddNewContactGroupClick();
        void onEditContactGroupClick();
        void onSaveContactGroupClick(ContactGroup contactGroup);
        void onDeleteContactGroupClick();
        void onDeleteContactGroupConfirmed();
        void onAddNewContactClick();
        void onSaveContactClick(Contact contact);
        void onContactClick(String contactId);
        void onPrayerRequestClick(String prayerRequestId);
    }

}
