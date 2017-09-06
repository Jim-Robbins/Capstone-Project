package com.copychrist.app.prayer.ui.contact;

import android.database.Cursor;

import com.copychrist.app.prayer.data.model.Contact;
import com.copychrist.app.prayer.ui.BasePresenter;
import com.copychrist.app.prayer.ui.BaseView;

/**
 * Created by jim on 9/4/17.
 */

public class ContactContract {
    public interface View extends BaseView<Presenter> {
        void showPrayerRequests(Cursor requests);
        void showPrayerRequestDetailView(long id);
        void showAddNewPrayerRequestView(long contactId);
        void showContactDetail(Cursor contactData);
        void showContactDetailEditView(long contactId);
        void showDBResultMessage(String message);
        void showDeleteContactDialog(Contact myContact);
        void finish();
    }

    public interface Presenter extends BasePresenter {
        void onPrayerRequestClick(long requestId);
        void onAddNewRequestClick();
        void onActiveRequestsClick();
        void onArchiveClick();
        void onContactEditClick(long contactId);
        void onEditClick(long contactId, String firstName, String lastName, String pictureUrl);
        void onContactDeleteClick(long id);
        void onDeleteConfirm(long id);
    }
}
