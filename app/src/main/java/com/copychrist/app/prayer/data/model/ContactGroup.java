package com.copychrist.app.prayer.data.model;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public class ContactGroup {
    @NonNull
    private final long id;

    @NonNull
    private final String name;

    @Nullable
    private final String description;

    @NonNull
    private final int order;

    @Nullable
    private List<Contact> contacts;

    public ContactGroup(@NonNull long id, @NonNull String name) {
        this(id, name, null, 0, null);
    }

    public ContactGroup(@NonNull long id, @NonNull String name, @Nullable String description,
                        @NonNull int order, @Nullable List<Contact> contacts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.order = order;
        this.contacts = contacts;
    }
    @NonNull
    public long getId() {
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

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts.addAll(contacts);
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }

    public static ContactGroup getContactGroupFromCursor(Cursor cursor) {
        return new ContactGroup(cursor.getLong(0), cursor.getString(1));
    }
}
