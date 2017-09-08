package com.copychrist.app.prayer.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@IgnoreExtraProperties
public class BibleVerse {
    public static String DB_NAME = "bibleVerses";

    @NonNull
    private String book;

    @NonNull
    private int chapter;

    @Nullable
    private String verse;

    @Nullable
    private String text;

    @Nullable
    private String version;

    @Nullable
    private String apiUrl;

    public BibleVerse() {
        // Default constructor required for calls to DataSnapshot.getValue(BibleVerse.class)
    }

    public BibleVerse(String book, int chapter, String verse) {
        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
    }

    public String getBook() {
        return book;
    }

    public int getChapter() {
        return chapter;
    }

    public String getVerse() {
        return verse;
    }

    public String getText() {
        return text;
    }

    public String getVersion() {
        return version;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getPassage() {
        return book + " " + chapter + ":" + verse;
    }

    @Exclude
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BibleVerse bibleVerse = (BibleVerse) o;
        return Objects.equals(getPassage(), bibleVerse.getPassage());
    }

    @Exclude
    @Override
    public String toString() {
        return "BibleVerse {" +
                "book='" + book + '\'' +
                ", chapter='" + chapter + '\'' +
                ", verse='" + verse + '\'' +
                " ,text='" + text + '\'' +
                " ,version='" + version + '\'' +
                " ,apiUrl='" + apiUrl + '\'' +
                " ,passage='" + getPassage() + '\'' +
                "}";
    }

    @Exclude
    public static BibleVerse getBibleVerseFromPassage(@NonNull String passage) {
        Pattern pattern = Pattern.compile("([^\\s:]+)");
        Matcher matcher = pattern.matcher(passage);
        String book = "";
        String verse = "";
        int chapter = 0;

        if(matcher.find()) {
            book = pattern.matcher(passage).group(0);
            chapter = Integer.parseInt(pattern.matcher(passage).group(1));

            verse = pattern.matcher(passage).group(2);
        }

        return new BibleVerse(book, chapter, verse);
    }

    public void setBook(@NonNull String book) {
        this.book = book;
    }

    public void setChapter(@NonNull int chapter) {
        this.chapter = chapter;
    }

    public void setVerse(@Nullable String verse) {
        this.verse = verse;
    }

    public void setText(@Nullable String text) {
        this.text = text;
    }

    public void setVersion(@Nullable String version) {
        this.version = version;
    }

    public void setApiUrl(@Nullable String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
