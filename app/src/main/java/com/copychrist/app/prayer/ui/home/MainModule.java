package com.copychrist.app.prayer.ui.home;

import com.copychrist.app.prayer.ApplicationModule;

import dagger.Module;

/**
 * Created by jim on 9/21/17.
 */

@Module(injects = MainActivity.class, addsTo = ApplicationModule.class)
public class MainModule {

    public MainModule() {

    }
}
