package com.copychrist.app.prayer.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.copychrist.app.prayer.PrayingWithDedicationApplication;
import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.PrayerListRequest;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by jim on 9/15/17.
 */

public class WidgetRemoteViewsService extends RemoteViewsService {

    @Inject
    protected WidgetService dataService;

    @Override
    public void onCreate() {
        Timber.i("onCreate");
        super.onCreate();
        PrayingWithDedicationApplication.injectModules(this, getModule());
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    protected Object getModule() {
        return new WidgetModule();
    }

    public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context context;
        private List<PrayerListRequest> prayerListRequests;
        private CountDownLatch countDownLatch;

        ListRemoteViewsFactory(Context context) {
            Timber.i("ListRemoteViewsFactory");
            this.context = context;
            prayerListRequests = new ArrayList<>();
        }

        @Override
        public void onCreate() {
        }

        @Override
        public int getCount() {
            return prayerListRequests.size();
        }

        @Override
        public void onDataSetChanged() {
            Timber.i("onDataSetChanged");
            countDownLatch = new CountDownLatch(1);
            dataService.getPrayerRequestValues(this, null);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void onPrayerRequestResults(List<PrayerRequest> requestList) {
            Timber.i("onPrayerRequestResults");
            prayerListRequests = new ArrayList<>();
            for (PrayerRequest prayerRequest : requestList) {
                PrayerListRequest prayerListRequest = new PrayerListRequest();
                prayerListRequest.setContact(prayerRequest.getContact());
                prayerListRequest.setGroup(prayerRequest.getContactGroup());
                prayerListRequest.setPrayerRequest(prayerRequest);
                prayerListRequest.setColor(prayerRequest.getContact().getProfileColor());
                prayerListRequests.add(prayerListRequest);
            }
            countDownLatch.countDown();
        }

        public void onDataResultMessage(String message) {
            Timber.d("No data:" + message);
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item_prayer_request);
            if (position < prayerListRequests.size()) {

                PrayerListRequest prayerRequest = prayerListRequests.get(position);

                if (prayerRequest != null) {
                    Timber.i(prayerRequest.getTitle());
                    SimpleDateFormat dateFormat = Utils.getDateFormat(context);
                    rv.setTextViewText(R.id.txt_contact_name, prayerRequest.getContactName());
                    // displaying the first letter of From in icon text
                    rv.setTextViewText(R.id.txt_icon_text, prayerRequest.getContactName().substring(0, 1));
                    rv.setTextViewText(R.id.txt_request_title, prayerRequest.getTitle());
                    rv.setTextViewText(R.id.txt_request_details, prayerRequest.getDesc());
                    if(prayerRequest.getEndDate() != null)
                        rv.setTextViewText(R.id.txt_timestamp, dateFormat.format(prayerRequest.getEndDate()));
                }
            }

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDestroy() {
            // Nothing to do here.
        }
    }
}