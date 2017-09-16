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
    public static final String DB_NAME = "biblePassages";

    @Exclude
    private String key;

    @NonNull
    private String book;
    public static final String CHILD_BOOK = "book";

    @NonNull
    private String chapter;
    public static final String CHILD_CHAPTER = "chapter";

    @Nullable
    private String verse;
    public static final String CHILD_VERSE = "verse";

    @Nullable
    private String text;
    public static final String CHILD_TEXT = "text";

    @Nullable
    private String version;
    public static final String CHILD_VERSION = "version";

    @Nullable
    private String copyright;
    public static final String CHILD_COPY_RIGHT = "copyright";

    public BiblePassage() {
        // Default constructor required for calls to DataSnapshot.getContact(BiblePassage.class)
    }

    public BiblePassage(String book, String chapter, String verse) {
        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
        setKey();
    }

    /**
     * Auto-generated Firebase key
     * @return key [String] - Used to reference data value set for this group
     */
    @Exclude
    public String getKey() {
        return key;
    }
    /**
     * Not editable by user, stores reference to Firebase key
     */
    public void setKey() {
        this.key = book + chapter + ":" + verse;
    }

    public String getBook() {
        return book;
    }

    public String getChapter() {
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

    public String getCopyright() {
        return copyright;
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
                " ,copyright='" + copyright + '\'' +
                " ,passage='" + getPassageReference() + '\'' +
                "}";
    }

    @Exclude
    public static BiblePassage getBiblePassageFromReference(@NonNull String passage) {
        Pattern pattern = Pattern.compile("([^\\s:]+)");
        Matcher matcher = pattern.matcher(passage);
        String book = "";
        String verse = "";
        String chapter = "";

        if(matcher.find()) {
            book = pattern.matcher(passage).group(0);
            chapter = pattern.matcher(passage).group(1);
            verse = pattern.matcher(passage).group(2);
        }

        return new BiblePassage(book, chapter, verse);
    }

    public void setBook(@NonNull String book) {
        this.book = book;
    }

    public void setChapter(@NonNull String chapter) {
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

    public void setCopyright(@Nullable String copyright) {
        this.copyright = copyright;
    }
}
