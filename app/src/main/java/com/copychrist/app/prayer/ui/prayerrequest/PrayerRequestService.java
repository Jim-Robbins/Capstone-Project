package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BaseService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by jim on 9/8/17.
 */

public class PrayerRequestService implements BaseService<PrayerRequest, PrayerRequestContract.AddPresenter> {
    private static final String TAG = "PrayerRequestService";

    FirebaseDatabase database;
    DatabaseReference prayerRequestGroupsRef;
    Query queryRef;
    ValueEventListener prayerRequestValueEventListener;
    ChildEventListener prayerRequestChildEventListener;
    ChildEventListener prayerRequestQryChildEventListener;

    public PrayerRequestService(FirebaseDatabase database) {
        this.database = database;
        prayerRequestGroupsRef = database.getReference(PrayerRequest.DB_NAME);
    }

    @Override
    public void getValues(PrayerRequestContract.AddPresenter presenter) {

    }

    @Override
    public void saveValue(PrayerRequest model) {

    }

    @Override
    public void deleteValue() {
    }

    @Override
    public void destroy() {

    }
}
