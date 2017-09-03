package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.adapter.PrayerListsListAdapter;
import com.copychrist.app.prayer.data.model.Contact;

/**
 * Created by jim on 8/19/17.
 */

public interface AddPrayerRequestView {

    void finish();
    void showAddPrayerRequestError();
    void showAddPrayerRequestDetails(Contact contact, PrayerListsListAdapter prayerListsListAdapter);

    class EmptyAddPrayerRequestView implements AddPrayerRequestView {
        @Override
        public void finish() {

        }

        @Override
        public void showAddPrayerRequestError() {

        }

        @Override
        public void showAddPrayerRequestDetails(Contact contact, PrayerListsListAdapter prayerListsListAdapter) {

        }
    }
}
