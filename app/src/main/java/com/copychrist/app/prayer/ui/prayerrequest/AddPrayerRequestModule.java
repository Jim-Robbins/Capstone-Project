package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.repository.RealmService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/19/17.
 */
@Module(
        injects = AddPrayerRequestDetailActivity.class,
        addsTo = ApplicationModule.class
)
public class AddPrayerRequestModule {

    private final int contactId;

    public AddPrayerRequestModule(final int contactId) {
        this.contactId = contactId;
    }

    @Provides
    AddPrayerRequestPresenter provideAddPrayerRequestPresenter(final RealmService realmService) {
        return new AddAddPrayerRequestPresenterImpl(realmService, contactId);
    }
}
