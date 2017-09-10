package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 9/8/17.
 */

public class PrayerRequestService {
    private static final String TAG = "PrayerRequestService";

    FirebaseDatabase database;
    DatabaseReference contactsRef;
    DatabaseReference prayerRequestsRef;
    DatabaseReference bibleVersesRef;
    DatabaseReference prayerListsRef;
    Query queryRef;
    ValueEventListener contactValueEventListener;
    ValueEventListener prayerRequestValueEventListener;
    ChildEventListener prayerRequestDeleteChildEventListener;
    ChildEventListener prayerRequestQryChildEventListener;

    private PrayerRequestContract.Presenter presenter;
    private Contact selectedContact;
    private PrayerRequest selectedPrayerRequest;

    public PrayerRequestService(FirebaseDatabase database) {
        this.database = database;
        contactsRef = database.getReference(Contact.DB_NAME);
        prayerRequestsRef = database.getReference(PrayerRequest.DB_NAME);
        bibleVersesRef = database.getReference(BiblePassage.DB_NAME);

        contactValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                if(contact != null) {
                    contact.setKey(dataSnapshot.getKey());
                    sendContactResults(contact);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }
        };

        prayerRequestValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PrayerRequest prayerRequest = dataSnapshot.getValue(PrayerRequest.class);
                if(prayerRequest != null) {
                    prayerRequest.setKey(dataSnapshot.getKey());
                    sendPrayerRequestResults(prayerRequest);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }
        };

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

    public void archivePrayerRequest() {
        selectedPrayerRequest.setAnswered(new Date(Utils.getCurrentTime()));
        prayerRequestsRef.child(selectedPrayerRequest.getKey()).setValue(selectedPrayerRequest);
    }

    public void deletePrayerRequest() {
        prayerRequestsRef.child(selectedPrayerRequest.getKey()).removeValue();
        // Todo: Remove key from any prayer lists
    }

    public void getContact(PrayerRequestContract.Presenter presenter, String contactKey) {
        Timber.d("getContact() called with: contactKey = [" + contactKey + "]");
        this.presenter = presenter;
        contactsRef.child(contactKey).addListenerForSingleValueEvent(contactValueEventListener);
    }

    public void getPrayerRequest(PrayerRequestContract.Presenter presenter, String prayerRequestKey) {
        Timber.d("getPrayerRequest() called with: prayerRequestKey = [" + prayerRequestKey + "]");
        this.presenter = presenter;
        prayerRequestsRef.child(prayerRequestKey).addListenerForSingleValueEvent(prayerRequestValueEventListener);
    }

    private void sendContactResults(Contact contact) {
        selectedContact = contact;
        presenter.onContactResults(selectedContact);
    }

    private void sendPrayerRequestResults(PrayerRequest request) {
        selectedPrayerRequest = request;
        presenter.onPrayerRequestResults(selectedPrayerRequest);
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
        contactsRef.removeEventListener(contactValueEventListener);
        prayerRequestsRef.removeEventListener(prayerRequestValueEventListener);
    }
}
