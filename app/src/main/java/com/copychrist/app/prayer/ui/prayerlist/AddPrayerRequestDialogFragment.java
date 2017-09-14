package com.copychrist.app.prayer.ui.prayerlist;

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
import com.copychrist.app.prayer.adapter.PrayerListRequestAdapter;
import com.copychrist.app.prayer.model.PrayerListRequest;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by jim on 9/10/17.
 */

public class AddPrayerRequestDialogFragment extends AppCompatDialogFragment implements PrayerListRequestAdapter.PrayerListRequestAdapterListener {
    private List<PrayerListRequest> prayerListRequests;
    private List<String> selectedPrayerRequestKeys = new ArrayList<>();
    private PrayerListContract.Presenter presenter;
    private PrayerListRequestAdapter prayerRequestsListAdapter;

    public static AddPrayerRequestDialogFragment newAddInstance(PrayerListContract.Presenter presenter) {
        AddPrayerRequestDialogFragment frag = new AddPrayerRequestDialogFragment();
        frag.presenter = presenter;
        return frag;
    }

    @Override
    public void onIconClicked(int position) {
        toggleSelection(position);
    }

    @Override
    public void onRowClicked(int position) {
        toggleSelection(position);
    }

    @Override
    public void onRowLongClicked(int position) {
        PrayerListRequest prayerListRequest = prayerListRequests.get(position);
        presenter.onPrayerRequestEditClick(prayerListRequest.getPrayerRequest());
    }

    View.OnClickListener onCreateClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Timber.d("Add a new request");
            editPrayerRequest();
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // Inflate custom view
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_to_prayer_list, null);
        alertDialogBuilder.setView(dialogView);

        alertDialogBuilder.setTitle(R.string.dialog_prayer_list_add_request_title);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);
        RelativeLayout btnCreateNew = dialogView.findViewById(R.id.btn_create_request);
        btnCreateNew.setOnClickListener(onCreateClick);

        prayerRequestsListAdapter = new PrayerListRequestAdapter(this.getContext(), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(prayerRequestsListAdapter);

        prayerListRequests = presenter.getAllPrayerRequests();
        prayerRequestsListAdapter.setAdpaterData(prayerListRequests);

        // Setup Confirm button
        alertDialogBuilder.setPositiveButton(R.string.btn_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                savePrayerRequestsToList();
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

    private void savePrayerRequestsToList() {
        List<Integer> selectedRows = prayerRequestsListAdapter.getSelectedItems();
        for (int position : selectedRows) {
            PrayerListRequest prayerListRequest = prayerListRequests.get(position);
            selectedPrayerRequestKeys.add(prayerListRequest.getKey());
        }
        presenter.onPrayerRequestsAddToList(selectedPrayerRequestKeys);
    }

    private void toggleSelection(int position) {
        prayerRequestsListAdapter.toggleSelection(position);
    }

    private final void editPrayerRequest() {
        presenter.onPrayerRequestsAddNewRequest();
    }

    @Override
    public void onRemoveClicked(int position) {

    }

    @Override
    public void onArchiveClicked(int position) {

    }

    @Override
    public void onMoreClicked(int position) {

    }
}
