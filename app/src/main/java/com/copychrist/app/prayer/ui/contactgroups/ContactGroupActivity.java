package com.copychrist.app.prayer.ui.contactgroups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.ContactsSwipeableSelectableRVAdapter;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.components.DeleteDialogFragment;
import com.copychrist.app.prayer.ui.components.MessageDialogFragment;
import com.copychrist.app.prayer.ui.contact.AddEditContactDialogFragment;
import com.copychrist.app.prayer.ui.contact.ContactDetailActivity;
import com.copychrist.app.prayer.ui.prayerrequest.PrayerRequestDetailActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ContactGroupActivity extends BaseActivity implements ContactGroupContract.View,
        ContactsSwipeableSelectableRVAdapter.ContactSwipeableSelectableRVListener,
        DeleteDialogFragment.DeleteActionDialogListener,
        AddContactGroupDialogFragment.AddContactGroupDialogListener,
        AddEditContactDialogFragment.AddEditContactDialogListener,
        MessageDialogFragment.MessageActionDialogListener {

    private static final String TAG = "ContactGroupActivity";

    @BindView(R.id.recycler_view) protected RecyclerView recyclerView;
    @BindView(R.id.empty_contacts) protected TextView txtEmpty;
    @BindView(R.id.layout_constraint) protected ConstraintLayout layout;
    @BindView(R.id.tab_layout_groups) protected TabLayout tabLayoutGroups;
    @Inject protected ContactGroupContract.Presenter contactsPresenter;

    private ContactsSwipeableSelectableRVAdapter contactsSwipeableSelectableRVAdapter;
    private int selectedTabIndex = 0;
    private ContactGroup selectedContactGroup;
    public static String EXTRA_CONTACT_GROUP = "extra_contact_group";
    private List<Contact> contacts;
    private String deleteType;
    private Contact contact;
    private boolean restoreView;
    private boolean listsReady = false;

    public static Intent getStartIntent(final Context context, final ContactGroup contactGroup) {
        Intent intent = new Intent(context, ContactGroupActivity.class);
        if(contactGroup != null) intent.putExtra(EXTRA_CONTACT_GROUP, contactGroup);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        initContactList();
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
                onSupportNavigateUp();
                return true;
            case R.id.action_add_group:
                contactsPresenter.onContactGroupAddClick();
                return true;
            case R.id.action_edit_group:
                if(!listsReady) {
                    showDatabaseResultMessage(R.string.not_yet);
                }
                contactsPresenter.onContactGroupEditClick();
                return true;
            case R.id.action_delete_group:
                if(!listsReady) {
                    showDatabaseResultMessage(R.string.not_yet);
                }
                if(tabLayoutGroups.getTabCount() > 1)
                    contactsPresenter.onContactGroupDeleteClick();
                else
                    showDatabaseResultMessage(getString(R.string.dialog_delete_group_denied));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Object getModule() {
        if (getIntent().hasExtra(EXTRA_CONTACT_GROUP)) {
            selectedContactGroup = getIntent().getParcelableExtra(EXTRA_CONTACT_GROUP);
            if (selectedContactGroup == null) {
                Timber.i("ContactGroup Parcel not properly sent.");
            }
        }
        return new ContactGroupModule(selectedContactGroup);
    }

    private void initContactList() {
        contactsSwipeableSelectableRVAdapter = new ContactsSwipeableSelectableRVAdapter(this, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactsSwipeableSelectableRVAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        contactsPresenter.clearView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        contactsPresenter.saveState();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreView = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(restoreView)
            contactsPresenter.resetView(this);
        else
            contactsPresenter.setView(this);
    }

    @Override
    public void showContacts(List<Contact> contacts, List<PrayerRequest> prayerRequests) {
        if(contacts.isEmpty()) {
            txtEmpty.setText(getString(R.string.empty_contacts));
            txtEmpty.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.GONE);
        }
        this.contacts = contacts;
        contactsSwipeableSelectableRVAdapter.setAdpaterData(contacts, prayerRequests);
    }

    @Override
    public void showContactGroupsTabs(List<ContactGroup> contactGroups, final ContactGroup selectedGroup) {
        if(contactGroups.isEmpty()) {
            txtEmpty.setText(getString(R.string.empty_contact_groups));
            txtEmpty.setVisibility(View.VISIBLE);
            listsReady = false;
        } else {
            txtEmpty.setVisibility(View.GONE);
            listsReady = true;
        }
        tabLayoutGroups.clearOnTabSelectedListeners();
        tabLayoutGroups.removeAllTabs();
        for (ContactGroup contactGroup : contactGroups) {
            Tab tab = tabLayoutGroups.newTab();
            tab.setText(contactGroup.getName());
            tab.setTag(contactGroup);
            tabLayoutGroups.addTab(tab);
            if(selectedGroup != null && contactGroup.getKey().equalsIgnoreCase(selectedGroup.getKey())) {
                selectedContactGroup = selectedGroup;
                selectedTabIndex = tab.getPosition();
                contactsPresenter.onContactGroupClicked(selectedGroup);
            }
        }
        tabLayoutGroups.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag() != null) {
                    Timber.d(TAG, tab.getText() + ":" + tab.getTag().toString());
                    contactsPresenter.onContactGroupClicked((ContactGroup) tab.getTag());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        Tab selectedTab = tabLayoutGroups.getTabAt(selectedTabIndex);
        if(selectedTab != null) {
            selectedTab.select();
        }
    }


    @Override
    public void showContactGroupDialogAdd() {
        AddContactGroupDialogFragment addContactGroupDialogFragment =
                AddContactGroupDialogFragment.newInstance(null, tabLayoutGroups.getTabCount());
        addContactGroupDialogFragment.show(getSupportFragmentManager(), "AddContactGroupDialog");
    }

    @Override
    public void showContactGroupDialogEdit(ContactGroup contactGroup) {
        AddContactGroupDialogFragment addContactGroupDialogFragment =
                AddContactGroupDialogFragment.newInstance(contactGroup, tabLayoutGroups.getTabCount());
        addContactGroupDialogFragment.show(getSupportFragmentManager(), "EditContactGroupDialog");
    }

    @Override
    public void onContactGroupSaveClick(ContactGroup contactGroup) {
        logEvent("Save Contact Group", contactGroup.getName(), null);
        contactsPresenter.onContactGroupSaveClick(contactGroup);
    }

    @Override
    public void showContactGroupDialogDelete(ContactGroup contactGroup) {
        this.deleteType = ContactGroup.DB_NAME;
        if (tabLayoutGroups.getTabCount() > 1) {
            DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.newInstance(
                    getString(R.string.dialog_delete_group_title),
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
    public void onConfirmedDeleteDialog() {
        if(deleteType.equalsIgnoreCase(Contact.DB_NAME)) {
            contactsPresenter.onContactDeleteConfirmed(contact);
        } else {
            contactsPresenter.onContactGroupDeleteConfirmed();
            logEvent("Delete Contact Group", null, null);
        }
    }

    @OnClick(R.id.fab)
    public void onAddNewContactClick() {
        if(!listsReady) {
            showDatabaseResultMessage(R.string.not_yet);
        } else {
            contactsPresenter.onContactAddClick();
        }
    }

    @Override
    public void showContactAddDialog(ContactGroup contactGroup) {
        AddEditContactDialogFragment addEditContactDialogFragment = AddEditContactDialogFragment.newAddInstance(contactGroup);
        addEditContactDialogFragment.show(getSupportFragmentManager(), "AddContactDialogFragment");
    }

    @Override
    public void onContactSaveClick(Contact contact) {
        contactsPresenter.onContactSaveClick(contact);
    }

    @Override
    public void onContactClick(int position) {
        Contact contact = contacts.get(position);
        startActivity(ContactDetailActivity.getStartIntent(this, contact));
    }

    @Override
    public void onRowLongClicked(int position) {
        //Todo: Allow user to sort contacts by dragging
    }

    @Override
    public void onContactRemoveClicked(int position) {
        this.deleteType = Contact.DB_NAME;
        this.contact = contacts.get(position);

        DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.newInstance(
                getString(R.string.dialog_delete_contact_title),
                contact.getFirstName() + contact.getLastName()
        );
        deleteDialogFragment.show(getSupportFragmentManager(), "DeleteDialogFragment");
    }

    @Override
    public void onDialogConfirmClicked() {
        Timber.d("Nothing to see here");
    }

    @Override
    public void onContactEditClicked(int position) {
        Contact contact = contacts.get(position);
        AddEditContactDialogFragment addEditContactDialogFragment = AddEditContactDialogFragment.newEditInstance(contact);
        addEditContactDialogFragment.show(getSupportFragmentManager(), "EditContactDialogFragment");
    }

    @Override
    public void onPrayerRequestClick(PrayerRequest prayerRequest) {
        startActivity(PrayerRequestDetailActivity.getStartEditIntent(this, prayerRequest));
    }

    @Override
    public void showDatabaseResultMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDatabaseResultMessage(int messageResId) {
        Toast.makeText(this, getBaseContext().getString(messageResId), Toast.LENGTH_LONG).show();
    }
}
