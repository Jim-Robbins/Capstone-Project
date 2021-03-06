package com.copychrist.app.prayer.ui.contactgroups;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.ContactGroup;

/**
 * Created by jim on 8/16/17.
 *
 */

public class AddContactGroupDialogFragment extends AppCompatDialogFragment {
    protected ContactGroup contactGroup;
    protected int groupCounter;
    private EditText textGroupName, textGroupDesc;

    public interface AddContactGroupDialogListener {
        void onContactGroupSaveClick(ContactGroup contactGroup);
    }

    public static AddContactGroupDialogFragment newInstance(ContactGroup contactGroup, int tabCount) {
        AddContactGroupDialogFragment frag = new AddContactGroupDialogFragment();
        frag.contactGroup = contactGroup;
        frag.groupCounter = tabCount;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Inflate custom view
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_save_contact_group, null);
        alertDialogBuilder.setView(dialogView);

        textGroupName = dialogView.findViewById(R.id.txt_group_name);
        textGroupDesc = dialogView.findViewById(R.id.txt_group_desc);

        if(contactGroup != null) {
            alertDialogBuilder.setTitle(R.string.dialog_edit_contact_group_title);
            textGroupName.setText(contactGroup.getName());
            textGroupDesc.setText(contactGroup.getDescription());
        } else {
            alertDialogBuilder.setTitle(R.string.dialog_add_contact_group_title);
        }

        // Setup Confirm button
        alertDialogBuilder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (validateForm()) {
                    saveContactGroup();
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

    private void saveContactGroup() {
        if(contactGroup == null) {
            contactGroup = new ContactGroup();
            contactGroup.setSortOrder(groupCounter);
        }
        contactGroup.setName(textGroupName.getText().toString());
        contactGroup.setDescription(textGroupDesc.getText().toString());
        ((AddContactGroupDialogListener) getActivity()).onContactGroupSaveClick(contactGroup);
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(textGroupName.getText().toString())) {
            textGroupName.setError(getString(R.string.form_error));
            result = false;
        }
        return result;
    }
}
