package com.copychrist.app.prayer;

import com.copychrist.app.prayer.repository.RealmService;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by jim on 8/14/17.
 * Reference : https://www.thedroidsonroids.com/blog/android/example-realm-mvp-dagger/
 */

@Module(injects = PrayingWithDedicationApplication.class, library = true)
public class ApplicationModule {
    public ApplicationModule() {}

    @Provides
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    RealmService provideRealmService(final Realm realm) {
        return new RealmService(realm);
    }
}
