package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.data.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BasePresenter;

/**
 * Created by jim on 8/19/17.
 */

public interface EditPrayerRequestPresenter extends BasePresenter<EditPrayerRequestView> {
    PrayerRequest onUpdateClick(String title, String desc, String verse, String endDate, String prayerList);
    void onBibleVerseItemlick(String bibleVerse);
    void onScheduleReminderClick();
    void onArchiveRequest();
    void onDeleteRequest();
    void onBibleIconClick();
    void onDateIconClick();
}
