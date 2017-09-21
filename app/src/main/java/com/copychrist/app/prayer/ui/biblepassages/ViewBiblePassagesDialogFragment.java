package com.copychrist.app.prayer.ui.biblepassages;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.BiblePassageFinderSelectableAdapter;
import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.util.Utils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 9/15/17.
 */

public class ViewBiblePassagesDialogFragment extends AppCompatDialogFragment
        implements BiblePassageFinderSelectableAdapter.BiblePassageAdapterListener {

    private static final String SELECTED_ITEMS = "VBPDF_SELECTED_ITEMS";
    private static final String ARRAY_LIST = "VBPDF_ARRAY_LIST";

    private List<BiblePassage> biblePassages;
    private List<String> selectedPassageKeys = new ArrayList<>();
    private BiblePassageFinderSelectableAdapter biblePassageFinderSelectableAdpapter;
    private String selectedItemsToRestore;

    public interface ViewBiblePassagesDialogListener {
        void onBiblePassagesAddedToPrayerRequest(List<String> selectedPassageKeys);
        void onBiblePassageAddNew();
        //void onBiblePassageLongClick(BiblePassage biblePassage);
    }

    protected View.OnClickListener onCreateClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Timber.d("Add a new request");
            addNewBiblePassage();
        }
    };

    public static ViewBiblePassagesDialogFragment newInstance(List<BiblePassage> biblePassages) {
        ViewBiblePassagesDialogFragment frag = new ViewBiblePassagesDialogFragment();
        frag.biblePassages = biblePassages;
        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        selectedItemsToRestore = biblePassageFinderSelectableAdpapter.getSelectedItems().toString();
        outState.putString(SELECTED_ITEMS, selectedItemsToRestore);
        outState.putParcelableArrayList(ARRAY_LIST, new ArrayList<Parcelable>(biblePassages));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            selectedItemsToRestore = savedInstanceState.getString(SELECTED_ITEMS);
            selectedItemsToRestore = selectedItemsToRestore.substring(1, selectedItemsToRestore.length()-1).replaceAll(" ","");
            biblePassages = savedInstanceState.getParcelableArrayList(ARRAY_LIST);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_prayer_request_select_bible_passages, null);
        alertDialogBuilder.setView(dialogView);

        alertDialogBuilder.setTitle(R.string.dialog_bible_passage_title);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        RelativeLayout btnCreateNew = dialogView.findViewById(R.id.btn_add);
        btnCreateNew.setOnClickListener(onCreateClick);

        biblePassageFinderSelectableAdpapter = new BiblePassageFinderSelectableAdapter(dialogView.getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(biblePassageFinderSelectableAdpapter);


        biblePassageFinderSelectableAdpapter.setAdapterData(biblePassages);

        // If presenter is null we must be restoring from rotation
        if(!TextUtils.isEmpty(selectedItemsToRestore)) {
            List<Integer> positions = Utils.parseIntListString(selectedItemsToRestore);
            for (Integer position : positions) {
                toggleSelection(position);
            }
        }

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
        List<Integer> selectedRows = biblePassageFinderSelectableAdpapter.getSelectedItems();
        if(selectedRows != null) {
            for (int position : selectedRows) {
                BiblePassage biblePassage = biblePassages.get(position);
                selectedPassageKeys.add(biblePassage.getPassageReference());
            }
        }
        ((ViewBiblePassagesDialogListener) getActivity()).onBiblePassagesAddedToPrayerRequest(selectedPassageKeys);
    }

    private void toggleSelection(int position) {
        biblePassageFinderSelectableAdpapter.toggleSelection(position);
    }

    private void addNewBiblePassage() {
        ((ViewBiblePassagesDialogListener) getActivity()).onBiblePassageAddNew();
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
        biblePassageFinderSelectableAdpapter.setAdapterData(biblePassages);
    }
}
