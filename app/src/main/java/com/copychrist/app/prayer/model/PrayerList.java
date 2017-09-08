package com.copychrist.app.prayer.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class PrayerList {
    public static String DB_NAME = "prayerLists";

    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private int order;

    @Nullable
    private List<PrayerRequest> requests;

    public PrayerList() {}

    public PrayerList(@NonNull String name) {
        this(name, -1, null);
    }

    public PrayerList(@NonNull String name, @NonNull int order) {
        this(name, order, null);
    }

    public PrayerList(@NonNull String name, @NonNull int order,
                      @Nullable List<PrayerRequest> requests) {
        this.id = createPrayerListId(name);
        this.name = name;
        this.order = order;
        this.requests = new ArrayList<>(requests);
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
    public List<PrayerRequest> getRequests() {
        return requests;
    }

    @Exclude
    public List<PrayerRequest> addRequests(List<PrayerRequest> requests) {
        this.requests.addAll(requests);
        return requests;
    }

    @Exclude
    public List<PrayerRequest> addRequest(PrayerRequest request) {
        this.requests.add(request);
        return requests;
    }

    @Exclude
    public List<PrayerRequest> deleteRequest(PrayerRequest request) {
        this.requests.remove(request);
        return requests;
    }

    @NonNull
    public int getOrder() {
        return order;
    }

    @Exclude
    private String createPrayerListId(String name) {
        return name.replaceAll("\\s","").toLowerCase();
    }

}
