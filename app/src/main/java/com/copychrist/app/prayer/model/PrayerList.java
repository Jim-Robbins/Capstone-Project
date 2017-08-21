package com.copychrist.app.prayer.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PrayerList extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private String name;
    private RealmList<PrayerRequest> requests;
    private int order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<PrayerRequest> getRequests() {
        return requests;
    }

    public void setRequests(RealmList<PrayerRequest> requests) {
        this.requests = requests;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
