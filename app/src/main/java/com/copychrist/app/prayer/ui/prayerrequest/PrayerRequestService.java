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

import java.util.Date;
import java.util.List;

/**
 * Created by jim on 9/8/17.
 */

public class PrayerRequestService {

    DatabaseReference prayerRequestsRef;
    DatabaseReference bibleVersesRef;
    ChildEventListener prayerRequestDeleteChildEventListener;

    private PrayerRequestContract.Presenter presenter;

    public PrayerRequestService(FirebaseDatabase database, FirebaseUser currentUser) {
        prayerRequestsRef = database.getReference(PrayerRequest.DB_NAME).child(currentUser.getUid());
        bibleVersesRef = database.getReference(BiblePassage.DB_NAME).child(currentUser.getUid());

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
    }

    public void saveValue(PrayerRequest prayerRequest) {
        if (prayerRequest.getKey() == null ) {
            prayerRequestsRef.push().setValue(prayerRequest);
        } else {
            prayerRequestsRef.child(prayerRequest.getKey()).setValue(prayerRequest);
        }

        //Todo: Also process passages
        //Todo: Also process prayerList
    }

    public void archivePrayerRequest(PrayerRequestContract.Presenter presenter, PrayerRequest prayerRequest) {
        this.presenter = presenter;
        prayerRequest.setAnswered(new Date(Utils.getCurrentTime()));
        prayerRequestsRef.child(prayerRequest.getKey()).setValue(prayerRequest);
    }

    public void unarchivePrayerRequest(PrayerRequestContract.Presenter presenter, PrayerRequest prayerRequest) {
        this.presenter = presenter;
        prayerRequest.setAnswered(null);
        prayerRequestsRef.child(prayerRequest.getKey()).setValue(prayerRequest);
    }

    public void deletePrayerRequest(PrayerRequestContract.Presenter presenter, PrayerRequest prayerRequest) {
        this.presenter = presenter;
        prayerRequestsRef.child(prayerRequest.getKey()).removeValue();
        // Todo: Remove key from any prayer lists
    }

    private void sendBibleVersesResult(List<BiblePassage> results) {
        presenter.onBibleVerseResults(results);
    }

    private void sendDataResultMessage(String message) {
        presenter.onDataResultMessage(message);
    }

    private void sendDataResultMessage(int messageResId) {
        presenter.onDataResultMessage(messageResId);
    }

    public void destroy() {
        prayerRequestsRef.removeEventListener(prayerRequestDeleteChildEventListener);
    }
}
