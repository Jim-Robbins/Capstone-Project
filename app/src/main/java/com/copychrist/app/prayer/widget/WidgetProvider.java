package com.copychrist.app.prayer.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.ui.home.MainActivity;
import com.copychrist.app.prayer.ui.prayerlist.PrayerListActivity;

/**
 * Created by jim on 9/15/17.
 */

/*
 AppWidgetProvider extends BroadcastReceiver
 */
public class WidgetProvider extends AppWidgetProvider {

    public static final String ACTION_DATA_UPDATED = "data_updated";

    /**
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds - holds ids of multiple instance of your widget, meaning you are
     *                       placing more than one widgets on your homescreen
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_content);

            // Create an Intent to launch MainActivity when icon is clicked
            Intent mainIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_icon, pendingIntent);
            remoteViews.setEmptyView(R.id.widget_list, R.id.widget_empty);

            Intent svcIntent = new Intent(context, WidgetRemoteViewsService.class);
            remoteViews.setRemoteAdapter(R.id.widget_list, svcIntent);

            Intent clickIntentTemplate = new Intent(context, PrayerListActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);


            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        Log.d("WidgetProvider", "onReceive action:"  + intent.getAction());
        if (intent.getAction().equals(ACTION_DATA_UPDATED)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout_content);
            rv.removeAllViews(appWidgetId);
        }
    }
}
