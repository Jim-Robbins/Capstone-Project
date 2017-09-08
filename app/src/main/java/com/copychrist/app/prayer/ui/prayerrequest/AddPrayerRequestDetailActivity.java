package com.copychrist.app.prayer.ui.prayerrequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.PrayerListsListAdapter;
import com.copychrist.app.prayer.model.BibleVerse;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.components.DatePickerOnClickListener;
import com.copychrist.app.prayer.ui.contact.ContactDetailActivity;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class AddPrayerRequestDetailActivity extends BaseActivity implements PrayerRequestContract.View {

    @BindView(R.id.add_layout_container) LinearLayout layoutContainer;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.edit_request_title) EditText editRequestTitle;
    @BindView(R.id.edit_request_desc) EditText editRequestDesc;
    @BindView(R.id.edit_passage) EditText editPassage;
    @BindView(R.id.edit_end_date) EditText editEndDate;
    @BindView(R.id.spinner_prayer_lists) Spinner spinnerPrayerLists;

    @Inject PrayerRequestContract.AddPresenter addPrayerRequestPresenter;

    public static String EXTRA_CONTACT_ID = "extra_contact_id";
    private String contactId;

    public static Intent getStartIntent(final Context context, final String contactId) {
        Intent intent = new Intent(context, AddPrayerRequestDetailActivity.class);
        intent.putExtra(EXTRA_CONTACT_ID, contactId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prayer_request_detail);
        ButterKnife.bind(this);

        initToolbar();
    }

    private void initToolbar() {
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("cek", "home selected");
                onNavUp();
            }
        });
    }

    @Override
    protected Object getModule() {
        if (getIntent().hasExtra(EXTRA_CONTACT_ID)) {
            contactId = getIntent().getExtras().getString(EXTRA_CONTACT_ID);
        }
        return new PrayerRequestModule(contactId);
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
    public void showAddPrayerRequestDetails(Contact contact, PrayerListsListAdapter prayerListsListAdapter) {
        spinnerPrayerLists.setAdapter(prayerListsListAdapter);
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format));
        editEndDate.setFocusable(false);
        editEndDate.setOnClickListener(new DatePickerOnClickListener(dateFormat));
    }

    @Override
    public void showPrayerRequestError() {
        Snackbar.make(layoutContainer, R.string.add_new_prayer_request_error, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.action_save)
    public void onSaveClick() {
        addPrayerRequestPresenter.onSaveNewClick(
                editRequestTitle.getText().toString(),
                editRequestDesc.getText().toString(),
                editPassage.getText().toString(),
                editEndDate.getText().toString(),
                null
                //spinnerPrayerLists.getSelectedItem().toString()
        );
    }

    @OnClick(R.id.action_cancel)
    public void onCancelClick() {
        onNavUp();
    }

    private void onNavUp() {
        startActivity(ContactDetailActivity.getStartIntent(this, contactId));
        finish();
    }

    @Override
    public void showEditPrayerRequestDetails(PrayerRequest request, PrayerListsListAdapter prayerListsListAdapter) {

    }

    @Override
    public void showBibleVerses(List<BibleVerse> bibleVerses) {

    }

    @Override
    public void showBibleVerseDetails(String bibleVerse) {

    }
}
