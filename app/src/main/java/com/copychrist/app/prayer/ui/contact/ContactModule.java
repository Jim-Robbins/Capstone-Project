package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.model.Contact;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/19/17.
 */
@Module(injects = ContactDetailActivity.class, addsTo = ApplicationModule.class)
public class ContactModule {

    private final Contact contact;

    public ContactModule(final Contact contact) {
        this.contact = contact;
    }

    @Provides
    ContactService provideContactService(FirebaseDatabase database, FirebaseUser user) {
        return new ContactService(database, user);
    }

    @Provides
    ContactContract.Presenter provideContactPresenter(final ContactService contactService) {
        return new ContactPresenter(contactService, contact);
    }
}
