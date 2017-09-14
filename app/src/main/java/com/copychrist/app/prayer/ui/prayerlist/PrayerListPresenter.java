package com.copychrist.app.prayer.ui.prayerlist;

import android.content.Context;

import com.copychrist.app.prayer.model.PrayerList;
import com.copychrist.app.prayer.model.PrayerListRequest;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.util.Utils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 9/10/17.
 */

public class PrayerListPresenter implements PrayerListContract.Presenter {
    private final PrayerListService dataService;

    private PrayerList selectedPrayerList;
    private PrayerListContract.View prayerListView;
    private Context context;
    private List<PrayerListRequest> currentListPrayerListRequests;
    private List<PrayerListRequest> fullListPrayerListRequests;

    public PrayerListPresenter(PrayerListService prayerListService, Context context, PrayerList prayerList) {
        this.dataService = prayerListService;
        this.context = context;
        this.selectedPrayerList = prayerList;
    }

    @Override
    public void setView(PrayerListContract.View view) {
        this.prayerListView = view;
        dataService.getPrayerListValues(this);
    }

    @Override
    public void onPrayerListAddClick() {
        prayerListView.showPrayerListDialogAdd();
    }

    @Override
    public void onPrayerListClick(PrayerList prayerList) {
        Timber.d("onPrayerListClick() called with: prayerList = [" + prayerList + "]");
        selectedPrayerList = prayerList;
        dataService.getPrayerRequestValues(selectedPrayerList);
    }

    @Override
    public void onPrayerListDeleteClick() {
        prayerListView.showPrayerListDialogDelete(selectedPrayerList);
    }

    @Override
    public void onPrayerListDeleteConfirmed() {
        dataService.deletePrayerList();
    }

    @Override
    public void onPrayerListEditClick() {
        prayerListView.showPrayerListDialogEdit(selectedPrayerList);
    }

    @Override
    public void onPrayerListResults(List<PrayerList> prayerLists, PrayerList selectedList) {
        this.selectedPrayerList = selectedList;
        prayerListView.showPrayerListTabs(prayerLists, selectedPrayerList);
    }

    @Override
    public void onPrayerListSaveClick(PrayerList prayerList) {
        dataService.savePrayerListValue(prayerList);
    }

    @Override
    public void onPrayerRequestAddClick() {
        prayerListView.showPrayerRequestAddDialog(selectedPrayerList);
    }

    @Override
    public void onPrayerRequestResults(List<PrayerRequest> listRequests, List<PrayerRequest> allPrayerRequests) {
        // Store copy of list filtered by current prayer list
        currentListPrayerListRequests = mapRequestsToListRequests(listRequests);
        prayerListView.showPrayerListRequests(currentListPrayerListRequests);
        
        // Store copy of full list
        fullListPrayerListRequests = mapRequestsToListRequests(allPrayerRequests);
    }

    @Override
    public List<PrayerListRequest> getAllPrayerRequests() {
        return fullListPrayerListRequests;
    }

    @Override
    public void onDataResultMessage(String message) {
        prayerListView.showDatabaseResultMessage(message);
    }

    @Override
    public void onDataResultMessage(int messageResId) {
        prayerListView.showDatabaseResultMessage(messageResId);
    }

    @Override
    public void clearView() {
        prayerListView = null;
        dataService.destroy();
    }

    private List<PrayerListRequest> mapRequestsToListRequests(List<PrayerRequest> requestList) {
        List<PrayerListRequest> prayerListRequests = new ArrayList<>();
        for (PrayerRequest prayerRequest : requestList) {
            PrayerListRequest prayerListRequest = new PrayerListRequest();
            prayerListRequest.setContact(prayerRequest.getContact());
            prayerListRequest.setGroup(prayerRequest.getContactGroup());
            prayerListRequest.setPrayerRequest(prayerRequest);
            prayerListRequest.setColor(Utils.getRandomMaterialColor(context, "400"));
            prayerListRequests.add(prayerListRequest);
        }

        return prayerListRequests;
    }

    @Override
    public void onPrayerRequestsAddToList(List<String> prayerRequestKeys) {
        Timber.d(prayerRequestKeys.toString());
        dataService.updatePrayerRequestWithList(prayerRequestKeys, selectedPrayerList.getKey());
    }

    @Override
    public void onPrayerRequestsAddNewRequest() {
        Timber.d("onPrayerRequestsAddNewRequest");
        prayerListView.showPrayerRequestAdd();
    }

    @Override
    public void onPrayerRequestEditClick(PrayerRequest prayerRequest) {
        prayerListView.showPrayerRequestEdit(prayerRequest);
    }

    @Override
    public void onPrayerRequestArchive(String prayerRequestKey) {
        dataService.archivePrayerRequest(prayerRequestKey);
    }

    @Override
    public void onPrayerRequestRemove(String prayerRequestKey) {
        dataService.removePrayerRequestFromList(prayerRequestKey, selectedPrayerList.getKey());
    }

    @Override
    public void onPrayerRequestPrayedFor(String prayerRequestKey) {
        dataService.prayedForRequest(prayerRequestKey);
    }

    @Override
    public void onPrayerCardPrayedForClick(int position) {
        prayerListView.showPrayerRequestAsPrayedFor(position);
    }
}
