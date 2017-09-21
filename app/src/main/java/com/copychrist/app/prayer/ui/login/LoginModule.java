package com.copychrist.app.prayer.ui.login;

import com.copychrist.app.prayer.ApplicationModule;

import dagger.Module;

/**
 * Created by jim on 9/21/17.
 */

@Module(injects = LoginActivity.class, addsTo = ApplicationModule.class)
public class LoginModule {

    public LoginModule() {

    }
}