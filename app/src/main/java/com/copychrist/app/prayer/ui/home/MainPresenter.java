package com.copychrist.app.prayer.ui.home;

/**
 * Created by jim on 9/11/17.
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mainView;
    public MainPresenter(MainService mainService) {
        super();
    }

    @Override
    public void setView(MainContract.View view) {
        mainView = view;
    }

    @Override
    public void clearView() {
        mainView = null;
    }

    @Override
    public void onCheckLoginStatus() {

    }

    @Override
    public void onPrayerListsClick() {

    }

    @Override
    public void onPrayerRequestsClick() {

    }

    @Override
    public void onContactGroupsClick() {

    }

    @Override
    public void onContactsClick() {

    }

    @Override
    public void onBibleVersesClick() {

    }
}
