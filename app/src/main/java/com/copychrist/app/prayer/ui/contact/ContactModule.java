package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.repository.DatabaseSerivce;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/19/17.
 */
@Module(injects = ContactDetailActivity.class, addsTo = ApplicationModule.class)
public class ContactModule {

    private final int contactId;

    public ContactModule(final int contactId) {
        this.contactId = contactId;
    }

    @Provides
    ContactPresenter provideMyListPresenter(final DatabaseSerivce realmService) {
        return new ContactPresenterImpl(realmService, contactId);
    }
}
