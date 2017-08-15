package com.copychrist.app.prayer.repository;

import io.realm.Realm;

/**
 * Created by jim on 8/14/17.
 * Reference : https://www.thedroidsonroids.com/blog/android/example-realm-mvp-dagger/
 */

public class RealmService {
    private final Realm realm;

    public RealmService(final Realm realm) {
        this.realm = realm;
    }

    public void closeRealm() {
        realm.close();
    }

    public interface OnTransactionCallback {
        void onRealmSuccess();
        void onRealmError(final Exception e);
    }
}
