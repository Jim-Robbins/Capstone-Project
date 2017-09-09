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
        void showContactDetail(Contact contact);
        void showDatabaseResultMessage(String message);
        void finish();
    }

    public interface Presenter extends BasePresenter<ContactContract.View> {
        void onGetContactResult(Contact contact);
        void onActiveRequestsClick();
        void onArchiveClick();
        void onSaveContactClick(Contact contact);
        void onDeleteConfirm();
        void onDeleteCompleted();
        void onDataResultMessage(String message);
    }
}
