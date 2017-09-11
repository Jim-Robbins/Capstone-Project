package com.copychrist.app.prayer.ui.prayerlist;

import com.copychrist.app.prayer.model.PrayerList;
import com.copychrist.app.prayer.model.PrayerListRequest;
import com.copychrist.app.prayer.model.PrayerRequest;

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

    public PrayerListPresenter(PrayerListService prayerListService, PrayerList prayerList) {
        this.dataService = prayerListService;
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
    public void onPrayerRequestResults(List<PrayerRequest> prayerRequests) {
        List<PrayerListRequest> prayerListRequests = new ArrayList<>();
        for (PrayerRequest prayerRequest : prayerRequests) {
            PrayerListRequest prayerListRequest = new PrayerListRequest();
            prayerListRequest.setContact(prayerRequest.getContact());
            prayerListRequest.setGroup(prayerRequest.getContactGroup());
            prayerListRequest.setPrayerRequest(prayerRequest);
            //Todo: need context ref, prayerListRequest.setColor(Utils.getRandomMaterialColor(this, "mdcolor_400"));
            prayerListRequests.add(prayerListRequest);
        }
        prayerListView.showPrayerListRequests(prayerListRequests);
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
}
