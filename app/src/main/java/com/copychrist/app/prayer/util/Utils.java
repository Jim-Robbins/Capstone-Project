package com.copychrist.app.prayer.util;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import com.copychrist.app.prayer.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by jim on 8/26/17.
 */

public class Utils {
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    public static Locale getCurrentLocale(Context context) {
        Locale currentLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentLocale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            currentLocale = context.getResources().getConfiguration().locale;
        }
        return currentLocale;
    }

    public static SimpleDateFormat getDateFormat(Context context) {
        return new SimpleDateFormat(context.getString(R.string.date_format), getCurrentLocale(context));
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis()/1000;
    }
}
