package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.data.model.PrayerList;
import com.copychrist.app.prayer.ui.BasePresenter;

/**
 * Created by jim on 8/19/17.
 */

public interface AddPrayerRequestPresenter extends BasePresenter<AddPrayerRequestView> {
    void onSaveNewClick(String title, String desc, String verse, String endDate, String prayerList);
    void onCancelClick();
    void onContactIconClick();
    void onBibleIconClick();
    void onDateIconClick();
}
