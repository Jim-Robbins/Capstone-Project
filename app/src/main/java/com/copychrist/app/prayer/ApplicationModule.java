package com.copychrist.app.prayer;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 8/14/17.
 * Reference : https://www.thedroidsonroids.com/blog/android/example-realm-mvp-dagger/
 */

@Module(injects = PrayingWithDedicationApplication.class, library = true)
public class ApplicationModule {
    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    FirebaseApp provideFirebaseApp() {
        return FirebaseApp.getInstance();
    }

    @Provides
    FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance(provideFirebaseApp());
    }

    @Provides
    FirebaseUser provideFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Provides
    Resources provideResources() {
        return application.getResources();
    }

    @Provides
    Context provideContext() {
        return application.getBaseContext();
    }
}
