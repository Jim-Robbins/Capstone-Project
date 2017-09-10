package com.copychrist.app.prayer.ui.prayerrequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.BibleVerseRecyclerViewAdapter;
import com.copychrist.app.prayer.adapter.PrayerListsListAdapter;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.ViewMode;
import com.copychrist.app.prayer.ui.components.DatePickerOnClickListener;
import com.copychrist.app.prayer.ui.contact.ContactDetailActivity;
import com.copychrist.app.prayer.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrayerRequestDetailActivity extends BaseActivity implements
         PrayerRequestContract.View, BibleVerseRecyclerViewAdapter.OnBibleVerseClickListener {

    public static String EXTRA_CONTACT_KEY = "extra_contact_key";
    public static String EXTRA_REQUEST_KEY = "extra_request_key";

    @BindView(R.id.add_layout_container) LinearLayout layoutContainer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.edit_request_title) EditText editRequestTitle;
    @BindView(R.id.edit_request_desc) EditText editRequestDesc;
    @BindView(R.id.edit_passage) EditText editPassage;
    @BindView(R.id.edit_end_date) EditText editEndDate;
    @BindView(R.id.spinner_prayer_lists) Spinner spinnerPrayerLists;
    @BindView(R.id.text_contact_first_name) TextView txtFirstName;
    @BindView(R.id.text_contact_last_name) TextView txtLastName;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Inject
    PrayerRequestContract.Presenter addPrayerRequestPresenter;

    BibleVerseRecyclerViewAdapter bibleVerseAdapter;

    private String contactKey;
    private String prayerRequestKey;
    private PrayerRequest selectedPrayerRequest;

    private ViewMode viewMode;

    public static Intent getStartAddIntent(final Context context, final String contactKey) {
        Intent intent = new Intent(context, PrayerRequestDetailActivity.class);
        intent.putExtra(EXTRA_CONTACT_KEY, contactKey);
        return intent;
    }

    public static Intent getStartEditIntent(final Context context, final String prayerRequestKey) {
        Intent intent = new Intent(context, PrayerRequestDetailActivity.class);
        intent.putExtra(EXTRA_REQUEST_KEY, prayerRequestKey);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_request_detail);
        ButterKnife.bind(this);

        initToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(viewMode.equals(ViewMode.EDIT_MODE)) {
            getMenuInflater().inflate(R.menu.edit_request_navigation_items, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onNavUp();
                return true;
            case R.id.action_schedule:
                addPrayerRequestPresenter.onPrayerRequestScheduleReminderClick();
                return true;
            case R.id.action_archive:
                addPrayerRequestPresenter.onPrayerRequestArchive();
                onNavUp();
                return true;
            case R.id.action_delete:
                addPrayerRequestPresenter.onPrayerRequestDelete();
                onNavUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initToolbar() {
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(viewMode.equals(ViewMode.EDIT_MODE)) {
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected Object getModule() {
        String key;
        if (getIntent().hasExtra(EXTRA_CONTACT_KEY)) {
            viewMode = new ViewMode(ViewMode.ADD_MODE);
            key = contactKey = getIntent().getExtras().getString(EXTRA_CONTACT_KEY);
        } else if (getIntent().hasExtra(EXTRA_REQUEST_KEY)) {
            viewMode = new ViewMode(ViewMode.EDIT_MODE);
            key = prayerRequestKey = getIntent().getExtras().getString(EXTRA_REQUEST_KEY);
        } else {
            onNavUp();
            return null;
        }
        return new PrayerRequestModule(key, viewMode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addPrayerRequestPresenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        addPrayerRequestPresenter.clearView();
    }

    @Override
    public void showContactDetail(Contact selectedContact) {
        contactKey = selectedContact.getKey();
        txtFirstName.setText(selectedContact.getFirstName());
        txtLastName.setText(selectedContact.getLastName());
    }

    @Override
    public void showDatabaseResultMessage(String message) {
        Snackbar.make(layoutContainer, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showDatabaseResultMessage(int messageResId) {
        Toast.makeText(this, getBaseContext().getString(messageResId), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.action_save)
    public void onSaveClick() {
        if (selectedPrayerRequest == null) {
            selectedPrayerRequest = new PrayerRequest();
            selectedPrayerRequest.setContactKey(contactKey);
        } else {
            selectedPrayerRequest.setKey(prayerRequestKey);
        }

        String passagesEntry = editPassage.getText().toString();
        if (!TextUtils.isEmpty(passagesEntry)) {
            String[] passages = passagesEntry.split("(\\s*,\\s*)(\\s*|\\s*)");
            selectedPrayerRequest.addPassages(Arrays.asList(passages));
        }

        String endDate = editEndDate.getText().toString();
        if (!TextUtils.isEmpty(endDate)) {
            Date date = Utils.stringToDate(endDate, getBaseContext());
            selectedPrayerRequest.setEndDate(date);
        }

        selectedPrayerRequest.setTitle(editRequestTitle.getText().toString());
        selectedPrayerRequest.setDescription(editRequestDesc.getText().toString());

        addPrayerRequestPresenter.onPrayerRequestSaveClick(selectedPrayerRequest);

        onNavUp();
    }

    @OnClick(R.id.action_cancel)
    public void onCancelClick() {
        onNavUp();
    }

    private void onNavUp() {
        startActivity(ContactDetailActivity.getStartIntent(this, contactKey));
        finish();
    }

    @Override
    public void showPrayerRequestDetails(PrayerRequest prayerRequest, PrayerListsListAdapter prayerListsListAdapter) {
        spinnerPrayerLists.setAdapter(prayerListsListAdapter);
        // If wa already have a contact, hide the contact entry view
        if(prayerRequest != null) {
            selectedPrayerRequest = prayerRequest;
            contactKey = prayerRequest.getContactKey();
            editRequestTitle.setText(prayerRequest.getTitle());
            editRequestDesc.setText(prayerRequest.getDescription());

            if(prayerRequest.getPassages() != null && prayerRequest.getPassages().size() > 0) {
                String passageList = TextUtils.join(", ", prayerRequest.getPassages());
                editPassage.setText(passageList);
            }
            SimpleDateFormat dateFormat = Utils.getDateFormat(this);
            if(prayerRequest.getEndDate() != null)
                editEndDate.setText(dateFormat.format(prayerRequest.getEndDate()));

            editEndDate.setFocusable(false);
            editEndDate.setOnClickListener(new DatePickerOnClickListener(dateFormat));
        }
    }

    @Override
    public void showBibleVerses(List<String> biblePassages) {
        bibleVerseAdapter = new BibleVerseRecyclerViewAdapter();
        bibleVerseAdapter.setBibleVerseClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bibleVerseAdapter);
    }

    @Override
    public void showBibleVerseDetails(String bibleVerse) {
        //Todo: hook up Bible verse editor
    }

    @Override
    public void onBibleVerseClick(String biblePassage) {
        addPrayerRequestPresenter.onBibleVerseItemClick(biblePassage);
    }
}