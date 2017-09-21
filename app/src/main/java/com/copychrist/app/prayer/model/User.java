package com.copychrist.app.prayer.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by jim on 9/6/17.
 */

@IgnoreExtraProperties
public class User {
    public static String DB_NAME = "users";

    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getContact(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
