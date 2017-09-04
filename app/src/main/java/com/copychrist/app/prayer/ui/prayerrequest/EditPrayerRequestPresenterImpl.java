package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.adapter.PrayerListsListAdapter;
import com.copychrist.app.prayer.data.AppDataSource;
import com.copychrist.app.prayer.data.model.PrayerRequest;

import timber.log.Timber;

/**
 * Created by jim on 8/19/17.
 */

public class EditPrayerRequestPresenterImpl
        implements EditPrayerRequestPresenter {

    private static final String TAG = "[EditPRequestPresenterImpl] ";
//    private final AppDataSource dataSource;
    private long requestId;
    private PrayerRequest myRequest;
    private EditPrayerRequestView myEditView = new EditPrayerRequestView.EmptyEditPrayerRequestView();

    public EditPrayerRequestPresenterImpl(long requestId) {
//        this.dataSource = dataSource;
        this.requestId = requestId;
    }

    @Override
    public void start() {

    }

    //    @Override
    public void setView(EditPrayerRequestView view) {
        myEditView = view;
        if(requestId != 0) {
//            myRequest = dataSource.getPrayerRequest(requestId);
        }
//        PrayerListsListAdapter prayerListsListAdapter = new PrayerListsListAdapter(dataSource.getAllPrayerLists());
//        myEditView.showEditPrayerRequestDetails(myRequest, prayerListsListAdapter);
//        showBibleVerses();
    }

    private void showBibleVerses() {
//        myEditView.showBibleVerses(myRequest.getVerses());
    }

    //@Override
    public void clearView() {
        myEditView = new EditPrayerRequestView.EmptyEditPrayerRequestView();
    }

    @Override
    public PrayerRequest onUpdateClick(String title, String desc, String verse, String endDate, String prayerList) {
        return null;
    //    return dataSource.editPrayerRequest(requestId, title, desc, verse, endDate, prayerList);
    }

    @Override
    public void onScheduleReminderClick() {
        Timber.d(TAG, "onScheduleReminderClick");
        //Todo: setup dialog to schedule a notification reminder
    }

    @Override
    public void onArchiveRequest() {
        Timber.d(TAG, "onArchiveRequest");
     //   dataSource.archivePrayerRequest(requestId);
    }

    @Override
    public void onDeleteRequest() {
        Timber.d(TAG, "onDeleteRequest");
     //   dataSource.deletePrayerRequest(requestId);
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

//    @Override
//    public void onDBSuccess() {
//        myEditView.finish();
//    }
//
//    @Override
//    public void onDBError(Throwable e) {
//        e.printStackTrace();
//        myEditView.showEditPrayerRequestError();
//    }
}
