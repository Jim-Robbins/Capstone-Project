package com.copychrist.app.prayer.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@IgnoreExtraProperties
public class Contact  {
    public static String DB_NAME = "contacts";

    @NonNull
    private String id;

    @NonNull
    private String firstName;

    @Nullable
    private String lastName;

    @Nullable
    private String pictureUrl;

    @NonNull
    private String groupId;

    @Nullable
    private List<PrayerRequest> requestList = new ArrayList<>();

    public Contact() {
        // Default constructor required for calls to DataSnapshot.getValue()
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    @Nullable
    public List<PrayerRequest> getRequestList() {
        return requestList;
    }

    @Exclude
    public List<PrayerRequest> addPrayerRequest(PrayerRequest request) {
        requestList.add(request);
        return requestList;
    }

    @Exclude
    public List<PrayerRequest> deletePrayerRequest(PrayerRequest request) {
        requestList.remove(request);
        return requestList;
    }

    @Exclude
    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, firstName);
    }

    @Exclude
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

    private String createContactId() {
        Random rnd = new Random();
        return firstName.replaceAll("\\s","").toLowerCase() + "_" +
                lastName.replaceAll("\\s","").toLowerCase() + "_" +
                rnd.nextInt();
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    public void setPictureUrl(@Nullable String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}