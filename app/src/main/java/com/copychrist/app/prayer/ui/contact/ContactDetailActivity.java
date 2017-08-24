package com.copychrist.app.prayer.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.PrayerRequestsListAdapter;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.contactgroups.AddContactDialog;
import com.copychrist.app.prayer.ui.prayerrequest.AddPrayerRequestDetailActivity;
import com.copychrist.app.prayer.ui.prayerrequest.EditPrayerRequestDetailActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;
import io.realm.RealmResults;
import timber.log.Timber;

import static android.R.attr.id;

public class ContactDetailActivity extends BaseActivity
        implements ContactView, PrayerRequestsListAdapter.OnPrayerRequestClickListener {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.text_contact_first_name) TextView txtFirstName;
    @BindView(R.id.text_contact_last_name) TextView txtLastName;
    @BindView(R.id.appBarLayout) AppBarLayout appBarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.tabLayout) TabLayout tabLayout;

    PrayerRequestsListAdapter prayerRequestsListAdapter;
    @Inject ContactPresenter contactPresenter;

    public static String EXTRA_CONTACT_ID = "extra_contact_id";
    private Contact contact;

    public static Intent getStartIntent(final Context context, final int contactId) {
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra(EXTRA_CONTACT_ID, contactId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ButterKnife.bind(this);

        initToolbar();
        initList();
    }

    @Override
    protected Object getModule() {
        int contactId = getIntent().getExtras().getInt(EXTRA_CONTACT_ID);
        return new ContactModule(contactId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onSupportNavigateUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initToolbar() {
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1:
                        contactPresenter.onArchiveClick();
                        break;
                    case 0:
                    default:
                        contactPresenter.onActiveRequestsClick();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initList() {
        prayerRequestsListAdapter = new PrayerRequestsListAdapter();
        prayerRequestsListAdapter.setPrayerRequestClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(prayerRequestsListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        contactPresenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        contactPresenter.clearView();
    }

    @Override
    protected void closeRealm() {
        contactPresenter.closeRealm();
    }


    @Override
    public void showContactDetail(final Contact contact) {
        this.contact = contact;
        txtFirstName.setText(contact.getFirstName());
        txtLastName.setText(contact.getLastName());
    }

    @Override
    public void showPrayerRequests(RealmResults<PrayerRequest> requests) {
        recyclerView.removeAllViews();
        prayerRequestsListAdapter.setPrayerRequests(requests);
    }

    @Override
    public void onPrayerRequestClick(int requestId) {
        contactPresenter.onPrayerRequestClick(requestId);
    }

    @OnClick(R.id.fab)
    public void onAddNewPrayerRequestClick() {
        contactPresenter.onAddNewRequestClick();
    }

    @Override
    public void showPrayerRequestDetailView(int requestId) {
        startActivity(EditPrayerRequestDetailActivity.getStartIntent(this, requestId));
    }

    @Override
    public void showAddNewPrayerRequestView(int contactId) {
        startActivity(AddPrayerRequestDetailActivity.getStartIntent(this, contactId));
        tabLayout.getTabAt(0).select();
    }

    @Override
    public void showContactDetailEditView(int contactId) {
        EditContactDialog editContactDialog = new EditContactDialog(this, contact, contactPresenter);
        editContactDialog.show();
    }

}
