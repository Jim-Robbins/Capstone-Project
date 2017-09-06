package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.repository.DatabaseSerivce;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/19/17.
 */
@Module(
        injects = EditPrayerRequestDetailActivity.class,
        addsTo = ApplicationModule.class
)
public class EditPrayerRequestModule {

    private final int requestId;

    public EditPrayerRequestModule(final int requestId) {
        this.requestId = requestId;
    }

    @Provides
    EditPrayerRequestPresenter provideEditPrayerRequestPresenter(final DatabaseSerivce realmService) {
        return new EditPrayerRequestPresenterImpl(realmService, requestId);
    }
}
