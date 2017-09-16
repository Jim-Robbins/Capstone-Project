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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.BibleVerseRecyclerViewAdapter;
import com.copychrist.app.prayer.adapter.PrayerListsListAdapter;
import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.ViewMode;
import com.copychrist.app.prayer.ui.biblepassages.BiblePassageFinderDialogFragment;
import com.copychrist.app.prayer.ui.biblepassages.ViewBiblePassagesDialogFragment;
import com.copychrist.app.prayer.ui.components.DatePickerOnClickListener;
import com.copychrist.app.prayer.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrayerRequestDetailActivity extends BaseActivity implements PrayerRequestContract.View,
        BibleVerseRecyclerViewAdapter.BiblePassageAdapterListener {

    public static String EXTRA_LIST_KEY = "extra_list_key";
    public static String EXTRA_CONTACT = "extra_contact";
    public static String EXTRA_REQUEST = "extra_request";

    @BindView(R.id.add_layout_container) LinearLayout layoutContainer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.edit_request_title) EditText editRequestTitle;
    @BindView(R.id.edit_request_desc) EditText editRequestDesc;
    @BindView(R.id.edit_passage) EditText editPassage;
    @BindView(R.id.edit_end_date) EditText editEndDate;
    @BindView(R.id.spinner_prayer_lists) Spinner spinnerPrayerLists;
    @BindView(R.id.text_contact_first_name) TextView txtFirstName;
    @BindView(R.id.text_contact_last_name) TextView txtLastName;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_bible_passage_finder) ImageButton btnBiblePassageFinder;

    @Inject
    PrayerRequestContract.Presenter addPrayerRequestPresenter;

    BibleVerseRecyclerViewAdapter bibleVerseAdapter;

    private Contact contact;
    private PrayerRequest selectedPrayerRequest;
    private String prayerListKey;
    private ViewBiblePassagesDialogFragment viewBiblePassagesDialogFragment;
    private ViewMode viewMode;

    public static Intent getStartAddIntent(final Context context, final Contact contact) {
        Intent intent = new Intent(context, PrayerRequestDetailActivity.class);
        intent.putExtra(EXTRA_CONTACT, contact);
        return intent;
    }

    public static Intent getStartEditIntent(final Context context, final PrayerRequest prayerRequest) {
        Intent intent = new Intent(context, PrayerRequestDetailActivity.class);
        intent.putExtra(EXTRA_REQUEST, prayerRequest);
        return intent;
    }

    public static Intent getStartAddIntent(final Context context, final String prayerListKey) {
        Intent intent = new Intent(context, PrayerRequestDetailActivity.class);
        intent.putExtra(EXTRA_LIST_KEY, prayerListKey);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_request_detail);
        ButterKnife.bind(this);
        viewMode = new ViewMode(ViewMode.FULL_ADD_MODE);
        initToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(viewMode.equals(ViewMode.EDIT_MODE)) {
            getMenuInflater().inflate(R.menu.edit_request_navigation_items, menu);
        } else if(viewMode.equals(ViewMode.ARCHIVE_MODE)) {
            getMenuInflater().inflate(R.menu.edit_request_navigation_archive_items, menu);
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
            case R.id.action_unarchive:
                addPrayerRequestPresenter.onPrayerRequestUnarchive();
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
        if (getIntent().hasExtra(EXTRA_CONTACT)) {
            viewMode = new ViewMode(ViewMode.ADD_MODE);
            contact = getIntent().getParcelableExtra(EXTRA_CONTACT);
        } else if (getIntent().hasExtra(EXTRA_REQUEST)) {
            selectedPrayerRequest = getIntent().getParcelableExtra(EXTRA_REQUEST);
            if(selectedPrayerRequest.getAnswered() == null) {
                viewMode = new ViewMode(ViewMode.EDIT_MODE);
            } else {
                viewMode = new ViewMode(ViewMode.ARCHIVE_MODE);
            }
        } else if (getIntent().hasExtra(EXTRA_LIST_KEY)) {
            viewMode = new ViewMode(ViewMode.FULL_ADD_MODE);
            prayerListKey = getIntent().getExtras().getString(EXTRA_LIST_KEY);
        }

        return new PrayerRequestModule(contact, selectedPrayerRequest, prayerListKey);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addPrayerRequestPresenter.setView(this);
        if (viewMode.equals(ViewMode.ADD_MODE)) {
            showContactDetail(contact);
        } if (viewMode.equals(ViewMode.FULL_ADD_MODE)) {
            //Todo: Show contact selector
        } else {
            showPrayerRequestDetails(selectedPrayerRequest, null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        addPrayerRequestPresenter.clearView();
    }

    @Override
    public void showContactDetail(Contact selectedContact) {
        contact = selectedContact;
        txtFirstName.setText(selectedContact.getFirstName());
        txtLastName.setText(selectedContact.getLastName());
    }

    @Override
    public void showContactSelector() {

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
            selectedPrayerRequest.setContact(contact);
            selectedPrayerRequest.setContactKey(contact.getKey());
        }

        String passagesEntry = editPassage.getText().toString();
        if (!TextUtils.isEmpty(passagesEntry)) {
            String[] passages = passagesEntry.split("(\\s*,\\s*)(\\s*|\\s*)");
            selectedPrayerRequest.setPassages(Arrays.asList(passages));
        }

        String endDate = editEndDate.getText().toString();
        if (!TextUtils.isEmpty(endDate)) {
            Date date = Utils.stringToDate(endDate, getBaseContext());
            selectedPrayerRequest.setEndDate(date.getTime());
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
        finish();
    }

    @Override
    public void showPrayerRequestDetails(PrayerRequest prayerRequest, PrayerListsListAdapter prayerListsListAdapter) {
        SimpleDateFormat dateFormat = Utils.getDateFormat(this);

        spinnerPrayerLists.setAdapter(prayerListsListAdapter);
        selectedPrayerRequest = prayerRequest;

        if(selectedPrayerRequest != null) {

            contact = selectedPrayerRequest.getContact();
            contact.setKey(selectedPrayerRequest.getContactKey());
            showContactDetail(contact);

            editRequestTitle.setText(selectedPrayerRequest.getTitle());
            editRequestDesc.setText(selectedPrayerRequest.getDescription());

            if(selectedPrayerRequest.getPassages() != null && selectedPrayerRequest.getPassages().size() > 0) {
                String passageList = TextUtils.join(", ", selectedPrayerRequest.getPassages());
                editPassage.setText(passageList);
            }
            if(selectedPrayerRequest.getEndDate() != null)
                editEndDate.setText(dateFormat.format(selectedPrayerRequest.getEndDate()));
        }
        editEndDate.setFocusable(false);
        editEndDate.setOnClickListener(new DatePickerOnClickListener(dateFormat));
    }

    @Override
    public void showBiblePassages(final List<BiblePassage> listResults, final List<BiblePassage> nonListResults) {
        bibleVerseAdapter = new BibleVerseRecyclerViewAdapter(this, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bibleVerseAdapter);

        if (viewBiblePassagesDialogFragment != null) {
            viewBiblePassagesDialogFragment.setAdapter(nonListResults);
        }

        btnBiblePassageFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewBiblePassagesDialogFragment = ViewBiblePassagesDialogFragment.newInstance(addPrayerRequestPresenter, nonListResults);
                viewBiblePassagesDialogFragment.show(getSupportFragmentManager(), "ViewBiblePassagesDialogFragment");
            }
        });
    }

    @Override
    public void showBiblePassageAddDialog() {
        BiblePassageFinderDialogFragment biblePassageFinderDialogFragment = BiblePassageFinderDialogFragment.newInstance(addPrayerRequestPresenter);
        biblePassageFinderDialogFragment.show(getSupportFragmentManager(), "BiblePassageFinderDialogFragment");
    }



    @Override
    public void onRowClicked(int position) {

    }

    @Override
    public void onRowLongClicked(int position) {

    }
}