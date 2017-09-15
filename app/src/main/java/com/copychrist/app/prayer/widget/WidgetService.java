package com.copychrist.app.prayer.widget;

import com.copychrist.app.prayer.model.PrayerList;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 9/15/17.
 */

public class WidgetService {

    private PrayerList selectedPrayerList;
    private Query prayerRequestsRef;
    private ValueEventListener prayerRequestValueEventListener;
    private WidgetRemoteViewsService.ListRemoteViewsFactory viewService;

    public WidgetService(FirebaseDatabase database, FirebaseUser currentUser) {
        prayerRequestsRef = database.getReference(PrayerRequest.DB_NAME).child(currentUser.getUid());

        prayerRequestValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PrayerRequest> listResults = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    PrayerRequest prayerRequest = snapshot.getValue(PrayerRequest.class);
                    if (prayerRequest != null && prayerRequest.getAnswered() == null) {
                        prayerRequest.setKey(snapshot.getKey());
                        HashMap<String, Boolean> prayerList = prayerRequest.getPrayerLists();
                        if (selectedPrayerList == null) {
                            listResults.add(prayerRequest);
                        } else if(prayerList.containsKey(selectedPrayerList.getKey()) == true) {
                            listResults.add(prayerRequest);
                        }
                    }
                }
                List<PrayerRequest> sortedList = Lists.reverse(listResults);
                sendPrayerRequestResults(sortedList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }
        };
    }

    public void getPrayerRequestValues(WidgetRemoteViewsService.ListRemoteViewsFactory viewService, PrayerList prayerList) {
        this.viewService = viewService;
        Timber.d("getPrayerRequestValues() called with: prayerList = [" + prayerList + "]");
        selectedPrayerList = prayerList;

        // Pull in latest 50
        prayerRequestsRef.orderByChild(PrayerRequest.CHILD_DATE_CREATED)
                .limitToLast(50)
                .addValueEventListener(prayerRequestValueEventListener);
    }

    private void sendPrayerRequestResults(List<PrayerRequest> filteredResults) {
        viewService.onPrayerRequestResults(filteredResults);
    }

    private void sendDataResultMessage(String message) {
        viewService.onDataResultMessage(message);
    }

    public void destroy() {
        prayerRequestsRef.removeEventListener(prayerRequestValueEventListener);
    }
}
