package com.copychrist.app.prayer.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.copychrist.app.prayer.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static Date stringToDate(String dateString, Context context) {
        if (TextUtils.isEmpty(dateString))
            return null;

        SimpleDateFormat format = getDateFormat(context);
        Date newDate = new Date();
        try {
            newDate = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis()/1000;
    }

    /**
     * https://www.androidhive.info/2017/02/android-creating-gmail-like-inbox-using-recyclerview/
     * @param context
     * @param typeColor
     * @return
     */
    private int getRandomMaterialColor(Context context, String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }
}
