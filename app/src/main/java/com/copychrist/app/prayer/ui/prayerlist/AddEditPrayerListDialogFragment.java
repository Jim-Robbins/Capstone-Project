package com.copychrist.app.prayer.ui.prayerlist;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.PrayerList;

/**
 * Created by jim on 9/10/17.
 */

public class AddEditPrayerListDialogFragment extends AppCompatDialogFragment {
    protected PrayerList prayerList;
    protected int prayerListCounter;
    private EditText textPrayerListName;

    public interface AddEditPrayerListDialogListener {
        void onPrayerListSaveClick(PrayerList prayerList);
    }

    public static AddEditPrayerListDialogFragment newInstance(PrayerList prayerList, int tabCount) {
        AddEditPrayerListDialogFragment frag = new AddEditPrayerListDialogFragment();
        frag.prayerList = prayerList;
        frag.prayerListCounter = tabCount;

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Inflate custom view
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_save_prayer_list, null);
        alertDialogBuilder.setView(dialogView);

        textPrayerListName = dialogView.findViewById(R.id.txt_prayer_list_name);

        if(prayerList != null) {
            alertDialogBuilder.setTitle(R.string.dialog_prayer_list_title_edit);
            textPrayerListName.setText(prayerList.getName());
        } else {
            alertDialogBuilder.setTitle(R.string.dialog_prayer_list_title_add);
        }

        // Setup Confirm button
        alertDialogBuilder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (validateForm()) {
                    savePrayerList();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
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

    private void savePrayerList() {
        if(prayerList == null) {
            prayerList = new PrayerList();
            prayerList.setSortOrder(prayerListCounter);
        }
        prayerList.setName(textPrayerListName.getText().toString());
        ((AddEditPrayerListDialogListener) getActivity()).onPrayerListSaveClick(prayerList);
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(textPrayerListName.getText().toString())) {
            textPrayerListName.setError(getString(R.string.form_error));
            result = false;
        }
        return result;
    }
}

