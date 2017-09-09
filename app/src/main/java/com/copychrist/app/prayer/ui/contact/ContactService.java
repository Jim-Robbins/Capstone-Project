package com.copychrist.app.prayer.ui.contact;


import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.ui.BaseService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by jim on 9/8/17.
 */

public class ContactService implements BaseService<Contact, ContactContract.View> {
    private static final String TAG = "ContactService";

    FirebaseDatabase database;
    DatabaseReference contactGroupsRef;
    Query queryRef;
    ValueEventListener contactValueEventListener;
    ChildEventListener contactChildEventListener;
    ChildEventListener contactQryChildEventListener;

    public ContactService(FirebaseDatabase database) {
        this.database = database;
        contactGroupsRef = database.getReference(Contact.DB_NAME);
    }

    @Override
    public void getValues(ContactContract.View view) {

    }

    @Override
    public void saveValue(Contact model) {

    }

    @Override
    public void deleteValue(ContactContract.View view, String id) {

    }

    @Override
    public void destroy() {

    }
}
