package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.data.AppDataSource;

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

    private final long requestId;

    public EditPrayerRequestModule(final long requestId) {
        this.requestId = requestId;
    }

    @Provides
    EditPrayerRequestPresenter provideEditPrayerRequestPresenter() {
        return new EditPrayerRequestPresenterImpl(requestId);
    }
}
