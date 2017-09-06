package com.copychrist.app.prayer.ui.contact;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.copychrist.app.prayer.data.AppDataSource;
import com.copychrist.app.prayer.data.AppRepository;
import com.copychrist.app.prayer.data.LoaderProvider;
import com.copychrist.app.prayer.data.model.Contact;

import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jim on 8/19/17.
 */

public class ContactPresenter implements ContactContract.Presenter,
        LoaderManager.LoaderCallbacks<Cursor>, AppRepository.ContactDetailActivityLoadDataCallback,
        AppDataSource.GetContactCallback {

    public final static int CONTACT_DETAIL_LOADER = 1;
    @NonNull
    private final AppRepository appRepository;

    @NonNull
    private final LoaderManager loaderManager;

    @NonNull
    private final LoaderProvider loaderProvider;

    private ContactFilter currentFilter = ContactFilter.from(ContactFilter.FilterType.REQUESTS);
    private final long contactId;
    private ContactContract.View view;
    private Contact contact;

    public ContactPresenter(@NonNull LoaderProvider loaderProvider,
                            @NonNull LoaderManager loaderManager,
                            @NonNull AppRepository repository,
                            @NonNull ContactContract.View view,
                            @NonNull long contactId) {
        this.loaderProvider = checkNotNull(loaderProvider, "loaderProvider provider cannot be null");
        this.loaderManager = checkNotNull(loaderManager, "loaderManager provider cannot be null");
        this.appRepository = checkNotNull(repository, "repository provider cannot be null");
        this.view = checkNotNull(view, "view cannot be null!");
        this.view.setPresenter(this);

        this.contactId = contactId;
    }

    @Override
    public void start() {
        loadContactDetail();
    }

    private static final String TAG = "ContactPresenter";
    private void loadContactDetail() {
//        view.setLoadingIndicator(true);
        Timber.d(TAG, "loadContactDetail() called");
        appRepository.getContact(contactId, this);
    }

    private void showActivePrayerRequests() {
        currentFilter = ContactFilter.from(ContactFilter.FilterType.REQUESTS);
        loadContactDetail();
    }

    private void showArchivedPrayerRequests() {
        currentFilter = ContactFilter.from(ContactFilter.FilterType.ARCHIVED);
        loadContactDetail();
    }

    @Override
    public void onPrayerRequestClick(long requestId) {
        view.showPrayerRequestDetailView(requestId);
    }

    @Override
    public void onAddNewRequestClick() {
        view.showAddNewPrayerRequestView(contactId);
    }

    @Override
    public void onContactEditClick(long contactId) {
        view.showContactDetailEditView(contactId);
    }

    @Override
    public void onEditClick(long contactId, String firstName, String lastName, String pictureUrl) {
        appRepository.saveContact(contact);
    }

    @Override
    public void onContactDeleteClick(long id) {
        view.showDeleteContactDialog(contact);
    }

    @Override
    public void onDeleteConfirm(long id) {
        appRepository.deleteContact(new Contact(id, -1, "", ""));
        view.finish();
    }

    @Override
    public void onActiveRequestsClick() {
        showActivePrayerRequests();
    }

    @Override
    public void onArchiveClick() {
        showArchivedPrayerRequests();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Timber.d(TAG, "onCreateLoader() called with: id = [" + id + "], args = [" + args + "]");
        return loaderProvider.createFilteredContactLoader(contactId, currentFilter);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Timber.d(TAG, "onLoadFinished() called with: loader = [" + loader + "], data = [" + data + "]");
        if (data != null) {
            if (data.moveToLast()) {
                onContactDetailDataLoaded(data);
            } else {
                onContactDetailDataEmpty();
            }
        } else {
            onContactDataNotAvailable();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        onContactDetailDataReset();
    }

    @Override
    public void onContactDetailDataLoaded(Cursor data) {
//        view.setLoadingIndicator(false);
        // Show contact detail
        Timber.d(TAG, "onContactDetailDataLoaded() called with: data = [" + data + "]");
        view.showContactDetail(data);
    }

    @Override
    public void onContactDetailDataEmpty() {
        Timber.d(TAG, "onContactDetailDataEmpty() called");
//        view.setLoadingIndicator(false);
        // Show a message indicating there are no tasks for that filter type.
        view.showContactDetail(null);
    }

    @Override
    public void onContactDetailDataNotAvailable() {
        Timber.d(TAG, "onContactDetailDataNotAvailable() called");
//        view.setLoadingIndicator(false);
//        view.showLoadingError();
    }

    @Override
    public void onContactDetailDataReset() {
        view.showContactDetail(null);
    }

    @Override
    public void onContactLoaded(Contact contact) {
        Timber.d(TAG, "onContactLoaded() called with: contact = [" + contact + "]");
        // we don't care about the result since the CursorLoader will load the data for us
        if (loaderManager.getLoader(CONTACT_DETAIL_LOADER) == null) {
            loaderManager.initLoader(CONTACT_DETAIL_LOADER, null, this);
        } else {
            loaderManager.restartLoader(CONTACT_DETAIL_LOADER, null, this);
        }
    }

    @Override
    public void onContactDataNotAvailable() {
        Timber.d(TAG, "onContactDataNotAvailable() called");
//        view.setLoadingIndicator(false);
//        view.showLoadingError();
    }
}
