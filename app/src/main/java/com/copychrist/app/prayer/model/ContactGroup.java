package com.copychrist.app.prayer.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class ContactGroup {
    @NonNull
    private String id;

    @NonNull
    private String name;

    @Nullable
    private String description;

    @NonNull
    private int order;

    @Nullable
    private List<Contact> members;

    public ContactGroup() {}

    public ContactGroup(@NonNull String name, @Nullable String description,
                        @NonNull int order, @Nullable List<Contact> contacts) {
        this.id = name.replaceAll("\\s","").toLowerCase();
        this.name = name;
        this.description = description;
        this.order = order;
        this.members = new ArrayList<>(contacts);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getDesc() {
        return description;
    }

    @NonNull
    public int getOrder() {
        return order;
    }

    @Nullable
    public List<Contact> getMembers() {
        return members;
    }

    @Exclude
    public List<Contact> addMember(Contact contact) {
        members.add(contact);
        return members;
    }

    @Exclude
    public List<Contact> deleteMember(Contact contact) {
        members.remove(contact);
        return members;
    }

    @Exclude
    @Override
    public String toString() {
        return "ContactGroup { " +
                "   id = " + getId() + ", " +
                "   name = " + getName() + ", " +
                "   desc = " + getDesc() + ", " +
                "   order = " + getOrder() + ", " +
                "}";
    }
}
