package com.copychrist.app.prayer.ui.prayerlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.PrayerListRequestAdapter;
import com.copychrist.app.prayer.model.PrayerList;
import com.copychrist.app.prayer.model.PrayerListRequest;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.components.DeleteDialogFragment;
import com.copychrist.app.prayer.ui.components.MessageDialogFragment;
import com.copychrist.app.prayer.ui.contactgroups.ContactGroupActivity;
import com.copychrist.app.prayer.ui.prayerrequest.PrayerRequestDetailActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class PrayerListActivity extends BaseActivity implements PrayerListContract.View,
        PrayerListRequestAdapter.PrayerListRequestAdapterListener,
        DeleteDialogFragment.DeleteActionDialogListener {

    private static final String TAG = "PrayerListActivity";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.tab_layout_prayer_list) TabLayout tabLayoutPrayerList;
    @Inject PrayerListContract.Presenter prayerListPresenter;

    private PrayerListRequestAdapter prayerRequestsListAdapter;
    private int selectedTabIndex = 0;
    private PrayerList selectedPrayerList;
    public static String EXTRA_PRAYER_LIST = "extra_prayer_list";

    private  List<PrayerListRequest> prayerListRequests;

    public static Intent getStartIntent(final Context context, final PrayerList prayerList) {
        Intent intent = new Intent(context, PrayerRequestDetailActivity.class);
        intent.putExtra(EXTRA_PRAYER_LIST, prayerList);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_list);
        ButterKnife.bind(this);
        initPrayerRequestList();
        initToolbar();
    }

    private void initToolbar() {
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_prayer_list_navigation_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                //Todo: don't leave this
                startActivity(ContactGroupActivity.getStartIntent(this, null));
                return true;
            case R.id.action_add_list:
                prayerListPresenter.onPrayerListAddClick();
                return true;
            case R.id.action_edit_list:
                prayerListPresenter.onPrayerListEditClick();
                return true;
            case R.id.action_delete_list:
                prayerListPresenter.onPrayerListDeleteClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Object getModule() {
        if(getIntent().hasExtra(EXTRA_PRAYER_LIST)) {
            selectedPrayerList = getIntent().getParcelableExtra(EXTRA_PRAYER_LIST);
        }
        return new PrayerListModule(selectedPrayerList);
    }

    private void initPrayerRequestList() {
        prayerRequestsListAdapter = new PrayerListRequestAdapter(this, this);

        GridLayoutManager layoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.contacts_grid_column));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(prayerRequestsListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        prayerListPresenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        prayerListPresenter.clearView();
    }

    @Override
    public void showPrayerListRequests(List<PrayerListRequest> prayerListRequests) {
        this.prayerListRequests = prayerListRequests;
        prayerRequestsListAdapter.setAdpaterData(prayerListRequests);
    }

    @Override
    public void showPrayerListTabs(List<PrayerList> prayerLists, final PrayerList selectedGroup) {
        tabLayoutPrayerList.clearOnTabSelectedListeners();
        tabLayoutPrayerList.removeAllTabs();
        for (PrayerList prayerList : prayerLists) {
            TabLayout.Tab tab = tabLayoutPrayerList.newTab();
            tab.setText(prayerList.getName());
            tab.setTag(prayerList);
            tabLayoutPrayerList.addTab(tab);
            if(selectedGroup != null && prayerList.getKey().equalsIgnoreCase(selectedGroup.getKey())) {
                selectedTabIndex = tab.getPosition();
                prayerListPresenter.onPrayerListClick(selectedGroup);
            }
        }
        tabLayoutPrayerList.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag() != null) {
                    Timber.d(TAG, tab.getText() + ":" + tab.getTag().toString());
                    prayerListPresenter.onPrayerListClick((PrayerList) tab.getTag());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        TabLayout.Tab selectedTab = tabLayoutPrayerList.getTabAt(selectedTabIndex);
        if(selectedTab != null) {
            selectedTab.select();
        }
    }


    @Override
    public void showPrayerListDialogAdd() {
        AddEditPrayerListDialogFragment addPrayerListDialogFragment =
                AddEditPrayerListDialogFragment.newInstance(null, prayerListPresenter, tabLayoutPrayerList.getTabCount());
        addPrayerListDialogFragment.show(getSupportFragmentManager(), "AddPrayerListDialog");
    }

    @Override
    public void showPrayerListDialogEdit(PrayerList prayerList) {
        AddEditPrayerListDialogFragment addPrayerListDialogFragment =
                AddEditPrayerListDialogFragment.newInstance(prayerList, prayerListPresenter, tabLayoutPrayerList.getTabCount());
        addPrayerListDialogFragment.show(getSupportFragmentManager(), "EditPrayerListDialog");
    }

    @Override
    public void showPrayerListDialogDelete(PrayerList prayerList) {
        if (tabLayoutPrayerList.getTabCount() > 1) {
            DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.newInstance(
                    getString(R.string.dialog_delete_group_title),
                    prayerList.getName()
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
        prayerListPresenter.onPrayerListDeleteConfirmed();
    }

    @OnClick(R.id.fab_add_request)
    public void onAddNewPrayerRequestClick() {
        prayerListPresenter.onPrayerRequestAddClick();
    }

    @Override
    public void showPrayerRequestAddDialog(PrayerList prayerListKey) {
        AddPrayerRequestDialogFragment addPrayerRequestDialogFragment = AddPrayerRequestDialogFragment.newAddInstance(prayerListKey, prayerListPresenter);
        addPrayerRequestDialogFragment.show(getSupportFragmentManager(), "AddContactDialogFragment");
    }

    @Override
    public void onIconClicked(int position) {
        toggleSelection(position);
    }

    @Override
    public void onRowClicked(int position) {
        // read the message which removes bold from the row
        PrayerListRequest prayerListRequest = prayerListRequests.get(position);
        prayerListRequest.setPrayedFor(true);
        prayerListRequests.set(position, prayerListRequest);
        prayerRequestsListAdapter.notifyDataSetChanged();

        Toast.makeText(getApplicationContext(), "Read: " + prayerListRequest.getDesc(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRowLongClicked(int position) {
        PrayerListRequest prayerListRequest = prayerListRequests.get(position);
        startActivity(PrayerRequestDetailActivity.getStartEditIntent(this, prayerListRequest.getPrayerRequest()));
    }

    @Override
    public void showDatabaseResultMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDatabaseResultMessage(int messageResId) {
        Toast.makeText(this, getBaseContext().getString(messageResId), Toast.LENGTH_LONG).show();
    }

    private void toggleSelection(int position) {
        prayerRequestsListAdapter.toggleSelection(position);
    }
}