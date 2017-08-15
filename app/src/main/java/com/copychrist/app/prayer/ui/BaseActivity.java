package com.copychrist.app.prayer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.copychrist.app.prayer.PrayingWithDedicationApplication;

/**
 * Created by jim on 8/14/17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrayingWithDedicationApplication.injectModules(this, getModule());
    }

    @Override
    protected void onDestroy() {
        closeRealm();
        super.onDestroy();
    }

    protected abstract Object getModule();
    protected abstract void closeRealm();
}
