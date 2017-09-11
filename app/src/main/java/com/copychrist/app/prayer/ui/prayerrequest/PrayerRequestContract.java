package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.adapter.PrayerListsListAdapter;
import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerList;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BasePresenter;

import java.util.List;

/**
 * Created by jim on 9/8/17.
 */

public class PrayerRequestContract {
    public interface View {
        void finish();
        void showBibleVerseDetails(String bibleVerse);
        void showBibleVerses(List<String> biblePassages);
        void showContactDetail(Contact selectedContact);
        void showPrayerRequestDetails(PrayerRequest prayerRequest, PrayerListsListAdapter prayerListsListAdapter);
        void showDatabaseResultMessage(String message);
        void showDatabaseResultMessage(int messageResId);
    }

    public interface Presenter extends BasePresenter<View> {
        void onBibleVerseResults(List<BiblePassage> results);
        void onBibleVerseItemClick(String bibleVerse);
        void onDataResultMessage(String message);
        void onDataResultMessage(int messageResId);
        void onPrayerListResults(List<PrayerList> prayerLists);
        void onPrayerRequestDelete();
        void onPrayerRequestDeleteCompleted();
        void onPrayerRequestArchive();
        void onPrayerRequestUnarchive();
        void onPrayerRequestSaveClick(PrayerRequest request);
        void onPrayerRequestScheduleReminderClick();
    }
}
