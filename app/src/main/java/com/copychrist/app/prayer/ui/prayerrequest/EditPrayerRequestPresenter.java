package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.model.PrayerRequest;

import timber.log.Timber;

/**
 * Created by jim on 8/19/17.
 */

public class EditPrayerRequestPresenter implements PrayerRequestContract.EditPresenter {

    private static final String TAG = "[EditPRequestPresenterImpl] ";
    private final PrayerRequestService dataService;
    private String requestId;
    private PrayerRequest myRequest;
    private PrayerRequestContract.View myEditView;

    public EditPrayerRequestPresenter(final PrayerRequestService dataService, String requestId) {
        this.dataService = dataService;
        this.requestId = requestId;
    }

    @Override
    public void setView(PrayerRequestContract.View view) {
        myEditView = view;
        if(requestId != null) {
            //myRequest = dataService.getPrayerRequest(requestId);
        }
        //PrayerListsListAdapter prayerListsListAdapter = new PrayerListsListAdapter(dataService.getAllPrayerLists());
        //myEditView.showEditPrayerRequestDetails(myRequest, prayerListsListAdapter);
        showBibleVerses();
    }

    private void showBibleVerses() {
        myEditView.showBibleVerses(myRequest.getVerses());
    }

    @Override
    public void clearView() {
        myEditView = null;
    }

    @Override
    public PrayerRequest onUpdateClick(String title, String desc, String verse, String endDate, String prayerList) {
        return null; //dataService.editPrayerRequest(requestId, title, desc, verse, endDate, prayerList);
    }

    @Override
    public void onScheduleReminderClick() {
        Timber.d(TAG, "onScheduleReminderClick");
        //Todo: setup dialog to schedule a notification reminder
    }

    @Override
    public void onArchiveRequest() {
        Timber.d(TAG, "onArchiveRequest");
        //dataService.archivePrayerRequest(requestId);
    }

    @Override
    public void onDeleteRequest() {
        Timber.d(TAG, "onDeleteRequest");
        //dataService.deletePrayerRequest(requestId);
    }

    @Override
    public void onBibleIconClick() {

    }

    @Override
    public void onDateIconClick() {

    }

    @Override
    public void onBibleVerseItemlick(String bibleVerse) {
        myEditView.showBibleVerseDetails(bibleVerse);
    }

    @Override
    public void onSaveNewClick(String title, String desc, String verse, String endDate, String prayerList) {

    }

    @Override
    public void onCancelClick() {

    }

    @Override
    public void onContactIconClick() {

    }
}
