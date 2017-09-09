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

/**
 * Created by jim on 9/7/17.
 *
 */

public class ContactGroupService implements BaseService<ContactGroup, ContactGroupContract.View> {
    private static final String TAG = "ContactGroupService";

    FirebaseDatabase database;
    DatabaseReference contactGroupsRef;
    DatabaseReference contactsRef;
    Query queryRef;
    ValueEventListener cgValueEventListener;
    ValueEventListener cgDeleteValueEventListener;
    ChildEventListener cgChildEventListener;
    ChildEventListener qryChildEventListener;

    public ContactGroup selectedContactGroup;

    public ContactGroupService(FirebaseDatabase database) {
        this.database = database;
        contactGroupsRef = database.getReference(ContactGroup.DB_NAME);
        contactsRef = database.getReference(Contact.DB_NAME);
    }

    @Override
    public void getValues(final ContactGroupContract.View contactGroupView) {
        cgValueEventListener = new ValueEventListener() {
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
                contactGroupView.showContactGroupsTabs(results, selectedContactGroup);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                contactGroupView.showDatabaseResultMessage(databaseError.getMessage());
            }
        };

        //Todo:extract strings
        cgChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                contactGroupView.showDatabaseResultMessage("Value successfully changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                contactGroupView.showDatabaseResultMessage("Value successfully deleted");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                contactGroupView.showDatabaseResultMessage(databaseError.getMessage());
            }
        };

        qryChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                contactGroupView.showDatabaseResultMessage("Value successfully added");
                selectedContactGroup = dataSnapshot.getValue(ContactGroup.class);
                selectedContactGroup.setKey(dataSnapshot.getKey());
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
                contactGroupView.showDatabaseResultMessage(databaseError.getMessage());
            }
        };

        contactGroupsRef.orderByChild(ContactGroup.ORDER_BY).addValueEventListener(cgValueEventListener);
        contactGroupsRef.orderByChild(ContactGroup.ORDER_BY).addChildEventListener(cgChildEventListener);

        queryRef = contactGroupsRef.orderByChild(ContactGroup.CREATED).startAt(Utils.getCurrentTime());
        queryRef.addChildEventListener(qryChildEventListener);
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
    public void deleteValue(final ContactGroupContract.View contactGroupView,
                            final String contactGroupKey) {
        Query qry = contactGroupsRef.orderByChild(Contact.CONTACT_GROUP)
                                    .startAt(contactGroupKey).endAt(contactGroupKey);
        qry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    selectedContactGroup = null;
                    deleteConfirmed(contactGroupKey);
                } else {
                    contactGroupView.showDatabaseResultMessage("Cannot delete a group that has contacts assigned to it");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void deleteConfirmed(String contactGroupKey) {
        contactGroupsRef.child(contactGroupKey).removeValue();
        //Todo: update the sort orders
    }

    public void getContactValues(String contactGroupKey) {

    }

    @Override
    public void destroy() {
        contactGroupsRef.removeEventListener(cgValueEventListener);
        contactGroupsRef.removeEventListener(cgChildEventListener);
        contactGroupsRef = null;
    }
}
