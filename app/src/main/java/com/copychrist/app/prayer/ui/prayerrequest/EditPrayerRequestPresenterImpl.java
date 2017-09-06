package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.adapter.PrayerListsListAdapter;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.repository.DatabaseSerivce;

import timber.log.Timber;

/**
 * Created by jim on 8/19/17.
 */

public class EditPrayerRequestPresenterImpl
        implements EditPrayerRequestPresenter, DatabaseSerivce.OnTransactionCallback {

    private static final String TAG = "[EditPRequestPresenterImpl] ";
    private final DatabaseSerivce realmService;
    private int requestId;
    private PrayerRequest myRequest;
    private EditPrayerRequestView myEditView = new EditPrayerRequestView.EmptyEditPrayerRequestView();

    public EditPrayerRequestPresenterImpl(final DatabaseSerivce realmService, int requestId) {
        this.realmService = realmService;
        this.requestId = requestId;
    }

    @Override
    public void setView(EditPrayerRequestView view) {
        myEditView = view;
        if(requestId != 0) {
            myRequest = realmService.getPrayerRequest(requestId);
        }
        PrayerListsListAdapter prayerListsListAdapter = new PrayerListsListAdapter(realmService.getAllPrayerLists());
        myEditView.showEditPrayerRequestDetails(myRequest, prayerListsListAdapter);
        showBibleVerses();
    }

    private void showBibleVerses() {
        myEditView.showBibleVerses(myRequest.getVerses());
    }

    @Override
    public void clearView() {
        myEditView = new EditPrayerRequestView.EmptyEditPrayerRequestView();
    }

    @Override
    public void closeRealm() {
        realmService.closeRealm();
    }

    @Override
    public PrayerRequest onUpdateClick(String title, String desc, String verse, String endDate, String prayerList) {
        return realmService.editPrayerRequest(requestId, title, desc, verse, endDate, prayerList);
    }

    @Override
    public void onScheduleReminderClick() {
        Timber.d(TAG, "onScheduleReminderClick");
        //Todo: setup dialog to schedule a notification reminder
    }

    @Override
    public void onArchiveRequest() {
        Timber.d(TAG, "onArchiveRequest");
        realmService.archivePrayerRequest(requestId);
    }

    @Override
    public void onDeleteRequest() {
        Timber.d(TAG, "onDeleteRequest");
        realmService.deletePrayerRequest(requestId);
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
    public void onRealmSuccess() {
        myEditView.finish();
    }

    @Override
    public void onRealmError(Throwable e) {
        e.printStackTrace();
        myEditView.showEditPrayerRequestError();
    }
}
