package com.copychrist.app.prayer.data.model;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
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

    @Nullable
    private final List<PrayerRequest> requests;

    @NonNull
    private final long groupId;

    public Contact(@NonNull long groupId, @NonNull String firstName, @Nullable String lastName) {
        this(-1, groupId, firstName, lastName, null, null);
    }

    public Contact(@NonNull long groupId, @NonNull String firstName, @Nullable String lastName,
                   @Nullable String pictureUrl, @Nullable List<PrayerRequest> requests) {
        this(-1, groupId, firstName, lastName, pictureUrl, requests);
    }

    public Contact(@NonNull long id, @NonNull long groupId, @NonNull String firstName,
                   @Nullable String lastName, @Nullable String pictureUrl,
                   @Nullable List<PrayerRequest> requests) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pictureUrl = pictureUrl;
        this.groupId = groupId;
        this.requests = requests;
    }

    public Contact(@NonNull Cursor cursor) {
        this.id = cursor.getLong(0);
        this.firstName = cursor.getString(1);
        this.lastName = cursor.getString(2);
        this.pictureUrl = cursor.getString(3);
        this.groupId = cursor.getLong(4);
        this.requests = null;
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

    public List<PrayerRequest> getRequests() {
        return requests;
    }

    @Nullable
    public void setRequests(List<PrayerRequest> requests) {
        this.requests.addAll(requests);
    }

    public void addRequest(PrayerRequest request) {
        this.requests.add(request);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id) &&
                Objects.equals(groupId, contact.groupId) &&
                Objects.equals(firstName, contact.firstName);
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
