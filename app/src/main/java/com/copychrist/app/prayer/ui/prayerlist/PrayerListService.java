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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final DatabaseReference.CompletionListener prayerRequestUpdateCompletionListener;
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
                List<PrayerRequest> nonListResults = new ArrayList<>();
                List<PrayerRequest> listResults = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    PrayerRequest prayerRequest = snapshot.getValue(PrayerRequest.class);
                    if (prayerRequest != null && prayerRequest.getAnswered() == null) {
                        prayerRequest.setKey(snapshot.getKey());
                        HashMap<String, Boolean> prayerList = prayerRequest.getPrayerLists();
                        if(prayerList.containsKey(selectedPrayerList.getKey()) == true) {
                            listResults.add(prayerRequest);
                        } else {
                            nonListResults.add(prayerRequest);
                        }
                    }
                }
                sendPrayerRequestResults(listResults, nonListResults);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }
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

        prayerRequestUpdateCompletionListener = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Timber.e("Error updating data: " + databaseError.getMessage());
                    sendDataResultMessage(databaseError.getMessage());
                }
            }
        };
    }

    public void setPresenter(PrayerListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void getPrayerListValues() {
        Timber.d("getPrayerListValues()");

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
        prayerRequestsRef.orderByChild(PrayerRequest.CHILD_DATE_CREATED)
                .limitToFirst(200)
                .addValueEventListener(prayerRequestValueEventListener);

    }

    public void updatePrayerRequestWithList(List<String> prayerRequestKeys, String prayerListKey) {
        Timber.d("updatePrayerRequestWithList() called with: prayerRequestKeys = [" + prayerRequestKeys + "]");

        Map updatedPrayerRequestData = new HashMap();
        for (String prayerRequestKey : prayerRequestKeys) {
            updatedPrayerRequestData.put(prayerRequestKey + '/' + PrayerList.DB_NAME + '/'+ prayerListKey, true);
        }

        prayerRequestsRef.updateChildren(updatedPrayerRequestData, prayerRequestUpdateCompletionListener);
    }

    public void prayedForRequest(String prayerRequestKey) {
        prayerRequestsRef.child(prayerRequestKey).child(PrayerRequest.CHILD_LAST_PRAYED_FOR).setValue(Utils.getCurrentTime());
    }

    public void archivePrayerRequest(String prayerRequestKey) {
        prayerRequestsRef.child(prayerRequestKey).child(PrayerRequest.CHILD_ANSWERED).setValue(Utils.getCurrentTime());
    }

    public void removePrayerRequestFromList(String prayerRequestKey, String prayerListKey) {
        prayerRequestsRef.child(prayerRequestKey).child(PrayerList.DB_NAME).child(prayerListKey).removeValue();
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

    private void sendPrayerRequestResults(List<PrayerRequest> filteredResults, List<PrayerRequest> nonListResults) {
        presenter.onPrayerRequestResults(filteredResults, nonListResults);
    }

    public void destroy() {
        try {
            prayerListsRef.removeEventListener(prayerListDataListener);
            prayerListsRef.removeEventListener(prayerListEditDeleteChildEventListener);
            prayerRequestsRef.removeEventListener(prayerRequestValueEventListener);
            if (prayerListsQuery != null) prayerListsQuery.removeEventListener(queryAddPrayerListChildEventListener);
        } catch (Error e) {
            Timber.w(e.getMessage());
        }
    }
}
