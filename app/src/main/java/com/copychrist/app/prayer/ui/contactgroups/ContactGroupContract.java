package com.copychrist.app.prayer.ui.contactgroups;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.copychrist.app.prayer.data.model.Contact;
import com.copychrist.app.prayer.data.model.ContactGroup;
import com.copychrist.app.prayer.data.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BasePresenter;
import com.copychrist.app.prayer.ui.BaseView;

import java.util.List;

/**
 * Created by jim on 9/3/17.
 */

public class ContactGroupContract {

    public interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);
        void showContactGroupsTabs (Cursor contactGroups, ContactGroup selectedGroup);
        void showContacts(Cursor contacts);
        void showAddContactGroupDialog();
        void showEditContactGroupDialog(ContactGroup contactGroup);
        void showDeleteContactGroupDialog(ContactGroup contactGroup);
        void showAddNewContactView(String contactGroupName);
        void showContactDetailView(long contactId);
        void showPrayerRequestDetailView(long contactId);
        void showLoadingError();
        void showNoContactGroups();
        void showSuccessfullySavedMessage();
    }

    public interface Presenter extends BasePresenter {
        void loadContactGroups(boolean forceUpdate);
        void setContactGroup(@NonNull ContactGroup contactGroup);
        void addNewContactGroup();
        void openContactGroupDetails();
        void saveContactGroup(@NonNull ContactGroup contactGroup);
        void deleteContactGroup(@NonNull ContactGroup contactGroup);
        void deleteContactGroupConfirm();

        void addNewContact();
        void saveContact(@NonNull Contact contact);
        void openContactDetails(@NonNull Contact contact);

        void openPrayerRequestDetails(@NonNull PrayerRequest prayerRequest);

//        void takeView(ContactGroupContract.View view);
//        void dropView();
    }

}
