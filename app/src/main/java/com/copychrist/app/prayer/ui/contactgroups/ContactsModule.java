package com.copychrist.app.prayer.ui.contactgroups;

import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.data.AppRepository;
import com.copychrist.app.prayer.data.LoaderProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/14/17.
 */

@Module(injects = ContactsActivity.class, addsTo = ApplicationModule.class)
public class ContactsModule {

    private final long contactGroupId;
    private final LoaderProvider loaderProvider;
    private final AppRepository appRepository;
    private final LoaderManager loaderManager;
    private final ContactGroupContract.View view;
    private final ContactGroupFilter filter;

    public ContactsModule(@NonNull LoaderProvider loaderProvider,
                          @NonNull LoaderManager loaderManager,
                          @NonNull AppRepository repository,
                          @NonNull ContactGroupContract.View view,
                          @NonNull ContactGroupFilter filter,
                          @NonNull long contactGroupId) {
        this.contactGroupId = contactGroupId;
        this.loaderProvider = loaderProvider;
        this.loaderManager = loaderManager;
        this.appRepository = repository;
        this.filter = filter;
        this.view = view;
    }

    @Provides
    public ContactGroupContract.Presenter providePresenter() {
        return new ContactsPresenter(loaderProvider, loaderManager,
                appRepository, view, filter, contactGroupId);
    }
}
