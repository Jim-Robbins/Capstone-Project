package com.copychrist.app.prayer.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.copychrist.app.prayer.PrayingWithDedicationApplication;
import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.service.DbtService;
import com.copychrist.app.prayer.service.VerseOfTheDayIntentService;
import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import timber.log.Timber;

/**
 * Created by jim on 8/14/17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected FirebaseAnalytics firebaseAnalytics;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrayingWithDedicationApplication.injectModules(this, getModule());

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(verseOfTheDayReceiver);
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver((verseOfTheDayReceiver),
                new IntentFilter(VerseOfTheDayIntentService.STATUS_FINISHED)
        );
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.progress_loading));
        }

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    protected abstract Object getModule();

    protected void logEvent(String id, String name, String contentType) {
        Bundle bundle = new Bundle();
        if(!TextUtils.isEmpty(id)) bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        if(!TextUtils.isEmpty(name)) bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        if(!TextUtils.isEmpty(contentType)) bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    /**
     * Our handler for received Intents. This will be called whenever a status event is
     * broadcast from the IntentService
     */
    private BroadcastReceiver verseOfTheDayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.d("Verse is ready");
            updateVerseUI(intent);
        }
    };

    protected abstract void updateVerseUI(Intent intent);
}