package com.copychrist.app.prayer;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.copychrist.app.prayer.util.ReleaseTree;
import com.squareup.leakcanary.LeakCanary;

import dagger.ObjectGraph;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.log.RealmLog;
import timber.log.Timber;

public class PrayingWithDedicationApplication extends Application {
    private static PrayingWithDedicationApplication instance;
    private ObjectGraph appGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initMemoryLeakCheck();
        initDatabase();
        initLogger();
        initApplicationGraph();
    }

    private void initMemoryLeakCheck() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void initDatabase() {
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    private void initLogger() {
        if (BuildConfig.DEBUG) {
            // Enable full log output when debugging
            RealmLog.setLevel(Log.VERBOSE);
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

    private void initApplicationGraph() {
        appGraph = ObjectGraph.create(new ApplicationModule());
    }

    public static void injectModules(@NonNull final Object object, final Object... modules) {
        instance.appGraph.plus(modules).inject(object);
    }
}