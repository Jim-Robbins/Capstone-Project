package com.copychrist.app.prayer.ui.home;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.contactgroups.ContactGroupActivity;
import com.copychrist.app.prayer.ui.login.LoginActivity;
import com.copychrist.app.prayer.ui.prayerlist.PrayerListActivity;
import com.copychrist.app.prayer.ui.prayerrequest.PrayerRequestDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainContract.View {

    @BindView(R.id.icon_container_prayer_lists) RelativeLayout iconPrayerLists;
    @BindView(R.id.icon_container_contact_groups) RelativeLayout iconContactGroups;
    @BindView(R.id.icon_container_prayer_requests) RelativeLayout iconPrayerRequests;
    @BindView(R.id.icon_container_logout) RelativeLayout iconLogout;

    @Inject MainContract.Presenter mainPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected Object getModule() {
        return new MainModule();
    }

    @Override
    public void showLogin() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainPresenter.clearView();
    }

    @OnClick(R.id.icon_container_prayer_lists)
    public void onPrayerListsClick() {
        startActivity(PrayerListActivity.getStartIntent(this, null));
    }

    @OnClick(R.id.icon_container_prayer_requests)
    public void onPrayerRequestsClick() {
        startActivity(PrayerRequestDetailActivity.getStartAddIntent(this, "zzz"));
    }

    @OnClick(R.id.icon_container_contact_groups)
    public void onContactGroupsClick() {
        startActivity(ContactGroupActivity.getStartIntent(this, null));
    }

    @OnClick(R.id.icon_container_logout)
    public void setIconLogoutClick() {
        FirebaseDatabase.getInstance().goOffline();
        FirebaseAuth.getInstance().signOut();
        startActivity(LoginActivity.getStartIntent(this));
    }
}
