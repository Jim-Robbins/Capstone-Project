package com.copychrist.app.prayer.model;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PrayerRequest extends RealmObject {
    @PrimaryKey @Required
    private int id;
    @Required
    private String title;
    private String description;
    private RealmList<BibleVerse> verses;
    private Date endDate;
    private String prayedForOn;
    private Date answered;
    private Date created = new Date();
    @Required
    private Contact contact;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RealmList<BibleVerse> getVerses() {
        return verses;
    }

    public void setVerses(RealmList<BibleVerse> verses) {
        this.verses = verses;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getAnswered() {
        return answered;
    }

    public void setAnswered(Date answered) {
        this.answered = answered;
    }

    public Date getCreated() {
        return created;
    }

    public List<String> getPrayedForOn() {
        return  Arrays.asList(prayedForOn.split(","));
    }

    public void setPrayedForOn(List<String> prayedForOnList) {
        this.prayedForOn = TextUtils.join(",", prayedForOnList);;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}