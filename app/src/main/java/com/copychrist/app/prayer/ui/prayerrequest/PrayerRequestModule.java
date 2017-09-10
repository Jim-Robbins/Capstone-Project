package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.ui.ViewMode;
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

    private final String itemId;
    private final ViewMode viewMode;

    public PrayerRequestModule(final String itemId, final ViewMode viewMode) {
        this.itemId = itemId;
        this.viewMode = viewMode;
    }

    @Provides
    @Singleton
    PrayerRequestService providePrayerRequestService(FirebaseDatabase database) {
        return new PrayerRequestService(database);
    }

    @Provides
    PrayerRequestContract.Presenter providePrayerRequestPresenter(final PrayerRequestService dataService) {
        return new PrayerRequestPresenter(dataService, itemId, viewMode);
    }
}
