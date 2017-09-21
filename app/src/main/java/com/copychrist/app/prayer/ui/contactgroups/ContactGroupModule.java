package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.model.ContactGroup;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/14/17.
 */

@Module(injects = ContactGroupActivity.class, addsTo = ApplicationModule.class)
public class ContactGroupModule {

    private final ContactGroup contactGroup;

    public ContactGroupModule(final ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
    }

    @Provides
    protected ContactGroupService provideContactGroupService(FirebaseDatabase firebaseDatabase, FirebaseUser user) {
        return new ContactGroupService(firebaseDatabase, user);
    }

    @Provides
    protected ContactGroupContract.Presenter provideMyListPresenter(ContactGroupService contactGroupService) {
        return new ContactGroupPresenter(contactGroupService, contactGroup);
    }
}
