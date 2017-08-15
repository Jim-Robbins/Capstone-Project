package com.copychrist.app.prayer.util;

import android.util.Log;

import timber.log.Timber;

/**
 * By Donn Felker
 * https://caster.io/episodes/episode-14-logging-with-timber/
 */
public class ReleaseTree extends Timber.Tree {
    private static final int MAX_LOG_LEN = 4000;

    @Override
    protected boolean isLoggable(String tag, int priority) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return false;
        }
        return super.isLoggable(tag, priority);
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (isLoggable(tag, priority)) {
            // Message is short enough, does not need to be broken into chunks
            if (message.length() < MAX_LOG_LEN) {
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, message);
                } else {
                    Log.println(priority, tag, message);
                }
                return;
            }

            // Split by line, then ensure each line can fit into Log's maximum length.
            for (int i = 0, length = message.length() ; i < length; i++) {
                int newline = message.indexOf('\n', i);
                newline = newline != -1 ? newline : length;
                do {
                    int end = Math.min(newline, i + MAX_LOG_LEN);
                    String part = message.substring(i, end);
                    if (priority == Log.ASSERT) {
                        Log.wtf(tag, part);
                    } else {
                        Log.println(priority, tag, part);
                    }
                    i = end;
                } while (i < newline);
            }
        }
    }
}
