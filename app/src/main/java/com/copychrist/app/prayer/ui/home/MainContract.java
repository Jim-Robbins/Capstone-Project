package com.copychrist.app.prayer.ui.home;

import com.copychrist.app.prayer.ui.BasePresenter;

/**
 * Created by jim on 9/11/17.
 */

public class MainContract {
    public interface View {
        void showLogin();
    }

    public interface Presenter extends BasePresenter<MainContract.View> {
        void onCheckLoginStatus();
        void onPrayerListsClick();
        void onPrayerRequestsClick();
        void onContactGroupsClick();
        void onContactsClick();
        void onBibleVersesClick();
    }
}
