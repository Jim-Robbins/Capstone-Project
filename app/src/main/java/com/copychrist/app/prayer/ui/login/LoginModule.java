package com.copychrist.app.prayer.ui.login;

import com.copychrist.app.prayer.ApplicationModule;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jim on 9/8/17.
 */

@Module(injects = LoginActivity.class, addsTo = ApplicationModule.class)
public class LoginModule {

    @Provides
    LoginService provideLoginService(FirebaseDatabase database) {
        return new LoginService(database);
    }

    @Provides
    LoginContract.Presenter provideMyListPresenter(final LoginService loginService) {
        return new LoginPresenter(loginService);
    }
}
