package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.repository.RealmService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/14/17.
 */

@Module(injects = ContactsActivity.class, addsTo = ApplicationModule.class)
public class ContactsModule {

    private final int contactGroupId;

    public ContactsModule(final int contactGroupId) {
        this.contactGroupId = contactGroupId;
    }

    @Provides
    ContactsPresenter provideMyListPresenter(final RealmService realmService) {
        return new ContactsPresenterImpl(realmService, contactGroupId);
    }
}
