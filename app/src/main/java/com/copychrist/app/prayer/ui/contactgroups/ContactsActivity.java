package com.copychrist.app.prayer.ui.contactgroups;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.copychrist.app.prayer.data.model.ContactGroup;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.components.DeleteDialogFragment;
import com.copychrist.app.prayer.ui.components.MessageDialogFragment;
import com.copychrist.app.prayer.ui.contact.AddEditContactDialogFragment;
import com.copychrist.app.prayer.ui.contact.ContactDetailActivity;
import com.copychrist.app.prayer.ui.prayerrequest.EditPrayerRequestDetailActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ContactsActivity extends BaseActivity
        implements ContactsView, ContactsListAdapter.OnContactClickListener, DeleteDialogFragment.DeleteActionDialogListener {

    private static final String TAG = "ContactsActivity";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.tab_layout_groups) TabLayout tabLayoutGroups;

    @Inject ContactsPresenter contactsPresenter;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        initList();
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
                contactsPresenter.onAddNewContactGroupClick();
                return true;
            case R.id.action_edit_group:
                contactsPresenter.onEditContactGroupClick();
                return true;
            case R.id.action_delete_group:
                contactsPresenter.onDeleteContactGroupClick();
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
        return new ContactsModule(contactGroupId);
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
        contactsPresenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        contactsPresenter.clearView();
    }

    @Override
    public void showContacts(Cursor contacts) {
        contactsListAdapter.setCursor(contacts);
    }

    @Override
    public void showContactGroupsTabs(List<ContactGroup> contactGroups, ContactGroup selectedGroup) {
        tabLayoutGroups.clearOnTabSelectedListeners();
        tabLayoutGroups.removeAllTabs();
        for (ContactGroup contactGroup : contactGroups) {
            Tab tab = tabLayoutGroups.newTab();
            tab.setText(contactGroup.getName());
            tab.setTag(contactGroup.getId());
            tabLayoutGroups.addTab(tab);
            if(contactGroup.getId() == selectedGroup.getId()) {
                selectedTabIndex = tab.getPosition();
            }
        }
        tabLayoutGroups.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Timber.d(TAG, tab.getText() +":"+tab.getTag().toString());
                contactsPresenter.onContactGroupClicked(Integer.parseInt(tab.getTag().toString()));
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
        contactsPresenter.onDeleteContactGroupConfirmed();
    }

    @OnClick(R.id.fab)
    public void onAddNewContactClick() {
        contactsPresenter.onAddNewContactClick();
    }

    @Override
    public void showAddNewContactView(String groupName) {
        AddEditContactDialogFragment addEditContactDialogFragment = AddEditContactDialogFragment.newAddInstance(groupName, contactsPresenter);
        addEditContactDialogFragment.show(getSupportFragmentManager(), "AddContactDialogFragment");
    }

    @Override
    public void onContactClick(long id) {
        contactsPresenter.onContactClick(id);
    }


    @Override
    public void showContactDetailView(long id) {
        startActivity(ContactDetailActivity.getStartIntent(this, id));
    }

    @Override
    public void onPrayerRequestClick(long requestId) {
        contactsPresenter.onPrayerRequestClick(requestId);
    }

    @Override
    public void showPrayerRequestDetailView(long requestId) {
        startActivity(EditPrayerRequestDetailActivity.getStartIntent(this, requestId));
    }

    @Override
    public void showDBResultMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
