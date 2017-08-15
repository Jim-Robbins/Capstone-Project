package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.repository.RealmService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/14/17.
 */

@Module(injects = ContactsActivity.class, addsTo = ApplicationModule.class)
public class ContactsModule {

    @Provides
    ContactsPresenter provideMyListPresenter(final RealmService realmService) {
        return new ContactsPresenterImpl(realmService);
    }
}
