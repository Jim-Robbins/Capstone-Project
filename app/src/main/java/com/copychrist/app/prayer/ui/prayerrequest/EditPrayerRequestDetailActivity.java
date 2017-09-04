package com.copychrist.app.prayer.ui.prayerrequest;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.BibleVerseRecyclerViewAdapter;
import com.copychrist.app.prayer.adapter.PrayerListsListAdapter;
import com.copychrist.app.prayer.data.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.components.DatePickerOnClickListener;
import com.copychrist.app.prayer.ui.contact.ContactDetailActivity;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPrayerRequestDetailActivity extends BaseActivity
        implements EditPrayerRequestView, BibleVerseRecyclerViewAdapter.OnBibleVerseClickListener {

    @BindView(R.id.add_layout_container) LinearLayout layoutContainer;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.edit_request_title) EditText editRequestTitle;
    @BindView(R.id.edit_request_desc) EditText editRequestDesc;
    @BindView(R.id.edit_passage) EditText editPassage;
    @BindView(R.id.edit_end_date) EditText editEndDate;
    @BindView(R.id.spinner_prayer_lists) Spinner spinnerPrayerLists;
    @BindView(R.id.text_contact_first_name)
    TextView txtFirstName;
    @BindView(R.id.text_contact_last_name)
    TextView txtLastName;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject EditPrayerRequestPresenter editPrayerRequestPresenter;

    public static String EXTRA_REQUEST_ID = "extra_request_id";
    private BibleVerseRecyclerViewAdapter bibleVerseAdapter;
    private long requestId = 0;
    private long contactId = 0;

    public static Intent getStartIntent(final Context context, final long requestId) {
        Intent intent = new Intent(context, EditPrayerRequestDetailActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prayer_request_detail);
        ButterKnife.bind(this);

        initToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_request_navigation_items, menu);
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
                editPrayerRequestPresenter.onScheduleReminderClick();
                return true;
            case R.id.action_archive:
                editPrayerRequestPresenter.onArchiveRequest();
                onNavUp();
                return true;
            case R.id.action_delete:
                editPrayerRequestPresenter.onDeleteRequest();
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
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void showBibleVerses(Cursor bibleVerses) {
        bibleVerseAdapter = new BibleVerseRecyclerViewAdapter(this);
        bibleVerseAdapter.setBibleVerseClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bibleVerseAdapter);
    }

    @Override
    protected Object getModule() {
        if (getIntent().hasExtra(EXTRA_REQUEST_ID)) {
            requestId = getIntent().getExtras().getInt(EXTRA_REQUEST_ID);
        }
        return new EditPrayerRequestModule(requestId);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        editPrayerRequestPresenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        editPrayerRequestPresenter.clearView();
    }

    @Override
    public void showEditPrayerRequestDetails(PrayerRequest prayerRequest, PrayerListsListAdapter prayerListsListAdapter) {
        spinnerPrayerLists.setAdapter(prayerListsListAdapter);
        // If wa already have a contact, hide the contact entry view
        if(prayerRequest != null) {
            //Todo: Reenable these
//            contactId = prayerRequest.getContact().getId();
//            txtFirstName.setText(prayerRequest.getContact().getFirstName());
//           txtLastName.setText(prayerRequest.getContact().getLastName());
            editRequestTitle.setText(prayerRequest.getTitle());
            editRequestDesc.setText(prayerRequest.getDescription());
            if(prayerRequest.getVerses().size() > 0) {
//                editPassage.setText(prayerRequest.getVerses().first().getPassage());
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format));
            if(prayerRequest.getEndDate() != null)
                editEndDate.setText(dateFormat.format(prayerRequest.getEndDate()).toString());

            editEndDate.setFocusable(false);
            editEndDate.setOnClickListener(new DatePickerOnClickListener(dateFormat));
        }
    }

    @Override
    public void showEditPrayerRequestError() {
        Snackbar.make(layoutContainer, R.string.edit_new_prayer_request_error, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.action_save)
    public void onSaveClick() {
        PrayerRequest prayerRequest = editPrayerRequestPresenter.onUpdateClick(
                editRequestTitle.getText().toString(),
                editRequestDesc.getText().toString(),
                editPassage.getText().toString(),
                editEndDate.getText().toString(),
                null
                //spinnerPrayerLists.getSelectedItem().toString()
        );
        onNavUp();
    }

    @OnClick(R.id.action_cancel)
    public void onCancelClick() {
        onNavUp();
    }

    @Override
    public void showBibleVerseDetails(String bibleVerse) {
        //Todo: hook up Bible verse editor
    }

    @Override
    public void onBibleVerseClick(String biblePassage) {
        editPrayerRequestPresenter.onBibleVerseItemlick(biblePassage);
    }


    private void onNavUp() {
        startActivity(ContactDetailActivity.getStartIntent(this, contactId));
        finish();
    }
}
