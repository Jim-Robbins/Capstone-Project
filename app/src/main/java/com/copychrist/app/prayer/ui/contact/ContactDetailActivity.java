package com.copychrist.app.prayer.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.PrayerRequestsSwipeableAdapter;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.components.DeleteDialogFragment;
import com.copychrist.app.prayer.ui.prayerrequest.PrayerRequestDetailActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ContactDetailActivity extends BaseActivity
        implements ContactContract.View, PrayerRequestsSwipeableAdapter.PrayerRequestSwipeableListener,
        DeleteDialogFragment.DeleteActionDialogListener,
        AddEditContactDialogFragment.AddEditContactDialogListener {

    @BindView(R.id.recycler_view) protected RecyclerView recyclerView;
    @BindView(R.id.txt_contact_first_name) protected TextView txtFirstName;
    @BindView(R.id.txt_contact_last_name)protected  TextView txtLastName;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.fab) protected FloatingActionButton fab;
    @BindView(R.id.tabLayout) protected TabLayout tabLayout;

    protected PrayerRequestsSwipeableAdapter prayerRequestsSwipeableAdapter;
    @Inject protected ContactContract.Presenter contactPresenter;

    public static String CONTACT_VIEW = "ContactDetailActivity_View";
    public static String EXTRA_CONTACT = "extra_contact";
    private Contact contact;
    private List<PrayerRequest> prayerRequests;
    private boolean restoreView;
    private int selectedTabIndex = 0;

    public static Intent getStartIntent(final Context context, final Contact contact) {
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra(EXTRA_CONTACT, contact);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ButterKnife.bind(this);

        initList();
    }

    @Override
    protected Object getModule() {
        if (getIntent().hasExtra(EXTRA_CONTACT)) {
            contact = getIntent().getParcelableExtra(EXTRA_CONTACT);
            if (contact == null) {
                Timber.e("ContactGroup Parcel not properly sent.");
            }
        }
        return new ContactModule(contact);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_contact_navigation_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_edit_contact:
                showContactDetailEditView();
                return true;
            case R.id.action_delete_contact:
                showDeleteContactDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrayerRequestClick(int position) {
        PrayerRequest prayerRequest = prayerRequests.get(position);
        showPrayerRequestDetailView(prayerRequest);
    }

//    @Override
//    public void onRemoveClicked(int position) {
//        PrayerRequest prayerRequest = prayerRequests.get(position);
//        showDeletePrayerRequestDialog(position);
//    }
//
//    @Override
//    public void onArchiveClicked(int position) {
//        PrayerRequest prayerRequest = prayerRequests.get(position);
//        Timber.d(prayerRequest.toString());
//    }

    @Override
    public void onRowLongClicked(int position) {
        //Todo: Allow user to sort the order of the requess?
    }

    @OnClick(R.id.fab)
    public void onAddNewPrayerRequestClick() {
        showAddNewPrayerRequestView();
    }

    @Override
    public void onConfirmedDeleteDialog() {
        contactPresenter.onContactDeleteConfirm();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        contactPresenter.clearView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CONTACT_VIEW, selectedTabIndex);
        contactPresenter.saveState();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedTabIndex = savedInstanceState.getInt(CONTACT_VIEW);
        restoreView = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(restoreView)
            contactPresenter.resetView(this);
        else
            contactPresenter.setView(this);

        initToolbar();
    }

    @Override
    public void showContactDetail(final Contact contact) {
        this.contact = contact;
        txtFirstName.setText(contact.getFirstName());
        txtLastName.setText(contact.getLastName());
    }

    @Override
    public void showPrayerRequests(List<PrayerRequest> prayerRequests) {
        if(prayerRequests == null) return;
        this.prayerRequests = prayerRequests;
        recyclerView.removeAllViews();
        prayerRequestsSwipeableAdapter.setPrayerRequests(prayerRequests);
    }

    @Override
    public void showDatabaseResultMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDatabaseResultMessage(int messageResId) {
        Toast.makeText(this, getBaseContext().getString(messageResId), Toast.LENGTH_SHORT).show();
    }

    private void initToolbar() {
        this.setSupportActionBar(toolbar);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabIndex = tab.getPosition();
                switch (tab.getPosition()) {
                    case 1:
                        contactPresenter.onPrayerRequestGetArchivedClick();
                        fab.setEnabled(false);
                        break;
                    case 0:
                    default:
                        contactPresenter.onPrayerRequestGetActiveClick();
                        fab.setEnabled(true);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        TabLayout.Tab selectedTab = tabLayout.getTabAt(selectedTabIndex);
        if(selectedTab != null) {
            selectedTab.select();
        }
    }

    private void initList() {
        prayerRequestsSwipeableAdapter = new PrayerRequestsSwipeableAdapter();
        prayerRequestsSwipeableAdapter.setPrayerRequestClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(prayerRequestsSwipeableAdapter);
    }

    private void showPrayerRequestDetailView(PrayerRequest prayerRequest) {
        startActivity(PrayerRequestDetailActivity.getStartEditIntent(this, prayerRequest));
    }

    private void showAddNewPrayerRequestView() {
        startActivity(PrayerRequestDetailActivity.getStartAddIntent(this, contact));
        if(tabLayout != null && tabLayout.getTabAt(0) != null) {
            tabLayout.getTabAt(0).select();
        }
    }

    private void showContactDetailEditView() {
        AddEditContactDialogFragment addEditContactDialogFragment = AddEditContactDialogFragment.newEditInstance(contact);
        addEditContactDialogFragment.show(getSupportFragmentManager(), "EditContactDialogFragment");
    }

    @Override
    public void onContactSaveClick(Contact contact) {
        contactPresenter.onContactSaveClick(contact);
    }

    private void showDeleteContactDialog() {
            DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.newInstance(
                    getString(R.string.dialog_delete_contact_title),
                    contact.getFirstName() + " " + contact.getLastName()
            );
            deleteDialogFragment.show(getSupportFragmentManager(), "DeleteDialogFragment");
    }
}
