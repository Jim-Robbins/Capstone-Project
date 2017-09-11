package com.copychrist.app.prayer.ui;

/**
 * Created by jim on 9/9/17.
 */

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ViewMode {
    public static final String ADD_MODE = "add";
    public static final String EDIT_MODE = "edit";
    public static final String REQUEST_MODE = "request";
    public static final String ARCHIVE_MODE = "archive";
    public final String mode;

    // Describes when the annotation will be discarded
    @Retention(RetentionPolicy.SOURCE)
    // Enumerate valid values for this interface
    @StringDef({ ADD_MODE, EDIT_MODE, REQUEST_MODE, ARCHIVE_MODE })
    // Create an interface for validating String types
    public @interface ViewModeDef { }

    // Mark the argument as restricted to these enumerated types
    public ViewMode(@ViewModeDef String mode) {
        this.mode = mode;
    }

    public boolean equals(String mode) {
        return this.mode.equalsIgnoreCase(mode);
    }
}
