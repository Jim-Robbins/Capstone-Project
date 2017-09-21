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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.icon_container_prayer_lists) protected RelativeLayout iconPrayerLists;
    @BindView(R.id.icon_container_contact_groups) protected RelativeLayout iconContactGroups;
    @BindView(R.id.icon_container_prayer_requests) protected RelativeLayout iconPrayerRequests;
    @BindView(R.id.icon_container_logout) protected RelativeLayout iconLogout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected Object getModule() {
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @OnClick(R.id.icon_container_prayer_lists)
    public void onPrayerListsClick() {
        startActivity(PrayerListActivity.getStartIntent(this, null));
    }

    @OnClick(R.id.icon_container_prayer_requests)
    public void onPrayerRequestsClick() {
        logEvent("Add Request From Home", null, null);
        startActivity(PrayerRequestDetailActivity.getStartAddIntent(this, ""));
    }

    @OnClick(R.id.icon_container_contact_groups)
    public void onContactGroupsClick() {
        startActivity(ContactGroupActivity.getStartIntent(this, null));
    }

    @OnClick(R.id.icon_container_logout)
    public void setIconLogoutClick() {
        FirebaseDatabase.getInstance().goOffline();
        FirebaseAuth.getInstance().signOut();
        logEvent("logout", null, null);
        startActivity(LoginActivity.getStartIntent(this));
    }
}
