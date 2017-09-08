package com.copychrist.app.prayer;

import com.google.firebase.FirebaseApp;
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
    FirebaseApp provideFirebaseApp() {
        return FirebaseApp.getInstance();
    }

    @Provides
    FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance(provideFirebaseApp());
    }
}
