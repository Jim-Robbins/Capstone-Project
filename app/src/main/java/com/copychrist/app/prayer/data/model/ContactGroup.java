package com.copychrist.app.prayer.data.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.copychrist.app.prayer.data.local.DatabaseContract;

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

    public ContactGroup(@NonNull long id, @NonNull String name, @Nullable String description,
                        @NonNull int order) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.order = order;
    }

    /**
     * Return a ContactGroup instance from the Cursor object
     * @param cursor
     * @return
     */
    public static ContactGroup getFrom(Cursor cursor) {
        long groupId = cursor.getLong(cursor.getColumnIndexOrThrow(
                DatabaseContract.ContactGroupEntry._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.ContactGroupEntry.COLUMN_NAME));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.ContactGroupEntry.COLUMN_DESC));
        int sort_order = cursor.getInt(cursor.getColumnIndexOrThrow(
                DatabaseContract.ContactGroupEntry.COLUMN_SORT_ORDER));
        return new ContactGroup(groupId, name, description, sort_order);
    }

    /**
     * Return a ContactGroup instance from ContentValues object
     * @param contentValues
     * @return
     */
    public static ContactGroup getFrom(ContentValues contentValues) {
        long groupId = contentValues.getAsLong(
                DatabaseContract.ContactGroupEntry._ID);
        String name = contentValues.getAsString(
                DatabaseContract.ContactGroupEntry.COLUMN_NAME);
        String description = contentValues.getAsString(
                DatabaseContract.ContactGroupEntry.COLUMN_DESC);
        int sort_order = contentValues.getAsInteger(
                DatabaseContract.ContactGroupEntry.COLUMN_SORT_ORDER);
        return new ContactGroup(groupId, name, description, sort_order);
    }

    public static ContentValues from(ContactGroup contactGroup) {
        ContentValues values = new ContentValues();
        if(contactGroup.getId() > 0)
            values.put(DatabaseContract.ContactGroupEntry._ID, contactGroup.getId());
        values.put(DatabaseContract.ContactGroupEntry.COLUMN_NAME, contactGroup.getName());
        values.put(DatabaseContract.ContactGroupEntry.COLUMN_DESC, contactGroup.getDesc());
        values.put(DatabaseContract.ContactGroupEntry.COLUMN_SORT_ORDER, contactGroup.getOrder());
        return values;
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