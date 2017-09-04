package com.copychrist.app.prayer;

import android.content.Context;
import android.support.annotation.NonNull;

import com.copychrist.app.prayer.data.AppDataSource;
import com.copychrist.app.prayer.data.AppRepository;
import com.copychrist.app.prayer.data.local.AppLocalDataSource;
import com.copychrist.app.prayer.data.local.Local;
import com.copychrist.app.prayer.data.remote.AppRemoteDataSource;
import com.copychrist.app.prayer.data.remote.Remote;
import com.copychrist.app.prayer.repository.RealmService;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/14/17.
 * Reference : https://www.thedroidsonroids.com/blog/android/example-realm-mvp-dagger/
 */

@Module(injects = PrayingWithDedicationApplication.class, library = true)
public class ApplicationModule {
    PrayingWithDedicationApplication application;

    public ApplicationModule(PrayingWithDedicationApplication application) {
        this.application = application;
    }

    @Provides @Singleton
    public Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton
    public AppRepository provideAppRepository() {
        return new AppRepository( provideRemoteDataSource(), provideLocalDataSource(provideApplicationContext()));
    }

    public AppRemoteDataSource provideRemoteDataSource() {
        return new AppRemoteDataSource();
    }

    public AppLocalDataSource provideLocalDataSource(@NonNull Context context) {
        return new AppLocalDataSource(context.getContentResolver());
    }
}
