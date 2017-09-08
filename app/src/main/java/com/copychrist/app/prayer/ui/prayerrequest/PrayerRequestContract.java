package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.adapter.PrayerListsListAdapter;
import com.copychrist.app.prayer.model.BibleVerse;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BasePresenter;

import java.util.List;

/**
 * Created by jim on 9/8/17.
 */

public class PrayerRequestContract {
    public interface View {
        void finish();
        void showPrayerRequestError();
        void showAddPrayerRequestDetails(Contact contact, PrayerListsListAdapter prayerListsListAdapter);
        void showEditPrayerRequestDetails(PrayerRequest request, PrayerListsListAdapter prayerListsListAdapter);
        void showBibleVerses(List<BibleVerse> bibleVerses);
        void showBibleVerseDetails(String bibleVerse);
    }

    public interface AddPresenter extends BasePresenter<View> {
        void onSaveNewClick(String title, String desc, String verse, String endDate, String prayerList);
        PrayerRequest onUpdateClick(String title, String desc, String verse, String endDate, String prayerList);
        void onBibleVerseItemlick(String bibleVerse);
        void onScheduleReminderClick();
        void onArchiveRequest();
        void onDeleteRequest();
        void onCancelClick();
        void onContactIconClick();
        void onBibleIconClick();
        void onDateIconClick();
    }

    public interface EditPresenter extends BasePresenter<View> {
        void onSaveNewClick(String title, String desc, String verse, String endDate, String prayerList);
        PrayerRequest onUpdateClick(String title, String desc, String verse, String endDate, String prayerList);
        void onBibleVerseItemlick(String bibleVerse);
        void onScheduleReminderClick();
        void onArchiveRequest();
        void onDeleteRequest();
        void onCancelClick();
        void onContactIconClick();
        void onBibleIconClick();
        void onDateIconClick();
    }
}
