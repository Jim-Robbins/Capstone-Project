package com.copychrist.app.prayer.ui.contactgroups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.ContactsListAdapter;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;
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
        ContactsListAdapter.OnContactClickListener, DeleteDialogFragment.DeleteActionDialogListener {

    private static final String TAG = "ContactGroupActivity";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.tab_layout_groups) TabLayout tabLayoutGroups;
    @Inject ContactGroupContract.Presenter contactsPresenter;

    private ContactsListAdapter contactsListAdapter;
    private int selectedTabIndex = 0;
    public static String EXTRA_CONTACT_GROUP_KEY = "extra_contact_group_key";

    public static Intent getStartIntent(final Context context, final String contactGroupKey) {
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra(EXTRA_CONTACT_GROUP_KEY, contactGroupKey);
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
                return true;
            case R.id.action_add_group:
                contactsPresenter.onContactGroupAddClick();
                return true;
            case R.id.action_edit_group:
                contactsPresenter.onContactGroupEditClick();
                return true;
            case R.id.action_delete_group:
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
        String contactGroupKey = "";
        if(getIntent().hasExtra(EXTRA_CONTACT_GROUP_KEY)) {
            contactGroupKey = getIntent().getExtras().getString(EXTRA_CONTACT_GROUP_KEY);
        }
        return new ContactGroupModule(contactGroupKey);
    }

    private void initContactList() {
        contactsListAdapter = new ContactsListAdapter();
        contactsListAdapter.setContactClickListener(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this,
               getResources().getInteger(R.integer.contacts_grid_column));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactsListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        contactsPresenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        contactsPresenter.clearView();
    }

    @Override
    public void showContacts(List<Contact> contacts) {
        contactsListAdapter.setContacts(contacts);
    }

    @Override
    public void showContactGroupsTabs(List<ContactGroup> contactGroups, final ContactGroup selectedGroup) {
        tabLayoutGroups.clearOnTabSelectedListeners();
        tabLayoutGroups.removeAllTabs();
        for (ContactGroup contactGroup : contactGroups) {
            Tab tab = tabLayoutGroups.newTab();
            tab.setText(contactGroup.getName());
            tab.setTag(contactGroup);
            tabLayoutGroups.addTab(tab);
            if(selectedGroup != null && contactGroup.getKey().equalsIgnoreCase(selectedGroup.getKey())) {
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
            tabLayoutGroups.getTabAt(selectedTabIndex).select();
        }
    }


    @Override
    public void showContactGroupDialogAdd() {
        AddContactGroupDialogFragment addContactGroupDialogFragment =
                AddContactGroupDialogFragment.newInstance(null, contactsPresenter, tabLayoutGroups.getTabCount());
        addContactGroupDialogFragment.show(getSupportFragmentManager(), "AddContactGroupDialog");
    }

    @Override
    public void showContactGroupDialogEdit(ContactGroup contactGroup) {
        AddContactGroupDialogFragment addContactGroupDialogFragment =
                AddContactGroupDialogFragment.newInstance(contactGroup, contactsPresenter, tabLayoutGroups.getTabCount());
        addContactGroupDialogFragment.show(getSupportFragmentManager(), "EditContactGroupDialog");
    }

    @Override
    public void showContactGroupDialogDelete(ContactGroup contactGroup) {
        if (tabLayoutGroups.getTabCount() > 1) {
            DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.newInstance(
                    getString(R.string.dialog_delete_group_title),
                    contactGroup.getName(),
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
    public void onConfirmedDeleteDialog(String itemId) {
        contactsPresenter.onContactGroupDeleteConfirmed();
    }

    @OnClick(R.id.fab)
    public void onAddNewContactClick() {
        contactsPresenter.onContactAddClick();
    }

    @Override
    public void showContactAddDialog(String groupName) {
        AddEditContactDialogFragment addEditContactDialogFragment = AddEditContactDialogFragment.newAddInstance(groupName, contactsPresenter);
        addEditContactDialogFragment.show(getSupportFragmentManager(), "AddContactDialogFragment");
    }

    @Override
    public void onContactClick(String contactKey) {
        startActivity(ContactDetailActivity.getStartIntent(this, contactKey));
    }

    @Override
    public void onPrayerRequestClick(String prayerRequestKey) {
        startActivity(PrayerRequestDetailActivity.getStartEditIntent(this, prayerRequestKey));
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
