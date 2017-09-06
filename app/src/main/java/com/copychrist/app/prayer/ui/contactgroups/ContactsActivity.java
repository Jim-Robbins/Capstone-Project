package com.copychrist.app.prayer.ui.contactgroups;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.ContactsListAdapter;
import com.copychrist.app.prayer.data.AppRepository;
import com.copychrist.app.prayer.data.LoaderProvider;
import com.copychrist.app.prayer.data.local.AppLocalDataSource;
import com.copychrist.app.prayer.data.model.Contact;
import com.copychrist.app.prayer.data.model.ContactGroup;
import com.copychrist.app.prayer.data.model.PrayerRequest;
import com.copychrist.app.prayer.data.remote.AppRemoteDataSource;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.components.DeleteDialogFragment;
import com.copychrist.app.prayer.ui.components.MessageDialogFragment;
import com.copychrist.app.prayer.ui.contact.AddEditContactDialogFragment;
import com.copychrist.app.prayer.ui.contact.ContactDetailActivity;
import com.copychrist.app.prayer.ui.contact.ContactFilter;
import com.copychrist.app.prayer.ui.prayerrequest.EditPrayerRequestDetailActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class ContactsActivity extends BaseActivity implements ContactGroupContract.View,
        ContactsListAdapter.OnContactClickListener, DeleteDialogFragment.DeleteActionDialogListener {

    private static final String TAG = "ContactsActivity";
    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.tab_layout_groups) TabLayout tabLayoutGroups;

    @Inject ContactGroupContract.Presenter contactsPresenter;

    private ContactsListAdapter contactsListAdapter;

    private int selectedTabIndex = 0;
    public static String EXTRA_CONTACT_GROUP_ID = "extra_contact_id";

    public static Intent getStartIntent(final Context context, final long contactGroupId) {
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra(EXTRA_CONTACT_GROUP_ID, contactGroupId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initFilterType(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        initList();
    }

    ContactFilter filter;
    private void initFilterType(Bundle savedInstanceState) {
        // Load previously saved state, if available.
        filter = ContactFilter.from(ContactFilter.FilterType.ALL);
        if (savedInstanceState != null) {
            ContactFilter.FilterType currentFiltering =
                    (ContactFilter.FilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            filter = ContactFilter.from(currentFiltering);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactsPresenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_contact_groups_navigation_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.action_add_group:
                contactsPresenter.addNewContactGroup();
                return true;
            case R.id.action_edit_group:
                contactsPresenter.openContactGroupDetails();
                return true;
            case R.id.action_delete_group:
                contactsPresenter.deleteContactGroupConfirm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Object getModule() {
        int contactGroupId = 1;
        if(getIntent().hasExtra(EXTRA_CONTACT_GROUP_ID)) {
            contactGroupId = getIntent().getExtras().getInt(EXTRA_CONTACT_GROUP_ID);
        }

        LoaderProvider loaderProvider = new LoaderProvider(this);
        AppRepository appRepository = new AppRepository(
                new AppRemoteDataSource(),
                new AppLocalDataSource(getContentResolver())
        );

        return new ContactsModule(
                loaderProvider,
                getSupportLoaderManager(),
                appRepository,
                this,
                filter,
                contactGroupId);
    }

    private void initList() {
        contactsListAdapter = new ContactsListAdapter(this);
        contactsListAdapter.setContactClickListener(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this,
               getResources().getInteger(R.integer.contacts_grid_column));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactsListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        contactsPresenter.start();
    }

    @Override
    public void showContacts(Cursor contacts) {
        contactsListAdapter.setCursor(contacts);
    }


    @Override
    public void setPresenter(ContactGroupContract.Presenter presenter) {
        this.contactsPresenter = presenter;
    }

    @Override
    public void showContactGroupsTabs(Cursor contactGroups, ContactGroup selectedGroup) {
        tabLayoutGroups.clearOnTabSelectedListeners();
        tabLayoutGroups.removeAllTabs();
        if(contactGroups != null) {
            if(contactGroups.moveToFirst()) {
                do {
                    ContactGroup contactGroup = ContactGroup.getFrom(contactGroups);
                    Tab tab = tabLayoutGroups.newTab();
                    tab.setText(contactGroup.getName().toUpperCase());
                    tab.setTag(contactGroup);
                    tabLayoutGroups.addTab(tab);
                    if (contactGroup.getId() == selectedGroup.getId()) {
                        selectedTabIndex = tab.getPosition();
                        contactsPresenter.setContactGroup((ContactGroup) tab.getTag());
                    }
                } while (contactGroups.moveToNext());
            }
            tabLayoutGroups.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Timber.d(TAG, tab.getText() +":"+tab.getTag().toString());
                    contactsPresenter.setContactGroup((ContactGroup) tab.getTag());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            tabLayoutGroups.getTabAt(selectedTabIndex).select();
        }
    }

    @Override
    public void showAddContactGroupDialog() {
        AddContactGroupDialogFragment addContactGroupDialogFragment =
                AddContactGroupDialogFragment.newInstance(null, contactsPresenter);
        addContactGroupDialogFragment.show(getSupportFragmentManager(), "AddContactGroupDialog");
    }


    @Override
    public void showEditContactGroupDialog(ContactGroup contactGroup) {
        AddContactGroupDialogFragment addContactGroupDialogFragment =
                AddContactGroupDialogFragment.newInstance(contactGroup, contactsPresenter);
        addContactGroupDialogFragment.show(getSupportFragmentManager(), "EditContactGroupDialog");
    }

    @Override
    public void showDeleteContactGroupDialog(ContactGroup contactGroup) {
        if (tabLayoutGroups.getTabCount() > 1) {
            DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.newInstance(
                    getString(R.string.dialog_delete_group_title),
                    contactGroup.getId(),
                    contactGroup.getName()
            );
            deleteDialogFragment.show(getSupportFragmentManager(), "DeleteDialogFragment");
        } else {
            MessageDialogFragment dialogFragment = MessageDialogFragment.newInstance(
                getString(R.string.dialog_delete_group_title),
                getString(R.string.dialog_delete_group_denied)
            );
            dialogFragment.show(getSupportFragmentManager(), "DeleteDialogFragment");
        }
    }

    @Override
    public void onConfirmedDeleteDialog(long itemId) {
        contactsPresenter.deleteContactGroup(new ContactGroup(itemId,"","",0));
    }

    @OnClick(R.id.fab)
    public void onAddNewContactClick() {
        contactsPresenter.addNewContact();
    }

    @Override
    public void showAddNewContactView(long groupId) {
        AddEditContactDialogFragment addEditContactDialogFragment = AddEditContactDialogFragment.newAddInstance(groupId, contactsPresenter);
        addEditContactDialogFragment.show(getSupportFragmentManager(), "AddContactDialogFragment");
    }

    @Override
    public void onContactClick(Contact contact) {
        contactsPresenter.openContactDetails(contact);
    }


    @Override
    public void showContactDetailView(@NonNull Contact contact) {
        startActivity(ContactDetailActivity.getStartIntent(this, contact.getId()));
    }

    @Override
    public void onPrayerRequestClick(PrayerRequest prayerRequest) {
        //contactsPresenter.openPrayerRequestDetails(prayerRequest);
    }

    @Override
    public void showPrayerRequestDetailView(long requestId) {
        startActivity(EditPrayerRequestDetailActivity.getStartIntent(this, requestId));
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingError() {
        showMessage(getString(R.string.loading_error));
    }

    @Override
    public void showNoContactGroups() {
        showMessage(getString(R.string.error_no_contact_groups));
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.dialog_contact_group_saved));
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
