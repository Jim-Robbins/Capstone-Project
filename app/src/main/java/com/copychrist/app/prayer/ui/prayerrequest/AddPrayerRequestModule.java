package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.repository.DatabaseSerivce;

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
    AddPrayerRequestPresenter provideAddPrayerRequestPresenter(final DatabaseSerivce realmService) {
        return new AddPrayerRequestPresenterImpl(realmService, contactId);
    }
}
