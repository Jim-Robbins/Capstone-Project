package com.copychrist.app.prayer.ui;

import android.os.Bundle;
import android.support.compat.BuildConfig;

/**
 * Created by jim on 9/3/17.
 */

public abstract class BaseFilter {
    protected final static String BASE_KEY_FILTER = BuildConfig.APPLICATION_ID;
    protected Bundle filterExtras;

    public abstract Object getFilterType();

    public Bundle getFilterExtras() {
        return filterExtras;
    }
}
