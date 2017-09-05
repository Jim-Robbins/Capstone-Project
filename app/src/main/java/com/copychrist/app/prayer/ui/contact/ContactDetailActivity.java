package com.copychrist.app.prayer.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.PrayerRequestsListAdapter;
import com.copychrist.app.prayer.data.AppRepository;
import com.copychrist.app.prayer.data.LoaderProvider;
import com.copychrist.app.prayer.data.local.AppLocalDataSource;
import com.copychrist.app.prayer.data.model.Contact;
import com.copychrist.app.prayer.data.remote.AppRemoteDataSource;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.components.DeleteDialogFragment;
import com.copychrist.app.prayer.ui.prayerrequest.AddPrayerRequestDetailActivity;
import com.copychrist.app.prayer.ui.prayerrequest.EditPrayerRequestDetailActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactDetailActivity extends BaseActivity
        implements ContactContract.View, PrayerRequestsListAdapter.OnPrayerRequestClickListener,
        DeleteDialogFragment.DeleteActionDialogListener {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.text_contact_first_name) TextView txtFirstName;
    @BindView(R.id.text_contact_last_name) TextView txtLastName;
    @BindView(R.id.appBarLayout) AppBarLayout appBarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.tabLayout) TabLayout tabLayout;

    PrayerRequestsListAdapter prayerRequestsListAdapter;
    @Inject ContactContract.Presenter contactPresenter;

    public static String EXTRA_CONTACT_ID = "extra_contact_id";
    private Contact contact;

    public static Intent getStartIntent(final Context context, final long contactId) {
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
    protected void onResume() {
        super.onResume();
        contactPresenter.start();
    }

    @Override
    protected Object getModule() {
        long contactId = getIntent().getExtras().getLong(EXTRA_CONTACT_ID);

        LoaderProvider loaderProvider = new LoaderProvider(this);
        AppRepository appRepository = new AppRepository(
                new AppRemoteDataSource(),
                new AppLocalDataSource(getContentResolver())
        );

        return new ContactModule(loaderProvider,
                getSupportLoaderManager(),
                appRepository,
                this,
                contactId);
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
                onSupportNavigateUp();
                return true;
            case R.id.action_edit_contact:
                contactPresenter.onContactEditClick(contact.getId());
                return true;
            case R.id.action_delete_contact:
                contactPresenter.onContactDeleteClick(contact.getId());
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
        prayerRequestsListAdapter = new PrayerRequestsListAdapter(this);
        prayerRequestsListAdapter.setPrayerRequestClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(prayerRequestsListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        contactPresenter.start();
    }

    @Override
    public void setPresenter(ContactContract.Presenter presenter) {
        this.contactPresenter = presenter;
    }

    //
//    @Override
//    protected void onStop() {
//        super.onStop();
//        contactPresenter.clearView();
//    }



    @Override
    public void showContactDetail(final Contact contact) {
        this.contact = contact;
        txtFirstName.setText(contact.getFirstName());
        txtLastName.setText(contact.getLastName());
    }

    @Override
    public void showPrayerRequests(Cursor requests) {
        recyclerView.removeAllViews();
        prayerRequestsListAdapter.setCursor(requests);
    }

    @Override
    public void onPrayerRequestClick(long requestId) {
        contactPresenter.onPrayerRequestClick(requestId);
    }

    @OnClick(R.id.fab)
    public void onAddNewPrayerRequestClick() {
        contactPresenter.onAddNewRequestClick();
    }

    @Override
    public void showPrayerRequestDetailView(long requestId) {
        startActivity(EditPrayerRequestDetailActivity.getStartIntent(this, requestId));
    }

    @Override
    public void showAddNewPrayerRequestView(long contactId) {
        startActivity(AddPrayerRequestDetailActivity.getStartIntent(this, contactId));
        tabLayout.getTabAt(0).select();
    }

    @Override
    public void showContactDetailEditView(long contactId) {
        AddEditContactDialogFragment addEditContactDialogFragment = AddEditContactDialogFragment.newEditInstance(contact, contactPresenter);
        addEditContactDialogFragment.show(getSupportFragmentManager(), "EditContactDialogFragment");
    }

    @Override
    public void showDeleteContactDialog(Contact myContact) {
            DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.newInstance(
                    getString(R.string.dialog_delete_contact_title),
                    myContact.getId(),
                    myContact.getFirstName() + " " + myContact.getLastName()
            );
            deleteDialogFragment.show(getSupportFragmentManager(), "DeleteDialogFragment");
    }

    @Override
    public void onConfirmedDeleteDialog(long contactId) {
        contactPresenter.onDeleteConfirm(contactId);
    }

    @Override
    public void showDBResultMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
