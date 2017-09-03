package com.copychrist.app.prayer.data.model;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PrayerRequest {
    @NonNull
    private final long id;

    @NonNull
    private final long contactId;

    @NonNull
    private final String title;

    @Nullable
    private final String description;

    @Nullable
    private final Date endDate;

    @Nullable
    private List<BibleVerse> verses = new ArrayList<>();

    @Nullable
    private String prayedForOn;

    @Nullable
    private final Date answered;

    private Date created = new Date();

    public PrayerRequest(@NonNull long contactId, @NonNull String title ) {
        this(-1, contactId, title, null, null, null, null, null);
    }

    public PrayerRequest(@NonNull long id, @NonNull long contactId, @NonNull String title ) {
        this(id, contactId, title, null, null, null, null, null);
    }

    public PrayerRequest(@NonNull long id, @NonNull long contactId, @Nullable Date answered ) {
        this(id, contactId, null, null, null, null, null, answered);
    }

    public PrayerRequest(@NonNull long contactId, @NonNull String title,
                         @Nullable String description, @Nullable Date endDate ) {
        this(-1, contactId, title, description, endDate, null, null, null);
    }

    public PrayerRequest(@NonNull long contactId, @NonNull String title,
                         @Nullable String description, @Nullable Date endDate,
                         @Nullable List<BibleVerse> verses ) {
        this(-1, contactId, title, description, endDate, verses, null, null);
    }

    public PrayerRequest(@NonNull long id, @NonNull long contactId, @NonNull String title,
                   @Nullable String description, @Nullable Date endDate,
                   @Nullable List<BibleVerse> verses, @Nullable String prayedForOn,
                   @Nullable Date answered ) {
        this.id = id;
        this.contactId = contactId;
        this.title = title;
        this.description = description;
        this.endDate = endDate;
        this.verses = verses;
        this.prayedForOn = prayedForOn;
        this.answered = answered;
    }

    public long getId() {
        return id;
    }

    public long getContactId() {
        return contactId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<BibleVerse> getVerses() {
        return verses;
    }

    public void setVerses(List<BibleVerse> verses) {
        this.verses.addAll(verses);
    }

    public void addVerse(BibleVerse verse) {
        this.verses.add(verse);
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getAnswered() {
        return answered;
    }

    public Date getCreated() {
        return created;
    }

    public List<String> getPrayedForOn() {
        return  Arrays.asList(prayedForOn.split(","));
    }

    public void setPrayedForOn(List<String> prayedForOnList) {
        this.prayedForOn = TextUtils.join(",", prayedForOnList);
    }

    public static PrayerRequest getPrayerRequestFromCursor(Cursor cursor) {
        return new PrayerRequest(cursor.getLong(0), 0, cursor.getString(1));
    }
}