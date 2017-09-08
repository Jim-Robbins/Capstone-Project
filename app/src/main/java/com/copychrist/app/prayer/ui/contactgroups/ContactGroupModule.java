package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.ApplicationModule;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/14/17.
 */

@Module(injects = ContactGroupActivity.class, addsTo = ApplicationModule.class)
public class ContactGroupModule {

    private final int contactGroupId;

    public ContactGroupModule(final int contactGroupId) {
        this.contactGroupId = contactGroupId;
    }

    @Provides
    ContactGroupService provideContactGroupService(FirebaseDatabase firebaseDatabase) {
        return new ContactGroupService(firebaseDatabase);
    }

    @Provides
    ContactGroupContract.Presenter provideMyListPresenter(ContactGroupService contactGroupService) {
        return new ContactGroupPresenter(contactGroupService, contactGroupId);
    }
}
