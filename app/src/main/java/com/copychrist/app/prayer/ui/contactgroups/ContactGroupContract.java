package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BasePresenter;

import java.util.List;


/**
 * Created by jim on 8/14/17.
 */

public class ContactGroupContract {
    public interface View {
        void showContactGroupDialogAdd();
        void showContactGroupDialogDelete(ContactGroup contactGroup);
        void showContactGroupDialogEdit(ContactGroup contactGroup);
        void showContactGroupsTabs(List<ContactGroup> contactGroups, final ContactGroup selectedGroup);
        void showContactAddDialog(ContactGroup contactGroup);
        void showContacts(List<Contact> contacts, List<PrayerRequest> prayerRequests);
        void showDatabaseResultMessage(String message);
        void showDatabaseResultMessage(int messageResId);
    }

    public interface Presenter extends BasePresenter<ContactGroupContract.View> {
        void onContactGroupAddClick();
        void onContactGroupClicked(ContactGroup contactGroup);
        void onContactGroupDeleteClick();
        void onContactGroupDeleteConfirmed();
        void onContactGroupEditClick();
        void onContactGroupResults(List<ContactGroup> results, ContactGroup selectedContactGroup);
        void onContactGroupSaveClick(ContactGroup contactGroup);
        void onContactAddClick();
        void onContactDeleteConfirmed(Contact contact);
        void onContactResults(List<Contact> results);
        void onContactSaveClick(Contact contact);
        void onDataResultMessage(String message);
        void onDataResultMessage(int messageResId);

        void onPrayerRequestResults(List<PrayerRequest> prayerRequests);
    }

}
