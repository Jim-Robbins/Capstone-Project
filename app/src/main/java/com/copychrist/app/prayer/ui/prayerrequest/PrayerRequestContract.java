package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.model.BiblePassage;
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
        void showBiblePassageAddDialog();
        void showBiblePassages(List<BiblePassage> listResults, List<BiblePassage> nonListResults);
        void showContactDetail(Contact selectedContact);
        void showPrayerRequestDetails(PrayerRequest prayerRequest);
        void showDatabaseResultMessage(String message);
        void showDatabaseResultMessage(int messageResId);
        void showContactSelector(List<Contact> contacts);
        //void showPrayerListSelector(List<PrayerList> prayerLists);
    }

    public interface Presenter extends BasePresenter<View> {
        void onBiblePassagesAddedToPrayerRequest(List<String> selectedPassageKeys);
        void onBiblePassageAddNew();
        void onBiblePassageSave(BiblePassage biblePassage);
        void onBiblePassageRemove(BiblePassage biblePassage);
        void onBiblePassageDeleteCompleted();
        void onBiblePassageResults(List<BiblePassage> listResults, List<BiblePassage> nonListResults);
        void onContactsResults(List<Contact> contacts);
        void onDataResultMessage(String message);
        void onDataResultMessage(int messageResId);
        //void onPrayerListResults(List<PrayerList> prayerLists);
        void onPrayerRequestDelete();
        void onPrayerRequestDeleteCompleted();
        void onPrayerRequestArchive();
        void onPrayerRequestUnarchive();
        void onPrayerRequestSaveClick(PrayerRequest request);
        void onPrayerRequestScheduleReminderClick();

    }

    public interface Service {
        void onBiblePassageSave(BiblePassage biblePassage);
        void onBiblePassageDelete(BiblePassage biblePassage);
        void onBiblePassagesLoad(List<String> selectedPassages);
        void onBiblePassageRemoveFromPrayerRequest(String prayerRequestKey, String biblePassageRef);
        void onContactsLoad();
        //void onPrayerListsLoad();
        void onPrayerRequestSave(PrayerRequest prayerRequest);
        void onPrayerRequestArchive(PrayerRequest prayerRequest);
        void onPrayerRequestUnarchive(PrayerRequest prayerRequest);
        void onPrayerRequestDelete(PrayerRequest prayerRequest);
        void onDestroy();
        void setPresenter(PrayerRequestContract.Presenter prayerRequestPresenter);
    }
}
