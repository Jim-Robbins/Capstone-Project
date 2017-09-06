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

    public BibleVerse(@NonNull String book, @NonNull int chapter, @Nullable String verse) {
        this(book, chapter, verse, null, null, null);
    }

    public BibleVerse(@NonNull String book, @NonNull int chapter,
                      @Nullable String verse, @Nullable String text) {
        this(book, chapter, verse, text, null, null);
    }

    public BibleVerse(@NonNull String book, @NonNull int chapter, @Nullable String verse,
                      @Nullable String text, @Nullable String version, @Nullable String apiUrl) {
        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
        this.text = text;
        this.version = version;
        this.apiUrl = apiUrl;
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

        return new BibleVerse(book, chapter, verse, null, null, null);
    }
}
