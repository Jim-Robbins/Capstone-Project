package com.copychrist.app.prayer.ui.home;

import com.copychrist.app.prayer.ApplicationModule;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 9/11/17.
 */
@Module(injects = MainActivity.class, addsTo = ApplicationModule.class)
public class MainModule {
    @Provides
    protected MainService provideMainService(FirebaseDatabase firebaseDatabase, FirebaseUser user) {
        return new MainService(firebaseDatabase, user);
    }

    @Provides
    protected MainContract.Presenter providePresenter(MainService mainService) {
        return new MainPresenter(mainService);
    }
}
