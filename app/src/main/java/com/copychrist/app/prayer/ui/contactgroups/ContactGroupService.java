package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;
import com.copychrist.app.prayer.ui.BaseService;
import com.copychrist.app.prayer.util.Utils;
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
 * Created by jim on 9/7/17.
 *
 */

public class ContactGroupService implements BaseService<ContactGroup, ContactGroupContract.Presenter> {
    private static final String TAG = "ContactGroupService";

    private final DatabaseReference contactGroupsRef;
    private final DatabaseReference contactsRef;
    private Query contactGroupsQuery;
    private Query contactsQuery;
    private final ValueEventListener contactGroupDataListener;
    private final ValueEventListener contactGroupDeleteSingleEventListener;
    private final ChildEventListener contactGroupEditDeleteChildEventListener;
    private final ChildEventListener queryAddContactGroupChildEventListener;

    private final ValueEventListener contactValueEventListener;
    private final ChildEventListener contactChildEventListener;

    private ContactGroup selectedContactGroup;
    private ContactGroupContract.Presenter presenter;

    public ContactGroupService(FirebaseDatabase database) {
        Timber.d("ContactGroupService() called with: database = [" + database + "]");
        contactGroupsRef = database.getReference(ContactGroup.DB_NAME);
        contactsRef = database.getReference(Contact.DB_NAME);

        // initialize Contact Group event listeners
        contactGroupDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ContactGroup> results = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ContactGroup tabContactGroup = snapshot.getValue(ContactGroup.class);
                    tabContactGroup.setKey(snapshot.getKey());
                    results.add(tabContactGroup);
                    if(selectedContactGroup == null) {
                        // If select group is not set, default to first entry
                        selectedContactGroup = tabContactGroup;
                    }
                }
                sendContactGroupResults(results);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }
        };

        contactGroupDeleteSingleEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    deleteConfirmed(selectedContactGroup.getKey());
                    selectedContactGroup = null;
                } else {
                    sendDataResultMessage("Cannot delete a group that has contacts assigned to it");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        contactGroupEditDeleteChildEventListener = new ChildEventListener() {
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                sendDataResultMessage("Value successfully changed");
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                sendDataResultMessage("Value successfully deleted");
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }

            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
        };

        queryAddContactGroupChildEventListener = new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                sendDataResultMessage("Contact Group successfully added");
                selectedContactGroup = dataSnapshot.getValue(ContactGroup.class);
                selectedContactGroup.setKey(dataSnapshot.getKey());
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
        };

        // initialize Contact event listeners
        contactValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Contact> results = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Contact contact = snapshot.getValue(Contact.class);
                    contact.setKey(snapshot.getKey());
                    results.add(contact);
                }
                sendContactResults(results);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }
        };

        contactChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Timber.d(dataSnapshot.toString());
                sendDataResultMessage("Contact successfully added");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                sendDataResultMessage(databaseError.getMessage());
            }
        };
    }

    @Override
    public void getValues(ContactGroupContract.Presenter presenter) {
        Timber.d("getValues()");

        this.presenter = presenter;

        contactGroupsRef.orderByChild(ContactGroup.ORDER_BY).addValueEventListener(contactGroupDataListener);
        contactGroupsRef.orderByChild(ContactGroup.ORDER_BY).addChildEventListener(contactGroupEditDeleteChildEventListener);

        contactGroupsQuery = contactGroupsRef.orderByChild(ContactGroup.CREATED).startAt(Utils.getCurrentTime());
        contactGroupsQuery.addChildEventListener(queryAddContactGroupChildEventListener);
    }

    @Override
    public void saveValue(final ContactGroup contactGroup) {
        if(contactGroup.getKey() == null) {
            // Add Contact Group
            contactGroupsRef.push().setValue(contactGroup);
        } else {
            // Edit Contact Group
            contactGroupsRef.child(contactGroup.getKey()).setValue(contactGroup);
        }
    }

    @Override
    public void deleteValue() {
        Query qry = contactGroupsRef.orderByChild(Contact.CONTACT_GROUP)
                                    .startAt(selectedContactGroup.getName())
                                    .endAt(selectedContactGroup.getName());
        qry.addListenerForSingleValueEvent(contactGroupDeleteSingleEventListener);
    }

    protected void deleteConfirmed(String contactGroupKey) {
        contactGroupsRef.child(contactGroupKey).removeValue();
        //Todo: update the sort orders
    }


    public void getContactValues(ContactGroup contactGroup) {
        Timber.d("getContactValues() called with: contactGroup = [" + contactGroup + "]");
        selectedContactGroup = contactGroup;
        contactsRef.orderByChild(Contact.CONTACT_GROUP)
                .startAt(contactGroup.getName())
                .endAt(contactGroup.getName())
                .addValueEventListener(contactValueEventListener);

    }

    public void saveContactValue(Contact contact) {
        Timber.d("saveContactValue() called with: contact = [" + contact + "]");
        contactsRef.removeEventListener(contactChildEventListener);
        contactsRef.orderByChild(Contact.CREATED)
                .startAt(Utils.getCurrentTime())
                .addChildEventListener(contactChildEventListener);

        if(contact.getKey() == null) {
            // Add Contact Group
            contactsRef.push().setValue(contact);
        } else {
            // Edit Contact Group
            contactsRef.child(contact.getKey()).setValue(contact);
        }
    }

    private void sendDataResultMessage(String message) {
        presenter.onDataResultMessage(message);
    }

    private void sendContactGroupResults(List<ContactGroup> results) {
        presenter.onContactGroupResults(results, selectedContactGroup);
    }

    private void sendContactResults(List<Contact> results) {
        presenter.onContactResults(results);
    }

    @Override
    public void destroy() {
        contactGroupsRef.removeEventListener(contactGroupDataListener);
        contactGroupsRef.removeEventListener(contactGroupEditDeleteChildEventListener);
        contactGroupsQuery.removeEventListener(queryAddContactGroupChildEventListener);
        contactsRef.removeEventListener(contactValueEventListener);
        contactsRef.removeEventListener(contactChildEventListener);
    }

}
