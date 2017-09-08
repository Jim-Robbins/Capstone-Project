package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.ApplicationModule;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/19/17.
 */
@Module(
        injects = {
                EditPrayerRequestDetailActivity.class,
                AddPrayerRequestDetailActivity.class
        },
        addsTo = ApplicationModule.class
)
public class PrayerRequestModule {

    private final String itemId;

    public PrayerRequestModule(final String itemId) {
        this.itemId = itemId;
    }

    @Provides
    PrayerRequestService providePrayerRequestService(FirebaseDatabase database) {
        return new PrayerRequestService(database);
    }

    @Provides
    PrayerRequestContract.EditPresenter provideEditPrayerRequestPresenter(final PrayerRequestService dataService) {
        return new EditPrayerRequestPresenter(dataService, itemId);
    }

    @Provides
    PrayerRequestContract.AddPresenter provideAddPrayerRequestPresenter(final PrayerRequestService dataService) {
        return new AddPrayerRequestPresenter(dataService, itemId);
    }
}
