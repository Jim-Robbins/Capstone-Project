package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.data.AppDataSource;

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

    private final long contactId;

    public AddPrayerRequestModule(final long contactId) {
        this.contactId = contactId;
    }

    @Provides
    AddPrayerRequestPresenter provideAddPrayerRequestPresenter() {
        return new AddPrayerRequestPresenterImpl(contactId);
    }
}
