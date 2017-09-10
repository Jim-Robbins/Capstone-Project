package com.copychrist.app.prayer.ui.login;

import com.copychrist.app.prayer.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by jim on 9/8/17.
 */

public class LoginService {
    FirebaseDatabase database;
    DatabaseReference usersGroupsRef;
    Query queryRef;
    ValueEventListener usersValueEventListener;
    ChildEventListener usersChildEventListener;
    ChildEventListener usersQryChildEventListener;

    public LoginService(FirebaseDatabase database) {
        this.database = database;
        usersGroupsRef = database.getReference(User.DB_NAME);
    }

    public void getValues(LoginContract.Presenter presenter) {

    }

    public void saveValue(User model) {

    }

    public void deleteValue() {

    }

    public void destroy() {

    }
}
