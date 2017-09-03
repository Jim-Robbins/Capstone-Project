package com.copychrist.app.prayer.data.model;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public class PrayerList {
    @NonNull
    private final long id;

    @NonNull
    private final String name;

    @NonNull
    private final int order;

    @Nullable
    private final List<PrayerRequest> requests;

    public PrayerList(@NonNull String name) {
        this(-1, name, -1, null);
    }

    public PrayerList(@NonNull String name, @NonNull int order) {
        this(-1, name, order, null);
    }

    public PrayerList(@NonNull long id, @NonNull String name, @NonNull int order,
                      @Nullable List<PrayerRequest> requests) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.requests = requests;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<PrayerRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<PrayerRequest> requests) {
        this.requests.addAll(requests);
    }

    public int getOrder() {
        return order;
    }

    public static PrayerList getPrayerListFromCursor(@NonNull Cursor cursor) {
        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        return new PrayerList(id, name, -1, null);
    }

}
