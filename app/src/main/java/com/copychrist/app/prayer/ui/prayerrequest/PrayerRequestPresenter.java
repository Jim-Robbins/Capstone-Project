package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.ViewMode;

import java.util.List;

import timber.log.Timber;

import static com.facebook.stetho.inspector.network.ResponseHandlingInputStream.TAG;

/**
 * Created by jim on 8/19/17.
 */

public class PrayerRequestPresenter implements PrayerRequestContract.Presenter {

    private final PrayerRequestService dataService;
    private final ViewMode viewMode;

    private String contactKey;
    private String prayerRequestKey;

    private Contact selectedContact;
    private PrayerRequest selectedPrayerRequest;
    private PrayerRequestContract.View prayerRequestView;


    public PrayerRequestPresenter(final PrayerRequestService dataService, String key, ViewMode viewMode) {
        this.dataService = dataService;
        this.viewMode = viewMode;

        if (viewMode.equals(ViewMode.EDIT_MODE)) {
            this.prayerRequestKey = key;
        } else {
            this.contactKey = key;
        }
    }

    @Override
    public void setView(PrayerRequestContract.View view) {
        prayerRequestView = view;
        if(contactKey != null) {
           dataService.getContact(this, contactKey);
        } else if (prayerRequestKey != null) {
            dataService.getPrayerRequest(this, prayerRequestKey);
        }
        //PrayerListsListAdapter prayerListsListAdapter = new PrayerListsListAdapter(dataService.getAllPrayerLists());
       // prayerRequestView.showPrayerRequestDetails(selectedContact, prayerListsListAdapter);
    }

    @Override
    public void onContactResults(Contact contact) {
        selectedContact = contact;
        prayerRequestView.showContactDetail(selectedContact);
    }

    @Override
    public void onPrayerRequestResults(PrayerRequest selectedPrayerRequest) {
        this.selectedPrayerRequest = selectedPrayerRequest;
        prayerRequestView.showPrayerRequestDetails(selectedPrayerRequest, null);
    }

    @Override
    public void onBibleVerseResults(List<BiblePassage> results) {
        prayerRequestView.showBibleVerses(selectedPrayerRequest.getPassages());
    }

    @Override
    public void clearView() {
        prayerRequestView = null;
        dataService.destroy();
    }

    @Override
    public void onPrayerRequestSaveClick(PrayerRequest prayerRequest) {
        dataService.saveValue(prayerRequest);
    }

    @Override
    public void onBibleVerseItemClick(String bibleVerse) {
        prayerRequestView.showBibleVerseDetails(bibleVerse);
    }

    @Override
    public void onPrayerRequestScheduleReminderClick() {
        Timber.d("onPrayerRequestScheduleReminderClick");
        //Todo: setup dialog to schedule a notification reminder
    }

    @Override
    public void onPrayerRequestArchive() {
        Timber.d("onPrayerRequestArchive");
        dataService.archivePrayerRequest();
    }

    @Override
    public void onPrayerRequestDelete() {
        Timber.d(TAG, "onPrayerRequestDelete");
        dataService.deletePrayerRequest();
    }

    @Override
    public void onPrayerRequestDeleteCompleted() {
        Timber.d(TAG, "onPrayerRequestDeleteCompleted");
        prayerRequestView.finish();
    }

    @Override
    public void onDataResultMessage(String message) {
        prayerRequestView.showDatabaseResultMessage(message);
    }

    @Override
    public void onDataResultMessage(int messageResId) {
        prayerRequestView.showDatabaseResultMessage(messageResId);
    }

    //    @Override
//    public void onContactIconClick() {
//
//    }
//
//    @Override
//    public void onBibleIconClick() {
//
//    }
//
//    @Override
//    public void onDateIconClick() {
//
//    }

}
