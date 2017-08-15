package com.copychrist.app.prayer.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Contact extends RealmObject {
    @Required
    private int id;
    @Required
    private String firstName;
    private String lastName;
    private String pictureUrl;
    private RealmList<PrayerRequest> requests;
    @Required
    private ContactGroup group;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public RealmList<PrayerRequest> getRequests() {
        return requests;
    }

    public void setRequests(RealmList<PrayerRequest> requests) {
        this.requests = requests;
    }

    public ContactGroup getGroup() {
        return group;
    }

    public void setGroup(ContactGroup group) {
        this.group = group;
    }
}
