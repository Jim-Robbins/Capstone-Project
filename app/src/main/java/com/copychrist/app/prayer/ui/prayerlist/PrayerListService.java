package com.copychrist.app.prayer.ui.prayerlist;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.PrayerList;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 9/10/17.
 *
 */

class PrayerListService {

    private final DatabaseReference prayerListsRef;
    private final DatabaseReference prayerRequestsRef;
    
    private final ValueEventListener prayerListDataListener;
    private final ValueEventListener prayerListDeleteSingleEventListener;
    private final ChildEventListener prayerListEditDeleteChildEventListener;
    private final ChildEventListener queryAddPrayerListChildEventListener;
    private final ValueEventListener prayerRequestValueEventListener;
    private final ChildEventListener prayerRequestChildEventListener;
    private Query prayerListsQuery;

    private PrayerList selectedPrayerList;
    private PrayerListContract.Presenter presenter;

    PrayerListService(FirebaseDatabase database, FirebaseUser currentUser) {
        prayerListsRef = database.getReference(PrayerList.DB_NAME).child(currentUser.getUid());
        prayerRequestsRef = database.getReference(PrayerRequest.DB_NAME).child(currentUser.getUid());

        // initialize PrayerRequest Group event listeners
        prayerListDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PrayerList> results = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    PrayerList tabPrayerList = snapshot.getValue(PrayerList.class);
                    if (tabPrayerList != null) {
                        tabPrayerList.setKey(snapshot.getKey());
                        results.add(tabPrayerList);
                    }
                    if(selectedPrayerList == null) {
                        // If select group is not set, default to first entry
                        selectedPrayerList = tabPrayerList;
                    }
                }
                sendPrayerListResults(results);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }
        };

        prayerListEditDeleteChildEventListener = new ChildEventListener() {
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                sendDataResultMessage(R.string.data_value_changed);
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                sendDataResultMessage(R.string.data_value_deleted);
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }

            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
        };

        queryAddPrayerListChildEventListener = new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                sendDataResultMessage("PrayerRequest Group successfully added");
                selectedPrayerList = dataSnapshot.getValue(PrayerList.class);
                selectedPrayerList.setKey(dataSnapshot.getKey());
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
        };

        // initialize PrayerRequest event listeners
        prayerRequestValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PrayerRequest> results = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    PrayerRequest prayerRequest = snapshot.getValue(PrayerRequest.class);
                    if (prayerRequest != null) {
                        if(prayerRequest.getPrayerLists().indexOf(selectedPrayerList.getKey()) > -1) {
                            prayerRequest.setKey(snapshot.getKey());
                            results.add(prayerRequest);
                        }
                    }
                }
                sendPrayerRequestResults(results);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }
        };

        prayerRequestChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Timber.d(dataSnapshot.toString());
                sendDataResultMessage("PrayerRequest successfully added");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
        };

        prayerListDeleteSingleEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    //Todo: remove list key from PrayerRequest
                    Timber.d(snapshot.toString());
                }
                deletePrayerListConfirmed(selectedPrayerList.getKey());
                selectedPrayerList = null;
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        };
    }

    public void getPrayerListValues(PrayerListContract.Presenter presenter) {
        Timber.d("getPrayerListValues()");

        this.presenter = presenter;

        prayerListsRef.orderByChild(PrayerList.CHILD_ORDER).addValueEventListener(prayerListDataListener);
        prayerListsRef.orderByChild(PrayerList.CHILD_ORDER).addChildEventListener(prayerListEditDeleteChildEventListener);

        prayerListsQuery = prayerListsRef.orderByChild(PrayerList.CHILD_DATE_CREATED).startAt(Utils.getCurrentTime());
        prayerListsQuery.addChildEventListener(queryAddPrayerListChildEventListener);
    }

    public void savePrayerListValue(final PrayerList prayerList) {
        if(prayerList.getKey() == null) {
            // Add PrayerRequest Group
            prayerListsRef.push().setValue(prayerList);
        } else {
            // Edit PrayerRequest Group
            prayerListsRef.child(prayerList.getKey()).setValue(prayerList);
        }
    }

    public void deletePrayerList() {
        Query qry = prayerListsRef.orderByChild(PrayerRequest.CHILD_PRAYER_LISTS)
                .startAt(selectedPrayerList.getKey())
                .endAt(selectedPrayerList.getKey());
        qry.addListenerForSingleValueEvent(prayerListDeleteSingleEventListener);
    }

    protected void deletePrayerListConfirmed(String prayerListKey) {
        prayerListsRef.child(prayerListKey).removeValue();
        //Todo: update the sort orders
    }


    public void getPrayerRequestValues(PrayerList prayerList) {
        Timber.d("getPrayerRequestValues() called with: prayerList = [" + prayerList + "]");
        selectedPrayerList = prayerList;
        prayerRequestsRef.orderByChild(PrayerRequest.CHILD_PRAYER_LISTS)
                .addValueEventListener(prayerRequestValueEventListener);

    }

    public void savePrayerRequestValue(PrayerRequest prayerRequest) {
        Timber.d("savePrayerRequestValue() called with: prayerRequest = [" + prayerRequest + "]");
        prayerRequestsRef.removeEventListener(prayerRequestChildEventListener);
        prayerRequestsRef.orderByChild(PrayerRequest.CHILD_DATE_CREATED)
                .startAt(Utils.getCurrentTime())
                .addChildEventListener(prayerRequestChildEventListener);

        if(prayerRequest.getKey() == null) {
            // Add PrayerRequest Group
            prayerRequestsRef.push().setValue(prayerRequest);
        } else {
            // Edit PrayerRequest Group
            prayerRequestsRef.child(prayerRequest.getKey()).setValue(prayerRequest);
        }
    }

    private void sendDataResultMessage(int messageResId) {
        presenter.onDataResultMessage(messageResId);
    }

    private void sendDataResultMessage(String message) {
        presenter.onDataResultMessage(message);
    }

    private void sendPrayerListResults(List<PrayerList> results) {
        presenter.onPrayerListResults(results, selectedPrayerList);
    }

    private void sendPrayerRequestResults(List<PrayerRequest> results) {
        presenter.onPrayerRequestResults(results);
    }

    public void destroy() {
        prayerListsRef.removeEventListener(prayerListDataListener);
        prayerListsRef.removeEventListener(prayerListEditDeleteChildEventListener);
        prayerListsQuery.removeEventListener(queryAddPrayerListChildEventListener);
        prayerRequestsRef.removeEventListener(prayerRequestValueEventListener);
        prayerRequestsRef.removeEventListener(prayerRequestChildEventListener);
    }
}
