package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jim on 9/8/17.
 *
 */

public class PrayerRequestService implements PrayerRequestContract.Service {

    private DatabaseReference prayerRequestsRef;
    private DatabaseReference biblePassagesRef;
    private ChildEventListener prayerRequestDeleteChildEventListener;
    private ValueEventListener biblePassageDataListener;
    private ChildEventListener biblePassageChildEventListener;

    private PrayerRequestContract.Presenter presenter;
    private List<String> selectedPassages;

    public PrayerRequestService(FirebaseDatabase database, FirebaseUser currentUser) {
        prayerRequestsRef = database.getReference(PrayerRequest.DB_NAME).child(currentUser.getUid());
        biblePassagesRef = database.getReference(BiblePassage.DB_NAME).child(currentUser.getUid());

        prayerRequestDeleteChildEventListener = new ChildEventListener() {
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                sendDataResultMessage(R.string.data_value_changed);
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                sendDataResultMessage(R.string.data_value_deleted);
                presenter.onPrayerRequestDeleteCompleted();
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }

            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
        };

        biblePassageDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BiblePassage> listResults = new ArrayList<>();
                List<BiblePassage> nonListResults = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    BiblePassage biblePassage = snapshot.getValue(BiblePassage.class);
                    if (biblePassage != null) {
                        if(selectedPassages != null && selectedPassages.contains(biblePassage.getKey())) {
                            listResults.add(biblePassage);
                        } else {
                            nonListResults.add(biblePassage);
                        }
                    }
                }
                sendBiblePassagesResult(listResults, nonListResults);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }
        };

        biblePassageChildEventListener = new ChildEventListener() {
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                sendDataResultMessage(R.string.data_value_changed);
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                sendDataResultMessage(R.string.data_value_deleted);
                presenter.onBiblePassageDeleteCompleted();
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }

            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
        };
    }

    public void setPresenter(PrayerRequestContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onPrayerRequestSave(PrayerRequest prayerRequest) {
        if (prayerRequest.getKey() == null ) {
            prayerRequestsRef.push().setValue(prayerRequest);
        } else {
            prayerRequestsRef.child(prayerRequest.getKey()).setValue(prayerRequest);
        }
    }

    @Override
    public void onPrayerRequestArchive(PrayerRequest prayerRequest) {
        prayerRequest.setAnswered(Utils.getCurrentTime());
        prayerRequestsRef.child(prayerRequest.getKey()).setValue(prayerRequest);
    }

    @Override
    public void onPrayerRequestUnarchive(PrayerRequest prayerRequest) {
        prayerRequest.setAnswered(null);
        prayerRequestsRef.child(prayerRequest.getKey()).setValue(prayerRequest);
    }

    @Override
    public void onPrayerRequestDelete(PrayerRequest prayerRequest) {
        prayerRequestsRef.child(prayerRequest.getKey()).removeValue();
    }

    @Override
    public void onBiblePassagesLoad(List<String> selectedPassages) {
        this.selectedPassages = selectedPassages;
        biblePassagesRef.addValueEventListener(biblePassageDataListener);
    }

    @Override
    public void onBiblePassageSave(BiblePassage biblePassage) {
        biblePassagesRef.child(biblePassage.getKey()).setValue(biblePassage);
        biblePassagesRef.addChildEventListener(biblePassageChildEventListener);
    }

    @Override
    public void onBiblePassageDelete(BiblePassage biblePassage) {

    }

    private void sendBiblePassagesResult(List<BiblePassage> listResults, List<BiblePassage> nonListResults) {
        presenter.onBiblePassageResults(listResults, nonListResults);
    }

    private void sendDataResultMessage(String message) {
        presenter.onDataResultMessage(message);
    }

    private void sendDataResultMessage(int messageResId) {
        presenter.onDataResultMessage(messageResId);
    }

    public void onDestroy() {
        prayerRequestsRef.removeEventListener(prayerRequestDeleteChildEventListener);
        biblePassagesRef.removeEventListener(biblePassageChildEventListener);
        biblePassagesRef.removeEventListener(biblePassageDataListener);
    }
}
