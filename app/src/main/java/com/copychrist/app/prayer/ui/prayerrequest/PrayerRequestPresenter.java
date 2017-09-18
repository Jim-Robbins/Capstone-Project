package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.ViewMode;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 8/19/17.
 */

public class PrayerRequestPresenter implements PrayerRequestContract.Presenter {

    private final ViewMode viewMode;

    private Contact selectedContact;
    private PrayerRequest selectedPrayerRequest;
    private final PrayerRequestContract.Service dataService;
    private PrayerRequestContract.View prayerRequestView;
    private String prayerListKey;
    private List<BiblePassage> listResults;
    private List<BiblePassage> nonListResults;

    public PrayerRequestPresenter(final PrayerRequestService dataService, Contact contact, PrayerRequest prayerRequest, String prayerListKey) {
        this.dataService = dataService;
        dataService.setPresenter(this);
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
        List<String> selectedBiblePassages = null;
        if(selectedContact != null) {
            prayerRequestView.showContactDetail(selectedContact);
        } else if (selectedPrayerRequest != null) {
            selectedBiblePassages = selectedPrayerRequest.getBiblePassagesAsList();
            prayerRequestView.showPrayerRequestDetails(selectedPrayerRequest);
        } else {
            dataService.onContactsLoad();
        }

        dataService.onBiblePassagesLoad(selectedBiblePassages);
        //dataService.onPrayerListsLoad();
    }

    @Override
    public void clearView() {
        prayerRequestView = null;
        dataService.onDestroy();
    }
//
//    @Override
//    public void onPrayerListResults(List<PrayerList> prayerLists) {
//        prayerRequestView.showPrayerListSelector(prayerLists);
//    }

    @Override
    public void onBiblePassageResults(List<BiblePassage> listResults, List<BiblePassage> nonListResults) {
        this.listResults = listResults;
        this.nonListResults = nonListResults;
        prayerRequestView.showBiblePassages(listResults, nonListResults);
    }

    @Override
    public void onPrayerRequestSaveClick(PrayerRequest prayerRequest) {
        dataService.onPrayerRequestSave(prayerRequest);
    }

    @Override
    public void onPrayerRequestScheduleReminderClick() {
        Timber.d("onPrayerRequestScheduleReminderClick");
        //Todo: setup dialog to schedule a notification reminder
    }

    @Override
    public void onPrayerRequestArchive() {
        Timber.d("onPrayerRequestArchive");
        dataService.onPrayerRequestArchive(selectedPrayerRequest);
    }

    @Override
    public void onPrayerRequestUnarchive() {
        Timber.d("onPrayerRequestUnarchive");
        dataService.onPrayerRequestUnarchive(selectedPrayerRequest);
    }

    @Override
    public void onPrayerRequestDelete() {
        Timber.d("onPrayerRequestDelete");
        dataService.onPrayerRequestDelete(selectedPrayerRequest);
    }

    @Override
    public void onPrayerRequestDeleteCompleted() {
        Timber.d("onPrayerRequestDeleteCompleted");
        if(prayerRequestView != null) {
            prayerRequestView.finish();
        }
    }

    @Override
    public void onDataResultMessage(String message) {
        if(prayerRequestView != null)
            prayerRequestView.showDatabaseResultMessage(message);
    }

    @Override
    public void onDataResultMessage(int messageResId) {
        if(prayerRequestView != null)
            prayerRequestView.showDatabaseResultMessage(messageResId);
    }

    @Override
    public void onBiblePassagesAddedToPrayerRequest(List<String> selectedPassageKeys) {
        if(selectedPassageKeys != null) {
            if (selectedPrayerRequest == null) {
                selectedPrayerRequest = new PrayerRequest();
            }
            HashMap<String, Boolean> passagesMap = selectedPrayerRequest.getBiblePassages();
            for (String passage: selectedPassageKeys) {
                passagesMap.put(passage, true);
            }
        }

        for (BiblePassage passage : nonListResults) {
            Timber.d(passage.getKey() + " contained in + " + selectedPassageKeys.toString());
            if(selectedPassageKeys.contains(passage.getPassageReference())) {
                listResults.add(passage);
            }
        }
        for (BiblePassage removePassage : listResults) {
            nonListResults.remove(removePassage);
        }

        prayerRequestView.showBiblePassages(listResults, nonListResults);
    }

    @Override
    public void onBiblePassageAddNew() {
        prayerRequestView.showBiblePassageAddDialog();
    }

    @Override
    public void onBiblePassageSave(BiblePassage biblePassage) {
        dataService.onBiblePassageSave(biblePassage);
    }

    @Override
    public void onBiblePassageDeleteCompleted() {

    }

    @Override
    public void onContactsResults(List<Contact> contacts) {
        prayerRequestView.showContactSelector(contacts);
    }

    @Override
    public void onBiblePassageRemove(BiblePassage biblePassage) {
        nonListResults.add(biblePassage);
        listResults.remove(biblePassage);
        prayerRequestView.showBiblePassages(listResults, nonListResults);
        if(selectedPrayerRequest.getKey() != null) {
            dataService.onBiblePassageRemoveFromPrayerRequest(selectedPrayerRequest.getKey(), biblePassage.getPassageReference());
        }
    }
}
