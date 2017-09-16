package com.copychrist.app.prayer.ui.biblepassages;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.copychrist.app.prayer.BuildConfig;
import com.faithcomesbyhearing.dbt.Dbt;
import com.faithcomesbyhearing.dbt.callback.BookCallback;
import com.faithcomesbyhearing.dbt.callback.VerseCallback;
import com.faithcomesbyhearing.dbt.model.Book;
import com.faithcomesbyhearing.dbt.model.Verse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 9/15/17.
 */

public class DbtService {
    private final String API_VERSION = "2";
    private final String V2_ETEXT = API_VERSION + "ET";
    private final String OT = "O";
    private final String NT = "N";

    protected final List<String> BIBLE_VERSIONS = Arrays.asList("KJV","ASV","ESV","NKJ","NIV","NAS");
    protected final List<String> LANGUAGES = Arrays.asList("ENG");

    private List<Book> bookList;
    private String selectedLanguage;
    private String selectedVersion;
    private DtbServiceListener dtbServiceListener;

    public interface DtbServiceListener {
        void onGetVerseSuccess(String passage);
        void onGetDataFailure(String error);
        void onServiceReady(List<Book> bookList);
    }

    public DbtService(final DtbServiceListener dtbServiceListener) {
        this.dtbServiceListener = dtbServiceListener;
        Dbt.setApiKey(BuildConfig.DBT_API_KEY);
        selectedLanguage = LANGUAGES.get(0);
        selectedVersion = BIBLE_VERSIONS.get(0);
        getAllBibleBooks();

        /*  This will return all books, chapter and verse info
            getVersionInfo http://dbt.io/library/verseinfo?key=dc951bf3411b2f2d5c91ab14f260cf15&dam_id=ENGKJVN2ET&v=2
         */

        // http://dbt.io/text/searchgroup?key=dc951bf3411b2f2d5c91ab14f260cf15&dam_id=ENGKJVN2ET&query=Seek+ye+first&v=2
    }

    private String getBaseDamId() {
        return getSelectedLanguage() + getSelectedVersion();
    }

    private void getAllBibleBooks() {
        BookCallback callback = new BookCallback() {
            @Override
            public void success(List<Book> books) {
                if (books.isEmpty()) return;
                bookList = new ArrayList(books);
                dtbServiceListener.onServiceReady(bookList);
            }

            @Override
            public void failure(Exception e) {
                String error = "Failed to find verse:" + e.getMessage();
                Timber.w(error);
                onError(error);
            }

        };

        Dbt.getLibraryBook(getBaseDamId(), callback);
    }

    public void getTextVerse(@NonNull String bookName, @Nullable String chapter, @Nullable String reference) {
        String startingVerse = null;
        String endingVerse = null;

        if(reference != null) {
            String[] verses = reference.split("-");
            startingVerse = verses[0];
            endingVerse = (verses.length > 1) ? verses[1] : null;
        }

        VerseCallback callback = new VerseCallback() {
            @Override
            public void success(List<Verse> verses) {
                if (verses.isEmpty()) return;
                String passage = "";
                for (Verse verse : verses) {
                    passage += "\t" + verse.getVerseId() + " " + verse.getVerseText().replaceAll("\t","");
                }

                dtbServiceListener.onGetVerseSuccess(passage);
            }

            @Override
            public void failure(Exception e) {
                String error = "Failed to find verse:" + e.getMessage().toString();
                onError(error);
            }
        };

        Book bookItem = findBookListItem(bookName, chapter);

        if(bookItem != null) {
            String dId = getBookDamId(bookItem);
            Dbt.getTextVerse(dId, bookName, chapter, startingVerse, endingVerse, callback);
        } else {
            Timber.w("Invalid reference");
        }
    }

    private String getBookDamId(Book bookItem) {
        int bookOrder = Integer.parseInt(bookItem.getBookOrder());
        if (bookOrder > 54) {
            return getBaseDamId() + NT + V2_ETEXT;
        } else {
            return getBaseDamId() + OT + V2_ETEXT;
        }
    }

    private Book findBookListItem(@NonNull String bookName, @Nullable String chapter) {
        for (Book book : bookList) {
            if (book.getBookName().equalsIgnoreCase(bookName) || book.getBookId().equalsIgnoreCase(bookName)) {
                if (book.getNumberOfChapters() >= Long.parseLong(chapter)) {
                    return book;
                } else {
                    Timber.d(book.getNumberOfChapters() + "<" + chapter);
                }
            }
        }
        return null;
    }

    public void searchText() {

    }

    protected String getSelectedLanguage() {
        return selectedLanguage;
    }
    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    protected String getSelectedVersion() {
        return selectedVersion;
    }
    public void setSelectedVersion(String selectedVersion) {
        this.selectedVersion = selectedVersion;
    }

    private void onError(final String message) {
        Timber.w(message);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                dtbServiceListener.onGetDataFailure(message);
//            }
//        });
    }
}
