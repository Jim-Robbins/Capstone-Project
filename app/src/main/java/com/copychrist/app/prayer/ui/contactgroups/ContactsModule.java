package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.ApplicationModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/14/17.
 */

@Module(injects = ContactsActivity.class, addsTo = ApplicationModule.class)
public class ContactsModule {

    private final long contactGroupId;

    public ContactsModule(final long contactGroupId) {
        this.contactGroupId = contactGroupId;
    }

    @Provides
    public ContactsPresenter providePresenter() {
        return new ContactsPresenterImpl(contactGroupId);
    }
}
