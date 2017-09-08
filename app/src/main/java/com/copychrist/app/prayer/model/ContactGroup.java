package com.copychrist.app.prayer.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class ContactGroup {
    public static final String CREATED = "dateCreated";
    public static final String DB_NAME = "contactGroups";
    public static final String ORDER_BY = "order";

    @Exclude
    private String id;

    @NonNull
    private String name;

    @Nullable
    private String description;

    @NonNull
    private int order;

    @Nullable
    private List<Contact> contacts;

    private Long dateCreated;

    public ContactGroup() {}

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @NonNull
    public int getOrder() {
        return order;
    }

    @Nullable
    public List<Contact> getContacts() {
        return contacts;
    }

    @NonNull
    public Long getDateCreated() {
        if(dateCreated == null) {
            dateCreated = System.currentTimeMillis()/1000;
        }

        return dateCreated;
    }

    @Exclude
    public List<Contact> addContact(Contact contact) {
        contacts.add(contact);
        return contacts;
    }

    @Exclude
    public List<Contact> deleteContact(Contact contact) {
        contacts.remove(contact);
        return contacts;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    @Override
    public String toString() {
        return "ContactGroup { " +
                "   name = " + getName() + ", " +
                "   desc = " + getDescription() + ", " +
                "   order = " + getOrder() + ", " +
                "}";
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public void setOrder(@NonNull int order) {
        this.order = order;
    }

    public void setContacts(@Nullable List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

}
