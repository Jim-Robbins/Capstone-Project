package com.copychrist.app.prayer.data.local;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by jim on 9/3/17.
 */

@Qualifier
@Retention(RUNTIME)
public @interface Local {
}
