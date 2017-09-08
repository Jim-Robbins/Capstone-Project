package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BasePresenter;

import java.util.List;

/**
 * Created by jim on 9/8/17.
 */

public class ContactContract {
    public interface View {
        void showPrayerRequests(List<PrayerRequest> requests);
        void showPrayerRequestDetailView(String id);
        void showAddNewPrayerRequestView(String contactId);
        void showContactDetail(Contact contact);
        void showContactDetailEditView(String contactId);
        void showRealmResultMessage(String message);
        void showDeleteContactDialog(Contact myContact);
        void finish();
    }

    public interface Presenter extends BasePresenter<ContactContract.View> {
        void onPrayerRequestClick(String requestId);
        void onAddNewRequestClick();
        void onActiveRequestsClick();
        void onArchiveClick();
        void onContactEditClick(String contactId);
        void onSaveClick(Contact contact);
        void onContactDeleteClick(String id);
        void onDeleteConfirm(String id);
    }
}
