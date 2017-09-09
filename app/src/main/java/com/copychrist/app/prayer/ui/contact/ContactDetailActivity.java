package com.copychrist.app.prayer.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.components.DeleteDialogFragment;
import com.copychrist.app.prayer.ui.prayerrequest.AddPrayerRequestDetailActivity;
import com.copychrist.app.prayer.ui.prayerrequest.EditPrayerRequestDetailActivity;

import java.util.List;

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
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.tabLayout) TabLayout tabLayout;

    PrayerRequestsListAdapter prayerRequestsListAdapter;
    @Inject ContactContract.Presenter contactPresenter;

    public static String EXTRA_CONTACT_KEY = "extra_contact_key";
    private Contact contact;

    public static Intent getStartIntent(final Context context, final String contactKey) {
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra(EXTRA_CONTACT_KEY, contactKey);
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
        String contactKey = getIntent().getExtras().getString(EXTRA_CONTACT_KEY);
        return new ContactModule(contactKey);
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
    public void onPrayerRequestClick(String requestId) {
        showPrayerRequestDetailView(requestId);
    }

    @OnClick(R.id.fab)
    public void onAddNewPrayerRequestClick() {
        showAddNewPrayerRequestView();
    }

    @Override
    public void onConfirmedDeleteDialog(String contactKey) {
        contactPresenter.onDeleteConfirm();
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
    public void showContactDetail(final Contact contact) {
        this.contact = contact;
        txtFirstName.setText(contact.getFirstName());
        txtLastName.setText(contact.getLastName());
    }

    @Override
    public void showPrayerRequests(List<PrayerRequest> requests) {
        recyclerView.removeAllViews();
        prayerRequestsListAdapter.setPrayerRequests(requests);
    }

    @Override
    public void showDatabaseResultMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void initList() {
        prayerRequestsListAdapter = new PrayerRequestsListAdapter();
        prayerRequestsListAdapter.setPrayerRequestClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(prayerRequestsListAdapter);
    }

    private void showPrayerRequestDetailView(String requestId) {
        startActivity(EditPrayerRequestDetailActivity.getStartIntent(this, requestId));
    }

    private void showAddNewPrayerRequestView() {
        startActivity(AddPrayerRequestDetailActivity.getStartIntent(this, contact.getKey()));
        if(tabLayout != null && tabLayout.getTabAt(0) != null) {
            tabLayout.getTabAt(0).select();
        }
    }

    private void showContactDetailEditView() {
        AddEditContactDialogFragment addEditContactDialogFragment = AddEditContactDialogFragment.newEditInstance(contact, contactPresenter);
        addEditContactDialogFragment.show(getSupportFragmentManager(), "EditContactDialogFragment");
    }

    private void showDeleteContactDialog() {
            DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.newInstance(
                    getString(R.string.dialog_delete_contact_title),
                    contact.getKey(),
                    contact.getFirstName() + " " + contact.getLastName()
            );
            deleteDialogFragment.show(getSupportFragmentManager(), "DeleteDialogFragment");
    }

}
