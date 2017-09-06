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
    public static String DB_NAME = "contact";

    @NonNull
    private String id;

    @NonNull
    private String firstName;

    @Nullable
    private String lastName;

    @Nullable
    private String pictureUrl;

    @NonNull
    private String group;

    @Nullable
    private List<PrayerRequest> requestList;

    public Contact() {
        // Default constructor required for calls to DataSnapshot.getValue()
    }

    public Contact(@NonNull String group,
                   @NonNull String firstName, @Nullable String lastName) {
        this(group, firstName, lastName, null, null);
    }

    public Contact(@NonNull String group, @NonNull String firstName,
                   @Nullable String lastName, @Nullable String pictureUrl,
                   @Nullable List<PrayerRequest> requestList) {
        this.id = createContactId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.pictureUrl = pictureUrl;
        this.group = group;
        this.requestList = new ArrayList<>(requestList);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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
        return Objects.hash(id, group, firstName);
    }

    @Exclude
    @Override
    public String toString() {
        return "ContactEntry {"+
                "id='" + id + '\'' +
                ", group='" + group + '\'' +
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
}