package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.ui.contactgroups.ContactGroupService;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/19/17.
 */
@Module(injects = ContactDetailActivity.class, addsTo = ApplicationModule.class)
public class ContactModule {

    private final String contactId;

    public ContactModule(final String contactId) {
        this.contactId = contactId;
    }

    @Provides
    ContactService provideContactService(FirebaseDatabase database) {
        return new ContactService(database);
    }

    @Provides
    ContactContract.Presenter provideContactPresenter(final ContactGroupService contactGroupService) {
        return new ContactPresenter(contactGroupService, contactId);
    }
}
