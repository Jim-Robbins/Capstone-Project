package com.copychrist.app.prayer.ui.prayerlist;

import com.copychrist.app.prayer.model.PrayerList;
import com.copychrist.app.prayer.model.PrayerListRequest;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BasePresenter;

import java.util.List;

/**
 * Created by jim on 9/10/17.
 */

public class PrayerListContract {
    public interface View {
        void showPrayerListDialogAdd();
        void showPrayerListDialogDelete(PrayerList prayerList);
        void showPrayerListDialogEdit(PrayerList prayerList);
        void showPrayerListTabs(List<PrayerList> prayerLists, PrayerList selectedList);
        void showPrayerListRequests(List<PrayerListRequest> prayerListRequests);

        void showPrayerRequestAddDialog(PrayerList prayerListKey);

        void showDatabaseResultMessage(String message);
        void showDatabaseResultMessage(int messageResId);
    }

    public interface Presenter extends BasePresenter<PrayerListContract.View> {
        void onPrayerListAddClick();
        void onPrayerListClick(PrayerList prayerList);
        void onPrayerListDeleteClick();
        void onPrayerListDeleteConfirmed();
        void onPrayerListEditClick();
        void onPrayerListResults(List<PrayerList> prayerLists, PrayerList selectedList);
        void onPrayerListSaveClick(PrayerList prayerList);
        void onPrayerRequestAddClick();
        void onPrayerRequestResults(List<PrayerRequest> prayerRequests);
        void onDataResultMessage(String message);
        void onDataResultMessage(int messageResId);
    }
}
