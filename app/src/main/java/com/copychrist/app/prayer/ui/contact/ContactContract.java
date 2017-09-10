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
        void showContactDetail(Contact contact);
        void showDatabaseResultMessage(String message);
        void showDatabaseResultMessage(int messageResId);
        void showPrayerRequests(List<PrayerRequest> prayerRequests);
        void finish();
    }

    public interface Presenter extends BasePresenter<ContactContract.View> {
        void onContactDeleteCompleted();
        void onContactDeleteConfirm();
        void onContactResults(Contact contact);
        void onContactSaveClick(Contact contact);
        void onDataResultMessage(String message);
        void onDataResultMessage(int messageResId);
        void onPrayerRequestGetActiveClick();
        void onPrayerRequestGetArchivedClick();
        void onPrayerRequestResults(List<PrayerRequest> prayerRequests);
    }
}
