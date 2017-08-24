package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.adapter.PrayerListsListAdapter;
import com.copychrist.app.prayer.model.BibleVerse;
import com.copychrist.app.prayer.model.PrayerRequest;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by jim on 8/19/17.
 */

public interface EditPrayerRequestView {

    void finish();
    void showEditPrayerRequestError();
    void showEditPrayerRequestDetails(PrayerRequest request, PrayerListsListAdapter prayerListsListAdapter);
    void showBibleVerses(RealmList<BibleVerse> bibleVerses);
    void showBibleVerseDetails(String bibleVerse);

    class EmptyEditPrayerRequestView implements EditPrayerRequestView {
        @Override
        public void finish() {

        }

        @Override
        public void showEditPrayerRequestError() {

        }

        @Override
        public void showEditPrayerRequestDetails(PrayerRequest request, PrayerListsListAdapter prayerListsListAdapter) {

        }

        @Override
        public void showBibleVerseDetails(String bibleVerse) {

        }

        @Override
        public void showBibleVerses(RealmList<BibleVerse> bibleVerses) {

        }
    }
}
