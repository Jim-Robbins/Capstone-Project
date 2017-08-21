package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by jim on 8/19/17.
 */

public interface ContactView {

    void showPrayerRequests(RealmResults<PrayerRequest> requests);
    void showPrayerRequestDetailView(int id);
    void showAddNewPrayerRequestView(int contactId);
    void finish();
    void showContactDetail(Contact contact);
    void showContactDetailEditView(int contactId);

    class EmptyMyListView implements ContactView {
        @Override
        public void showPrayerRequests(RealmResults<PrayerRequest> requests) {

        }

        @Override
        public void showPrayerRequestDetailView(int id) {

        }

        @Override
        public void showAddNewPrayerRequestView(int contactId) {

        }

        @Override
        public void finish() {

        }

        @Override
        public void showContactDetail(Contact contact) {

        }

        @Override
        public void showContactDetailEditView(int contactId) {

        }
    }
}
