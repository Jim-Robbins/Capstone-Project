package com.copychrist.app.prayer.ui.biblepassages;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.BiblePassageFinderSelectableAdapter;
import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.ui.prayerrequest.PrayerRequestContract;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 9/15/17.
 */

public class ViewBiblePassagesDialogFragment extends AppCompatDialogFragment
        implements BiblePassageFinderSelectableAdapter.BiblePassageAdapterListener {

    private PrayerRequestContract.Presenter presenter;

    private List<BiblePassage> biblePassages;
    private List<String> selectedPassageKeys = new ArrayList<>();
    private BiblePassageFinderSelectableAdapter BiblePassageFinderSelectableAdapter;

    View.OnClickListener onCreateClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Timber.d("Add a new request");
            addNewBiblePassage();
        }
    };

    public static ViewBiblePassagesDialogFragment newInstance(PrayerRequestContract.Presenter presenter,
                                                              List<BiblePassage> biblePassages) {
        ViewBiblePassagesDialogFragment frag = new ViewBiblePassagesDialogFragment();
        frag.presenter = presenter;
        frag.biblePassages = biblePassages;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_prayer_request_select_bible_passages, null);
        alertDialogBuilder.setView(dialogView);

        alertDialogBuilder.setTitle(R.string.dialog_bible_passage_title);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        RelativeLayout btnCreateNew = dialogView.findViewById(R.id.btn_add);
        btnCreateNew.setOnClickListener(onCreateClick);

        BiblePassageFinderSelectableAdapter = new BiblePassageFinderSelectableAdapter(dialogView.getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(BiblePassageFinderSelectableAdapter);


        BiblePassageFinderSelectableAdapter.setAdapterData(biblePassages);

        // Setup Confirm button
        alertDialogBuilder.setPositiveButton(R.string.btn_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveBiblePassagesToPrayerRequest();
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

    private void saveBiblePassagesToPrayerRequest() {
        List<Integer> selectedRows = BiblePassageFinderSelectableAdapter.getSelectedItems();
        if(selectedRows != null) {
            for (int position : selectedRows) {
                BiblePassage biblePassage = biblePassages.get(position);
                selectedPassageKeys.add(biblePassage.getPassageReference());
            }
        }
        presenter.onBiblePassagesAddedToPrayerRequest(selectedPassageKeys);
    }

    private void toggleSelection(int position) {
        BiblePassageFinderSelectableAdapter.toggleSelection(position);
    }

    private void addNewBiblePassage() {
        presenter.onBiblePassageAddNew();
    }

    @Override
    public void onRowClicked(int position) {
        toggleSelection(position);
    }

    @Override
    public void onRowLongClicked(int position) {
//        BiblePassage biblePassage = biblePassages.get(position);
//        //presenter.onBiblePassage(biblePassage);
    }

    public void setAdapter(List<BiblePassage> adapter) {
        this.biblePassages = adapter;
        BiblePassageFinderSelectableAdapter.setAdapterData(biblePassages);
    }
}
