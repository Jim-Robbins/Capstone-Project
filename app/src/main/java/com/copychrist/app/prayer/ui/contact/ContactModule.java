package com.copychrist.app.prayer.ui.contact;

import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.data.AppRepository;
import com.copychrist.app.prayer.data.LoaderProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/19/17.
 */
@Module(injects = ContactDetailActivity.class, addsTo = ApplicationModule.class)
public class ContactModule {

    private final long contactId;
    private final LoaderProvider loaderProvider;
    private final AppRepository appRepository;
    private final LoaderManager loaderManager;
    private final ContactContract.View view;

    public ContactModule(@NonNull LoaderProvider loaderProvider,
                         @NonNull LoaderManager loaderManager,
                         @NonNull AppRepository repository,
                         @NonNull ContactContract.View view,
                         @NonNull long contactId) {
        this.contactId = contactId;
        this.loaderProvider = loaderProvider;
        this.loaderManager = loaderManager;
        this.appRepository = repository;
        this.view = view;
    }

    @Provides
    ContactContract.Presenter provideContactPresenter() {
        return new ContactPresenter(loaderProvider, loaderManager,
                appRepository, view, contactId);
    }
}
