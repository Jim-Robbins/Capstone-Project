package com.copychrist.app.prayer.ui.contactgroups;

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
    Query queryRef;
    ValueEventListener cgValueEventListener;
    ChildEventListener cgChildEventListener;
    ChildEventListener qryChildEventListener;

    public ContactGroupService(FirebaseDatabase database) {
        this.database = database;
        contactGroupsRef = database.getReference(ContactGroup.DB_NAME);
    }

    @Override
    public void saveValue(final ContactGroup contactGroup) {
        if(contactGroup.getId() == null) {
            // Add Contact Group
            contactGroupsRef.push().setValue(contactGroup);
        } else {
            // Edit Contact Group
            contactGroupsRef.child(contactGroup.getId()).setValue(contactGroup);
        }
    }

    @Override
    public void getValue(final ContactGroupContract.View contactGroupView,
                                       final ContactGroup contactGroup) {
        cgValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ContactGroup> results = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ContactGroup contactGroup = snapshot.getValue(ContactGroup.class);
                    contactGroup.setId(snapshot.getKey());
                    results.add(contactGroup);
                }
                contactGroupView.showContactGroupsTabs(results, contactGroup);
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
    public void deleteValue(String groupId) {
        contactGroupsRef.child(groupId).removeValue();
    }

    @Override
    public void destroy() {
        contactGroupsRef.removeEventListener(cgValueEventListener);
        contactGroupsRef.removeEventListener(cgChildEventListener);
        contactGroupsRef = null;
    }

}
