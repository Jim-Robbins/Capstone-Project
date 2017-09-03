package com.copychrist.app.prayer.data.model;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

public class BibleVerse {
    @NonNull
    private final String book;

    @NonNull
    private final int chapter;

    @Nullable
    private final String verse;

    @Nullable
    private final String text;

    @Nullable
    private final String version;

    @Nullable
    private final String apiUrl;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BibleVerse bibleVerse = (BibleVerse) o;
        return Objects.equals(getPassage(), bibleVerse.getPassage());
    }

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

    public static BibleVerse getBibleVerseFromCursor(Cursor cursor) {
        return new BibleVerse(cursor.getString(0), cursor.getInt(1), cursor.getString(2));
    }
}
