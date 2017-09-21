package com.copychrist.app.prayer;

import android.app.Application;
import android.support.annotation.NonNull;

import com.copychrist.app.prayer.util.ReleaseTree;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import dagger.ObjectGraph;
import timber.log.Timber;

public class PrayingWithDedicationApplication extends Application {
    private static PrayingWithDedicationApplication instance;
    private ObjectGraph appGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initLogger();
        initDb();
        initApplicationGraph();
    }

    private void initLogger() {
        if (BuildConfig.DEBUG) {
            // Enable full log output when debugging
            Timber.plant(new Timber.DebugTree() {
                // Add the line number to the tag.

                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) +
                            ":" + element.getMethodName() +
                            ":" + element.getLineNumber();
                }
            });
        } else {
            Timber.plant(new ReleaseTree());
        }
    }

    private void initDb() {
        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.INFO);
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            FirebaseDatabase.getInstance().getReference().keepSynced(true);
        }

    }

    private void initApplicationGraph() {
        appGraph = ObjectGraph.create(new ApplicationModule(this));
    }

    public static void injectModules(@NonNull final Object object, final Object... modules) {
        instance.appGraph.plus(modules).inject(object);
    }
}
