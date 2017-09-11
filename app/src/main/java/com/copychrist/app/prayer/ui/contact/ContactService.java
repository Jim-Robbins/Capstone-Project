package com.copychrist.app.prayer.ui.contact;


import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 9/8/17.
 *
 */

public class ContactService {
    private DatabaseReference contactsRef;
    private ValueEventListener contactDeleteSingleEventListener;
    private ChildEventListener contactChildEventListener;

    private DatabaseReference prayerRequestsRef;
    private ValueEventListener prayerRequestsValueEventListener;

    private ContactContract.Presenter presenter;
    private Contact selectedContact;
    private boolean showActiveRequests;

    public ContactService(FirebaseDatabase database) {
        contactsRef = database.getReference(Contact.DB_NAME);
        prayerRequestsRef = database.getReference(PrayerRequest.DB_NAME);

        contactChildEventListener = new ChildEventListener() {
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(selectedContact != null) {
                    sendContactUpdate(selectedContact);
                }
                sendDataResultMessage(R.string.data_value_changed);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                sendDataResultMessage(R.string.data_value_deleted);
                presenter.onContactDeleteCompleted();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }

            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
        };

        contactDeleteSingleEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if(dataSnapshot.getValue() == null) {
                    deleteContactConfirmed(selectedContact.getKey());
                    selectedContact = null;
//                } else {
//                    sendDataResultMessage("Cannot delete a contact that has requests assigned to it");
//                }
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        };

        // initialize Prayer request event listeners
        prayerRequestsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PrayerRequest> results = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    PrayerRequest prayerRequest = snapshot.getValue(PrayerRequest.class);
                    if (prayerRequest != null) {
                        prayerRequest.setKey(snapshot.getKey());
                        if(showActiveRequests && prayerRequest.getAnswered() == null) {
                            // Create a list of active requests
                            results.add(prayerRequest);
                        } else if (!showActiveRequests && prayerRequest.getAnswered() != null) {
                            // Create a list of archived requests
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
    }

    public void getContact(ContactContract.Presenter presenter,
                           Contact contact,
                           boolean showActiveRequests) {

        Timber.d("getContact() called with: contactKey = [" + contact.getKey() + "]");
        this.showActiveRequests = showActiveRequests;
        this.presenter = presenter;
        this.selectedContact = contact;
        contactsRef.child(contact.getKey()).addChildEventListener(contactChildEventListener);

        prayerRequestsRef.orderByChild(PrayerRequest.CHILD_CONTACT_KEY)
                .startAt(contact.getKey())
                .endAt(contact.getKey())
                .addValueEventListener(prayerRequestsValueEventListener);
    }

    private void sendPrayerRequestResults(List<PrayerRequest> prayerRequests) {
        if(prayerRequests == null) return;

        Timber.d("sendPrayerRequestResults() called with: prayerRequests = [" + prayerRequests + "]");
        presenter.onPrayerRequestResults(prayerRequests);
    }

    void saveContact(Contact contact) {
        if (contact != null && contact.getKey() == null) {
            contactsRef.push().setValue(contact);
        } else {
            contactsRef.child(contact.getKey()).setValue(contact);
        }
    }

    void deleteContact(Contact contact) {
        this.selectedContact = contact;
        contactsRef.orderByChild(PrayerRequest.CHILD_CONTACT)
                .startAt(contact.getKey())
                .endAt(contact.getKey())
                .addListenerForSingleValueEvent(contactDeleteSingleEventListener);
    }

    private void deleteContactConfirmed(String contactKey) {
        contactsRef.child(contactKey).removeValue();
        //Todo: update the sort orders
    }

    private void sendDataResultMessage(String message) {
        presenter.onDataResultMessage(message);
    }

    private void sendDataResultMessage(int messageResId) {
        presenter.onDataResultMessage(messageResId);
    }

    private void sendContactUpdate(Contact contact) {
        presenter.onContactUpdated(contact);
    }

    void destroy() {
        contactsRef.removeEventListener(contactChildEventListener);
        prayerRequestsRef.removeEventListener(prayerRequestsValueEventListener);
        prayerRequestsRef.removeEventListener(contactDeleteSingleEventListener);
    }
}
