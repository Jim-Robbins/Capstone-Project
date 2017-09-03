package com.copychrist.app.prayer;

import com.copychrist.app.prayer.data.AppDataSource;
import com.copychrist.app.prayer.repository.RealmService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/14/17.
 * Reference : https://www.thedroidsonroids.com/blog/android/example-realm-mvp-dagger/
 */

@Module(injects = PrayingWithDedicationApplication.class, library = true)
public class ApplicationModule {
    public ApplicationModule() {}

}
