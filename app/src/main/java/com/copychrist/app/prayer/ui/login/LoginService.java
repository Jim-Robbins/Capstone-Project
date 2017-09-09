package com.copychrist.app.prayer.ui.login;

import com.copychrist.app.prayer.model.User;
import com.copychrist.app.prayer.ui.BaseService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by jim on 9/8/17.
 */

public class LoginService implements BaseService<User, LoginContract.Presenter> {
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

    @Override
    public void getValues(LoginContract.Presenter presenter) {

    }

    @Override
    public void saveValue(User model) {

    }

    @Override
    public void deleteValue() {

    }

    @Override
    public void destroy() {

    }
}
