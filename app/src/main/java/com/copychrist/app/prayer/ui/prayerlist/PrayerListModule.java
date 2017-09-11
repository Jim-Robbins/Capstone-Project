package com.copychrist.app.prayer.ui.prayerlist;

import com.copychrist.app.prayer.ApplicationModule;
import com.copychrist.app.prayer.model.PrayerList;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 9/10/17.
 */

@Module(injects = PrayerListActivity.class, addsTo = ApplicationModule.class)
public class PrayerListModule {
    private final PrayerList prayerList;

    public PrayerListModule(final PrayerList prayerList) {
        this.prayerList = prayerList;
    }

    @Provides
    PrayerListService providePrayerListService(FirebaseDatabase firebaseDatabase) {
        return new PrayerListService(firebaseDatabase);
    }

    @Provides
    PrayerListContract.Presenter providePresenter(PrayerListService prayerListService) {
        return new PrayerListPresenter(prayerListService, prayerList);
    }
}
