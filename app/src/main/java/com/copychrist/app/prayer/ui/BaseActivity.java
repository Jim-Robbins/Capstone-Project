package com.copychrist.app.prayer.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.copychrist.app.prayer.PrayingWithDedicationApplication;
import com.copychrist.app.prayer.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by jim on 8/14/17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrayingWithDedicationApplication.injectModules(this, getModule());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ProgressDialog progressDialog;

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

}
