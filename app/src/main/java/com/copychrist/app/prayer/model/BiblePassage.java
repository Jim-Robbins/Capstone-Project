package com.copychrist.app.prayer.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@IgnoreExtraProperties
public class BiblePassage {
    public static String DB_NAME = "biblePassages";

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

    public BiblePassage() {
        // Default constructor required for calls to DataSnapshot.getContact(BiblePassage.class)
    }

    public BiblePassage(String book, int chapter, String verse) {
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

    public String getPassageReference() {
        return book + " " + chapter + ":" + verse;
    }

    @Exclude
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiblePassage biblePassage = (BiblePassage) o;
        return Objects.equals(getPassageReference(), biblePassage.getPassageReference());
    }

    @Exclude
    @Override
    public String toString() {
        return "BiblePassage {" +
                "book='" + book + '\'' +
                ", chapter='" + chapter + '\'' +
                ", verse='" + verse + '\'' +
                " ,text='" + text + '\'' +
                " ,version='" + version + '\'' +
                " ,apiUrl='" + apiUrl + '\'' +
                " ,passage='" + getPassageReference() + '\'' +
                "}";
    }

    @Exclude
    public static BiblePassage getBiblePassageFromReference(@NonNull String passage) {
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

        return new BiblePassage(book, chapter, verse);
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
