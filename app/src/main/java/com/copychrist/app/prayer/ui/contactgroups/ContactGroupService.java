package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;
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
 * Created by jim on 9/7/17.
 *
 */

public class ContactGroupService {
    private final DatabaseReference contactGroupsRef;
    private final DatabaseReference contactsRef;
    private final DatabaseReference prayerRequestsRef;

    private Query contactGroupsQuery;
    private final ValueEventListener contactGroupDataListener;
    private final ValueEventListener contactGroupDeleteSingleEventListener;
    private final ChildEventListener contactGroupEditDeleteChildEventListener;
    private final ChildEventListener queryAddContactGroupChildEventListener;

    private final ValueEventListener contactValueEventListener;
    private final ChildEventListener contactChildEventListener;

    private final ValueEventListener prayerRequestsValueEventListener;

    private ContactGroup selectedContactGroup;
    private ContactGroupContract.Presenter presenter;

    public ContactGroupService(FirebaseDatabase database, FirebaseUser currentUser) {
        Timber.d("ContactGroupService() called with: database = [" + database + "]");

        contactGroupsRef = database.getReference(ContactGroup.DB_NAME).child(currentUser.getUid());
        contactsRef = database.getReference(Contact.DB_NAME).child(currentUser.getUid());
        prayerRequestsRef = database.getReference(PrayerRequest.DB_NAME).child(currentUser.getUid());

        // initialize Contact Group event listeners
        contactGroupDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ContactGroup> results = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ContactGroup tabContactGroup = snapshot.getValue(ContactGroup.class);
                    if (tabContactGroup != null) {
                        tabContactGroup.setKey(snapshot.getKey());
                        results.add(tabContactGroup);
                        if (selectedContactGroup == null) {
                            // If select group is not set, default to first entry
                            selectedContactGroup = tabContactGroup;
                        }
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
                    deleteContactGroupConfirmed(selectedContactGroup.getKey());
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
                    if (contact != null) {
                        contact.setKey(snapshot.getKey());
                        results.add(contact);
                    }
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

        // initialize Prayer request event listeners
        prayerRequestsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PrayerRequest> results = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    PrayerRequest prayerRequest = snapshot.getValue(PrayerRequest.class);
                    if (prayerRequest != null) {
                        prayerRequest.setKey(snapshot.getKey());
                        if(prayerRequest.getContact().getContactGroupKey().equalsIgnoreCase(selectedContactGroup.getKey())) {
                            // Create a list of active requests
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

    public void setPresenter(ContactGroupContract.Presenter contactGroupPresenter) {
        presenter = contactGroupPresenter;
    }

    public void getContactGroupValues() {
        Timber.d("getContactGroupValues()");

        contactGroupsRef.orderByChild(ContactGroup.CHILD_SORT_ORDER).addValueEventListener(contactGroupDataListener);
        contactGroupsRef.orderByChild(ContactGroup.CHILD_SORT_ORDER).addChildEventListener(contactGroupEditDeleteChildEventListener);

        contactGroupsQuery = contactGroupsRef.orderByChild(ContactGroup.CHILD_DATE_CREATED).startAt(Utils.getCurrentTime());
        contactGroupsQuery.addChildEventListener(queryAddContactGroupChildEventListener);
    }

    public void saveContactGroupValue(final ContactGroup contactGroup) {
        if(contactGroup.getKey() == null) {
            // Add Contact Group
            contactGroupsRef.push().setValue(contactGroup);
        } else {
            // Edit Contact Group
            contactGroupsRef.child(contactGroup.getKey()).setValue(contactGroup);
        }
    }

    public void deleteContactGroup() {
        Query qry = contactGroupsRef.child(Contact.CHILD_CONTACT_GROUP)
                                    .orderByChild(Contact.CHILD_CONTACT_GROUP)
                                    .startAt(selectedContactGroup.getKey())
                                    .endAt(selectedContactGroup.getKey());
        qry.addListenerForSingleValueEvent(contactGroupDeleteSingleEventListener);
    }

    private void deleteContactGroupConfirmed(String contactGroupKey) {
        contactGroupsRef.child(contactGroupKey).removeValue();
        //Todo: update the sort orders
    }

    public void deleteContact(String contactKey) {
        contactsRef.child(contactKey).removeValue();
    }

    public void getContactValues(ContactGroup contactGroup) {
        Timber.d("getContactValues() called with: contactGroup = [" + contactGroup + "]");
        selectedContactGroup = contactGroup;
        contactsRef.removeEventListener(contactValueEventListener);
        contactsRef.orderByChild(Contact.CHILD_CONTACT_GROUP_KEY)
                .startAt(contactGroup.getKey())
                .endAt(contactGroup.getKey())
                .addValueEventListener(contactValueEventListener);

        // Have to get all requests and then filter them as they come in
        prayerRequestsRef.removeEventListener(contactValueEventListener);
        prayerRequestsRef.orderByChild(PrayerRequest.CHILD_ANSWERED).addValueEventListener(prayerRequestsValueEventListener);
    }

    public void saveContactValue(Contact contact) {
        Timber.d("saveContactValue() called with: contact = [" + contact + "]");
        contactsRef.removeEventListener(contactChildEventListener);
        contactsRef.orderByChild(Contact.CHILD_DATE_CREATED)
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

    private void sendDataResultMessage(int messageResId) {
        presenter.onDataResultMessage(messageResId);
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

    private void sendPrayerRequestResults(List<PrayerRequest> prayerRequests) {
        if(prayerRequests == null) return;
        presenter.onPrayerRequestResults(prayerRequests);
    }

    public void destroy() {
        contactGroupsRef.removeEventListener(contactGroupDataListener);
        contactGroupsRef.removeEventListener(contactGroupEditDeleteChildEventListener);
        if(contactGroupsQuery != null) contactGroupsQuery.removeEventListener(queryAddContactGroupChildEventListener);
        contactsRef.removeEventListener(contactValueEventListener);
        contactsRef.removeEventListener(contactChildEventListener);
    }
}
