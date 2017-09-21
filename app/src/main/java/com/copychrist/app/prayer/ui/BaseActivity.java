package com.copychrist.app.prayer.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.copychrist.app.prayer.PrayingWithDedicationApplication;
import com.copychrist.app.prayer.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

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
}
