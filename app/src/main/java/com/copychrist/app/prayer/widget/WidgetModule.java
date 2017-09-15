package com.copychrist.app.prayer.widget;

import com.copychrist.app.prayer.ApplicationModule;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 9/15/17.
 */

@Module(injects = WidgetRemoteViewsService.class, addsTo = ApplicationModule.class)
public class WidgetModule {
    @Provides
    WidgetService provideWidgetService(FirebaseDatabase firebaseDatabase, FirebaseUser user) {
        return new WidgetService(firebaseDatabase, user);
    }
}

