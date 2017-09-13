package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerList;
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

    private Contact selectedContact;
    private PrayerRequest selectedPrayerRequest;
    private PrayerRequestContract.View prayerRequestView;
    private String prayerListKey;


    public PrayerRequestPresenter(final PrayerRequestService dataService, Contact contact, PrayerRequest prayerRequest, String prayerListKey) {
        this.dataService = dataService;
        if (prayerRequest == null) {
            this.viewMode = new ViewMode(ViewMode.ADD_MODE);
            this.selectedContact = contact;
        } else if (prayerListKey != null) {
            this.viewMode = new ViewMode(ViewMode.FULL_ADD_MODE);
            this.prayerListKey = prayerListKey;
        } else {
            this.viewMode = new ViewMode(ViewMode.EDIT_MODE);
            this.selectedPrayerRequest = prayerRequest;
        }
    }

    @Override
    public void setView(PrayerRequestContract.View view) {
        prayerRequestView = view;
        if(selectedContact != null) {
            prayerRequestView.showContactDetail(selectedContact);
        } else if (selectedPrayerRequest != null) {
            prayerRequestView.showPrayerRequestDetails(selectedPrayerRequest, null);
        } else {
            //Todo: Show contact selection
        }
    }

    @Override
    public void onPrayerListResults(List<PrayerList> prayerLists) {
        //PrayerListsListAdapter prayerListsListAdapter = new PrayerListsListAdapter(this);
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
        dataService.archivePrayerRequest(this, selectedPrayerRequest);
    }

    @Override
    public void onPrayerRequestUnarchive() {
        Timber.d("onPrayerRequestUnarchive");
        dataService.unarchivePrayerRequest(this, selectedPrayerRequest);
    }

    @Override
    public void onPrayerRequestDelete() {
        Timber.d(TAG, "onPrayerRequestDelete");
        dataService.deletePrayerRequest(this, selectedPrayerRequest);
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

}
