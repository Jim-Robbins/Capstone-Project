package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.data.AppDataSource;
import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/19/17.
 */
@Module(injects = ContactDetailActivity.class, addsTo = ApplicationModule.class)
public class ContactModule {

    private final long contactId;

    public ContactModule(final long contactId) {
        this.contactId = contactId;
    }

    @Provides
    ContactPresenter provideMyListPresenter() {
        return new ContactPresenterImpl(contactId);
    }
}
