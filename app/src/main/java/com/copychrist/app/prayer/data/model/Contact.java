package com.copychrist.app.prayer.data.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.copychrist.app.prayer.data.local.DatabaseContract;

import java.util.Objects;

public class Contact  {
    @NonNull
    private final long id;

    @NonNull
    private final String firstName;

    @Nullable
    private final String lastName;

    @Nullable
    private final String pictureUrl;

    @NonNull
    private final long groupId;

    public Contact(@NonNull long id, @NonNull long groupId,
                   @NonNull String firstName, @Nullable String lastName) {
        this(id, groupId, firstName, lastName, null);
    }

    public Contact(@NonNull long id, @NonNull long groupId, @NonNull String firstName,
                   @Nullable String lastName, @Nullable String pictureUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pictureUrl = pictureUrl;
        this.groupId = groupId;
    }

    public static Contact getFrom(@NonNull Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(
                DatabaseContract.ContactEntry._ID));
        long groupId = cursor.getLong(cursor.getColumnIndexOrThrow(
                DatabaseContract.ContactEntry.COLUMN_GROUP));
        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.ContactEntry.COLUMN_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.ContactEntry.COLUMN_LAST_NAME));
        String pictureUrl = cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.ContactEntry.COLUMN_PICTURE_URL));
        return new Contact(id, groupId, firstName, lastName, pictureUrl);
    }

    /**
     * Return a ContactGroup instance from ContentValues object
     * @param contentValues
     * @return
     */
    public static Contact getFrom(ContentValues contentValues) {
        long id = contentValues.getAsLong(
                DatabaseContract.ContactEntry._ID);
        long groupId = contentValues.getAsLong(
                DatabaseContract.ContactEntry.COLUMN_GROUP);
        String firstName = contentValues.getAsString(
                DatabaseContract.ContactEntry.COLUMN_FIRST_NAME);
        String lastName = contentValues.getAsString(
                DatabaseContract.ContactEntry.COLUMN_LAST_NAME);
        String pictureUrl = contentValues.getAsString(
                DatabaseContract.ContactEntry.COLUMN_PICTURE_URL);
        return new Contact(id, groupId, firstName, lastName, pictureUrl);
    }

    public static ContentValues from(Contact contact) {
        ContentValues values = new ContentValues();
        if(contact.getId() > 0)
            values.put(DatabaseContract.ContactEntry._ID, contact.getId());
        values.put(DatabaseContract.ContactEntry.COLUMN_GROUP, contact.getGroupId());
        values.put(DatabaseContract.ContactEntry.COLUMN_FIRST_NAME, contact.getFirstName());
        values.put(DatabaseContract.ContactEntry.COLUMN_LAST_NAME, contact.getLastName());
        values.put(DatabaseContract.ContactEntry.COLUMN_PICTURE_URL, contact.getPictureUrl());
        return values;
    }
    @NonNull
    public long getId() {
        return id;
    }

    @NonNull
    public long getGroupId() {
        return groupId;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @Nullable
    public String getLastName() {
        return lastName;
    }

    @Nullable
    public String getPictureUrl() {
        return pictureUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, firstName);
    }

    @Override
    public String toString() {
        return "ContactEntry {"+
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", firstName='" + firstName + '\'' +
                " ,lastName='" + lastName + '\'' +
                " ,pictureUrl=" + pictureUrl + '\'' +
                "}";
    }
}
