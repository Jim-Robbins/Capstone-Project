package com.copychrist.app.prayer.ui.contact;


import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.ui.BaseService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;

/**
 * Created by jim on 9/8/17.
 */

public class ContactService implements BaseService<Contact, ContactContract.Presenter> {
    private static final String TAG = "ContactService";

    FirebaseDatabase database;
    DatabaseReference contactsRef;
    DatabaseReference prayerRequestsRef;
    ValueEventListener contactValueEventListener;
    ValueEventListener contactDeleteSingleEventListener;
    ChildEventListener contactChildEventListener;

    private ContactContract.Presenter presenter;
    private Contact selectedContact;

    public ContactService(FirebaseDatabase database) {
        this.database = database;
        contactsRef = database.getReference(Contact.DB_NAME);
        prayerRequestsRef = database.getReference(PrayerRequest.DB_NAME);

        contactValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                contact.setKey(dataSnapshot.getKey());
                sendContactResults(contact);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }
        };

        contactChildEventListener = new ChildEventListener() {
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                sendDataResultMessage("Value successfully changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                sendDataResultMessage("Value successfully deleted");
                presenter.onDeleteCompleted();
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
                if(dataSnapshot.getValue() == null) {
                    deleteConfirmed(selectedContact.getKey());
                    selectedContact = null;
                } else {
                    sendDataResultMessage("Cannot delete a contact that has requests assigned to it");
                }
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        };
    }

    @Override
    public void getValues(ContactContract.Presenter presenter) {
        Timber.d("getValues()");
    }

    public void getValue(ContactContract.Presenter presenter, String contactKey) {
        Timber.d("getValue() called with: contactKey = [" + contactKey + "]");
        this.presenter = presenter;
        contactsRef.child(contactKey).addValueEventListener(contactValueEventListener);
        contactsRef.child(contactKey).addChildEventListener(contactChildEventListener);
    }

    @Override
    public void saveValue(Contact contact) {
        if (contact == null) {
            contactsRef.push().setValue(contact);
        } else {
            contactsRef.child(contact.getKey()).setValue(contact);
        }
    }

    protected void deleteConfirmed(String contactKey) {
        contactsRef.child(contactKey).removeValue();
        //Todo: update the sort orders
    }

    @Override
    public void deleteValue() {
        prayerRequestsRef.orderByChild(PrayerRequest.CONTACT_KEY)
                .startAt(selectedContact.getKey())
                .endAt(selectedContact.getKey())
                .addListenerForSingleValueEvent(contactDeleteSingleEventListener);
    }

    private void sendContactResults(Contact result) {
        selectedContact = result;
        presenter.onGetContactResult(selectedContact);
    }



    private void sendDataResultMessage(String message) {
        presenter.onDataResultMessage(message);
    }

    @Override
    public void destroy() {
        contactsRef.removeEventListener(contactValueEventListener);
        contactsRef.removeEventListener(contactChildEventListener);
    }
}
