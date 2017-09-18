package com.copychrist.app.prayer.ui.biblepassages;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.copychrist.app.prayer.BuildConfig;
import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.BibleBooksSpinnerArrayAdapter;
import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.ui.prayerrequest.PrayerRequestContract;
import com.faithcomesbyhearing.dbt.Dbt;
import com.faithcomesbyhearing.dbt.model.Book;

import java.util.List;

/**
 * Created by jim on 9/16/17.
 */

public class BiblePassageFinderDialogFragment extends AppCompatDialogFragment
        implements DbtService.DtbServiceListener {

    private static DbtService dbtService;
    private PrayerRequestContract.Presenter presenter;

    private Context context;
    private Spinner booksSpinner;
    private Spinner chaptersSpinner;
    private TextInputEditText txtVerses;
    private ImageButton btnFind;
    private TextView txtSearchResults;

    public static BiblePassageFinderDialogFragment newInstance(PrayerRequestContract.Presenter presenter) {
        BiblePassageFinderDialogFragment frag = new BiblePassageFinderDialogFragment();
        frag.presenter = presenter;

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dbt.setApiKey(BuildConfig.DBT_API_KEY);
        dbtService = new DbtService(this);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_bible_passage_finder, null);
        alertDialogBuilder.setView(dialogView);
        context = dialogView.getContext();

        alertDialogBuilder.setTitle(R.string.dialog_search_and_save_bible_passage_title);

        booksSpinner = dialogView.findViewById(R.id.spinner_books);
        chaptersSpinner = dialogView.findViewById(R.id.spinner_chapters);
        txtVerses = dialogView.findViewById(R.id.txt_verse_reference);
        txtSearchResults = dialogView.findViewById(R.id.txt_search_result);
        txtSearchResults.setMovementMethod(new ScrollingMovementMethod());
        btnFind = dialogView.findViewById(R.id.btn_find);

        // Setup Confirm button
        alertDialogBuilder.setPositiveButton(R.string.btn_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveBiblePassage();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

        });

        // Setup Cancel button
        alertDialogBuilder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

        });

        return alertDialogBuilder.create();
    }

    private void saveBiblePassage() {
        String book = ((Book) booksSpinner.getSelectedItem()).getBookId();
        String chapter = chaptersSpinner.getSelectedItem().toString();
        String verses = txtVerses.getText().toString();

        BiblePassage biblePassage = new BiblePassage(book, chapter, verses);
        biblePassage.setText(txtSearchResults.getText().toString());

        presenter.onBiblePassageSave(biblePassage);
    }

    @Override
    public void onGetVerseSuccess(String passage) {
        txtSearchResults.setText(passage);
    }

    @Override
    public void onGetDataFailure(String error) {
        txtSearchResults.setText(error);
    }

    @Override
    public void onServiceReady(final List<Book> bookList) {
        final int layoutRes = R.layout.item_spinner;
        final int textViewRes = R.id.text_spinner_item;

        BibleBooksSpinnerArrayAdapter bibleBooksAdapter = new BibleBooksSpinnerArrayAdapter(context,
                layoutRes, textViewRes, bookList);
        booksSpinner.setAdapter(bibleBooksAdapter);

        booksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Book book = bookList.get(pos);
                Integer[] items = createChaptersArray(book.getNumberOfChapters());
                ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(context, layoutRes, textViewRes, items);
                chaptersSpinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String book = ((Book) booksSpinner.getSelectedItem()).getBookId();
                String chapter = chaptersSpinner.getSelectedItem().toString();
                String verses = txtVerses.getText().toString();
                dbtService.getTextVerse(book, chapter, verses);
            }
        });
    }

    private Integer[] createChaptersArray(long size) {
        Integer[] chapters = new Integer[(int) size];
        for (int i = 0; i < size; i++) {
            chapters[i] = i+1;
        }
        return chapters;
    }
}
