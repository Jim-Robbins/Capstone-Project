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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jim on 8/19/17.
 */

public class ContactPresenter implements ContactContract.Presenter,
        LoaderManager.LoaderCallbacks<Cursor>, AppRepository.ContactDetailActivityLoadDataCallback,
        AppDataSource.GetContactCallback
        {

    @NonNull
    private final AppRepository appRepository;

    @NonNull
    private final LoaderManager loaderManager;

    @NonNull
    private final LoaderProvider loaderProvider;

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

    private void loadContactDetail() {
//        view.setLoadingIndicator(true);
        appRepository.getContact(contactId, this);
    }

    private void showActivePrayerRequests() {
//            view.showPrayerRequests(appDataSource.getActiveRequestsByContact(contact));
    }

    private void showArchivedPrayerRequests() {
//            view.showPrayerRequests(appDataSource.getArchivedRequestsByContact(contact));
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
//        appDataSource.editContact(contactId, firstName, lastName, pictureUrl, contact.getGroup().getName(), this);
    }

    @Override
    public void onContactDeleteClick(long id) {
        view.showDeleteContactDialog(contact);
    }

    @Override
    public void onDeleteConfirm(long id) {
//        appDataSource.deleteContact(id);
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
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onContactDetailDataLoaded(Cursor data) {

    }

    @Override
    public void onContactDetailataEmpty() {

    }

    @Override
    public void onContactDetailDataNotAvailable() {

    }

    @Override
    public void onContactDetailDataReset() {

    }

    @Override
    public void onContactLoaded(Contact contact) {

    }

    @Override
    public void onContactDataNotAvailable() {

    }
}
