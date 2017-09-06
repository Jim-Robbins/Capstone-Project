package com.copychrist.app.prayer;

import com.copychrist.app.prayer.repository.DatabaseSerivce;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/14/17.
 * Reference : https://www.thedroidsonroids.com/blog/android/example-realm-mvp-dagger/
 */

@Module(injects = PrayingWithDedicationApplication.class, library = true)
public class ApplicationModule {
    public ApplicationModule() {}

    @Provides
    DatabaseReference provideDatabase() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Provides
    DatabaseSerivce provideDatabaseService() {
        return new DatabaseSerivce();
    }
}
