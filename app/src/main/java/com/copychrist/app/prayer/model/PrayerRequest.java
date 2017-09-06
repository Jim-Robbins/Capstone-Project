package com.copychrist.app.prayer.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class PrayerRequest {
    @NonNull
    private long id;

    @NonNull
    private String contact;

    @NonNull
    private String title;

    @Nullable
    private String description;

    @Nullable
    private Date endDate;

    @Nullable
    private List<BibleVerse> verses = new ArrayList<>();

    @Nullable
    private List<String> prayedForOn;

    @Nullable
    private Date answered;

    private Date created = new Date();

    public PrayerRequest() {}

    public PrayerRequest(@NonNull String contact, @NonNull String title ) {
        this(-1, contact, title, null, null, null, null, null);
    }

    public PrayerRequest(@NonNull long id, @NonNull String contact, @NonNull String title ) {
        this(id, contact, title, null, null, null, null, null);
    }

    public PrayerRequest(@NonNull long id, @NonNull String contact, @Nullable Date answered ) {
        this(id, contact, null, null, null, null, null, answered);
    }

    public PrayerRequest(@NonNull String contact, @NonNull String title,
                         @Nullable String description, @Nullable Date endDate ) {
        this(-1, contact, title, description, endDate, null, null, null);
    }

    public PrayerRequest(@NonNull String contact, @NonNull String title,
                         @Nullable String description, @Nullable Date endDate,
                         @Nullable List<BibleVerse> verses ) {
        this(-1, contact, title, description, endDate, verses, null, null);
    }

    public PrayerRequest(@NonNull long id, @NonNull String contact, @NonNull String title,
                         @Nullable String description, @Nullable Date endDate,
                         @Nullable List<BibleVerse> verses, @Nullable List<String> prayedForOn,
                         @Nullable Date answered ) {
        this.id = id;
        this.contact = contact;
        this.title = title;
        this.description = description;
        this.endDate = endDate;
        this.verses = new ArrayList<>(verses);
        this.prayedForOn = new ArrayList<>(prayedForOn);
        this.answered = answered;
    }

    @NonNull
    public long getId() {
        return id;
    }

    @NonNull
    public String getContact() {
        return contact;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public List<BibleVerse> getVerses() {
        return verses;
    }

    @Exclude
    public List<BibleVerse> addVerses(List<BibleVerse> verses) {
        this.verses.addAll(verses);
        return this.verses;
    }

    @Exclude
    public List<BibleVerse> addVerse(BibleVerse verse) {
        this.verses.add(verse);
        return this.verses;
    }

    @Exclude
    public List<BibleVerse> deleteVerse(BibleVerse verse) {
        this.verses.remove(verse);
        return this.verses;
    }

    @Nullable
    public Date getEndDate() {
        return endDate;
    }

    @Nullable
    public Date getAnswered() {
        return answered;
    }

    @NonNull
    public Date getCreated() {
        return created;
    }

    @Nullable
    public List<String> getPrayedForOn() {
        return prayedForOn;
    }

    @Exclude
    public List<String> addPrayedForOn(String prayedOn) {
        this.prayedForOn.add(prayedOn);
        return this.prayedForOn;
    }

    @Exclude
    public List<String> deletePrayedForOn(String prayedOn) {
        this.prayedForOn.remove(prayedOn);
        return this.prayedForOn;
    }
}