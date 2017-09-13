package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/19/17.
 */
@Module(
        injects = PrayerRequestDetailActivity.class,
        addsTo = ApplicationModule.class
)
public class PrayerRequestModule {

    private final Contact contact;
    private final PrayerRequest prayerRequest;
    private final String prayerListKey;

    public PrayerRequestModule(final Contact contact, final PrayerRequest prayerRequest, String prayerListKey) {
        this.contact = contact;
        this.prayerRequest = prayerRequest;
        this.prayerListKey = prayerListKey;
    }

    @Provides
    @Singleton
    PrayerRequestService providePrayerRequestService(FirebaseDatabase database, FirebaseUser user) {
        return new PrayerRequestService(database, user);
    }

    @Provides
    PrayerRequestContract.Presenter providePrayerRequestPresenter(final PrayerRequestService dataService) {
        return new PrayerRequestPresenter(dataService, contact, prayerRequest, prayerListKey);
    }
}
