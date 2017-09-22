package com.copychrist.app.prayer.ui.prayerrequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.BiblePassageFinderSelectableAdapter;
import com.copychrist.app.prayer.adapter.BiblePassageSwipableAdapter;
import com.copychrist.app.prayer.adapter.ContactsSpinnerPrayerRequestArrayAdapter;
import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.ViewMode;
import com.copychrist.app.prayer.ui.biblepassages.BiblePassageFinderDialogFragment;
import com.copychrist.app.prayer.ui.biblepassages.ViewBiblePassagesDialogFragment;
import com.copychrist.app.prayer.ui.components.DatePickerOnClickListener;
import com.copychrist.app.prayer.ui.components.DeleteDialogFragment;
import com.copychrist.app.prayer.ui.components.MessageDialogFragment;
import com.copychrist.app.prayer.ui.contactgroups.ContactGroupActivity;
import com.copychrist.app.prayer.ui.home.MainActivity;
import com.copychrist.app.prayer.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrayerRequestDetailActivity extends BaseActivity implements PrayerRequestContract.View,
        BiblePassageFinderSelectableAdapter.BiblePassageAdapterListener,
        BiblePassageSwipableAdapter.BiblePassageAdapterListener,
        BiblePassageFinderDialogFragment.BiblePassageFinderDialogListener,
        ViewBiblePassagesDialogFragment.ViewBiblePassagesDialogListener,
        MessageDialogFragment.MessageActionDialogListener {

    public static String EXTRA_LIST_KEY = "extra_list_key";
    public static String EXTRA_CONTACT = "extra_contact";
    public static String EXTRA_REQUEST = "extra_request";

    @BindView(R.id.add_layout_container)
    protected LinearLayout layoutContainer;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.edit_request_title)
    protected EditText editRequestTitle;
    @BindView(R.id.edit_request_desc)
    protected EditText editRequestDesc;
    @BindView(R.id.edit_end_date)
    protected EditText editEndDate;
    @BindView(R.id.txt_contact_first_name)
    protected TextView txtFirstName;
    @BindView(R.id.txt_contact_last_name)
    protected TextView txtLastName;
    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;
    @BindView(R.id.btn_bible_passage_finder)
    protected ImageView btnBiblePassageFinder;
    @BindView(R.id.contact_container)
    protected LinearLayout layoutContactContainer;
    @BindView(R.id.spinner_contacts)
    protected Spinner spinnerContacts;
    @BindView(R.id.profile_icon)
    protected ImageView toolbarIcon;

    @Inject
    protected PrayerRequestContract.Presenter addPrayerRequestPresenter;

    private BiblePassageSwipableAdapter bibleVerseAdapter;
    private SimpleDateFormat dateFormat;

    private Contact contact;
    private PrayerRequest selectedPrayerRequest;
    private String prayerListKey;
    private ViewBiblePassagesDialogFragment viewBiblePassagesDialogFragment;
    private ViewMode viewMode;
    private List<BiblePassage> selectedVerses;

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

    public static Intent getStartAddIntent(final Context context) {
        return new Intent(context, PrayerRequestDetailActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_request_detail);
        ButterKnife.bind(this);
        initToolbar();
        initAdapter();
        dateFormat = Utils.getDateFormat(this);
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

    private void initToolbar() {
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(viewMode.equals(ViewMode.EDIT_MODE)) {
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            toolbarIcon.setVisibility(View.GONE);
            this.getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_archive:
                addPrayerRequestPresenter.onPrayerRequestArchive();
                finish();
                return true;
            case R.id.action_unarchive:
                addPrayerRequestPresenter.onPrayerRequestUnarchive();
                finish();
                return true;
            case R.id.action_delete:
                addPrayerRequestPresenter.onPrayerRequestDelete();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initAdapter() {
        bibleVerseAdapter = new BiblePassageSwipableAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(bibleVerseAdapter);
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
        } else {
            viewMode = new ViewMode(ViewMode.FULL_ADD_MODE);
        }

        return new PrayerRequestModule(contact, selectedPrayerRequest, prayerListKey);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addPrayerRequestPresenter.setView(this);
        if (viewMode.equals(ViewMode.ADD_MODE)) {
            showContactDetail(contact);
        } if (viewMode.equals(ViewMode.EDIT_MODE)) {
            showPrayerRequestDetails(selectedPrayerRequest);
        }
        showDatePicker();
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
    public void showContactSelector(List<Contact> contacts) {
        if(contacts.isEmpty()) {
            showNeedContactsDialog();
        }

        layoutContactContainer.setVisibility(View.VISIBLE);

        final int layoutRes = R.layout.item_spinner;
        final int textViewRes = R.id.text_spinner_item;

        ContactsSpinnerPrayerRequestArrayAdapter contactsAdapter = new ContactsSpinnerPrayerRequestArrayAdapter(this, layoutRes, textViewRes, contacts);
        spinnerContacts.setAdapter(contactsAdapter);
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
        if (!validateForm()) {
            return;
        }
        if(selectedPrayerRequest == null) {
            selectedPrayerRequest = new PrayerRequest();
        }

        if (viewMode.equals(ViewMode.FULL_ADD_MODE)) {
            if (prayerListKey != null) selectedPrayerRequest.getPrayerLists().put(prayerListKey, true);
            contact = (Contact) spinnerContacts.getSelectedItem();
        }

        if (viewMode.equals(ViewMode.ADD_MODE) || viewMode.equals(ViewMode.FULL_ADD_MODE)) {
            selectedPrayerRequest.setContact(contact);
            selectedPrayerRequest.setContactKey(contact.getKey());
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

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(editRequestTitle.getText().toString())) {
            editRequestTitle.setError(getString(R.string.form_error));
            result = false;
        }
        return result;
    }

    @OnClick(R.id.action_cancel)
    public void onCancelClick() {
        onNavUp();
    }

    private void onNavUp() {
        finish();
    }

    @Override
    public void showPrayerRequestDetails(PrayerRequest prayerRequest) {
        selectedPrayerRequest = prayerRequest;

        if(selectedPrayerRequest != null) {
            contact = selectedPrayerRequest.getContact();
            contact.setKey(selectedPrayerRequest.getContactKey());
            showContactDetail(contact);

            editRequestTitle.setText(selectedPrayerRequest.getTitle());
            editRequestDesc.setText(selectedPrayerRequest.getDescription());

            if(selectedPrayerRequest.getEndDate() != null)
                editEndDate.setText(dateFormat.format(selectedPrayerRequest.getEndDate()));
        }
    }

    private void showDatePicker() {
        editEndDate.setFocusable(false);
        editEndDate.setOnClickListener(new DatePickerOnClickListener(dateFormat));
    }
    @Override
    public void showBiblePassages(final List<BiblePassage> listResults, final List<BiblePassage> nonListResults) {
        setupBiblePassagesRecycler(listResults);
        setupBiblePassagesFinder(nonListResults);
    }

    private void setupBiblePassagesRecycler(List<BiblePassage> listResults) {
        selectedVerses = listResults;
        bibleVerseAdapter.setAdapterData(listResults);
    }

    private void setupBiblePassagesFinder(final List<BiblePassage> nonListResults) {
        if (viewBiblePassagesDialogFragment != null) {
            viewBiblePassagesDialogFragment.setAdapter(nonListResults);
        }

        btnBiblePassageFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewBiblePassagesDialogFragment = ViewBiblePassagesDialogFragment.newInstance(nonListResults);
                viewBiblePassagesDialogFragment.show(getSupportFragmentManager(), "ViewBiblePassagesDialogFragment");
            }
        });
    }

    @Override
    public void showBiblePassageAddDialog() {
        BiblePassageFinderDialogFragment biblePassageFinderDialogFragment = BiblePassageFinderDialogFragment.newInstance();
        biblePassageFinderDialogFragment.show(getSupportFragmentManager(), "BiblePassageFinderDialogFragment");
    }

    @Override
    public void onBiblePassageSave(BiblePassage biblePassage) {
        logEvent("Save Bible Passage", biblePassage.getPassageReference(), null);
        addPrayerRequestPresenter.onBiblePassageSave(biblePassage);
    }

    @Override
    public void onRowClicked(int position) {

    }

    @Override
    public void onRowLongClicked(int position) {

    }

    @Override
    public void onRemoveClicked(int position) {
        logEvent("Delete Request", null, "swipe");
        addPrayerRequestPresenter.onBiblePassageRemove(selectedVerses.get(position));
    }

    @Override
    public void onBiblePassagesAddedToPrayerRequest(List<String> selectedPassageKeys) {
        addPrayerRequestPresenter.onBiblePassagesAddedToPrayerRequest(selectedPassageKeys);
    }

    @Override
    public void onBiblePassageAddNew() {
        logEvent("Add Verses", null, "button");
        addPrayerRequestPresenter.onBiblePassageAddNew();
    }

    private void showNeedContactsDialog() {
        MessageDialogFragment dialogFragment = MessageDialogFragment.newInstance(
                getString(R.string.dialog_need_to_add_contacts_first_title),
                getString(R.string.dialog_need_to_add_contacts_first)
        );
        dialogFragment.show(getSupportFragmentManager(), "MessageDialogFragment");
    }

    @Override
    public void onDialogConfirmClicked() {
        startActivity(ContactGroupActivity.getStartIntent(this, null));
        finish();
    }
}