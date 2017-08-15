package com.copychrist.app.prayer.model;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class BibleVerse extends RealmObject {
    @Required
    private String book;
    @Required
    private int chapter;
    @Required
    private String verse;
    @Required
    private String text;
    private String version;
    private String apiUrl;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getReference() {
        return book + " " + chapter + ":" + verse;
    }
}