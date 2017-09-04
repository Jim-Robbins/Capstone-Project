package com.copychrist.app.prayer.ui.contactgroups;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.EditText;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.data.model.ContactGroup;

/**
 * Created by jim on 8/16/17.
 */

public class AddContactGroupDialogFragment extends AppCompatDialogFragment {
    public static final int REQUEST_ADD_TASK = 1;
    protected ContactGroup contactGroup;
    protected ContactGroupContract.Presenter presenter;
    private long contactGroupId = -1;
    private int sortOrder = -1;

    private EditText textGroupName, textGroupDesc;

    public static AddContactGroupDialogFragment newInstance(ContactGroup contactGroup,
                                                            ContactGroupContract.Presenter contactsPresenter) {
        AddContactGroupDialogFragment frag = new AddContactGroupDialogFragment();
        frag.contactGroup = contactGroup;
        frag.presenter = contactsPresenter;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Inflate custom view
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_save_contact_group, null);
        alertDialogBuilder.setView(dialogView);

        textGroupName = dialogView.findViewById(R.id.text_group_name);
        textGroupDesc = dialogView.findViewById(R.id.text_group_desc);

        if(contactGroup != null) {
            alertDialogBuilder.setTitle(R.string.dialog_edit_contact_group_title);
            textGroupName.setText(contactGroup.getName());
            textGroupDesc.setText(contactGroup.getDesc());
            contactGroupId = contactGroup.getId();
            sortOrder = contactGroup.getOrder();
        } else {
            alertDialogBuilder.setTitle(R.string.dialog_add_contact_group_title);
        }

        // Setup Confirm button
        alertDialogBuilder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveContactGroup();
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

    private void saveContactGroup() {
        presenter.saveContactGroup(new ContactGroup(
                contactGroupId,
                textGroupName.getText().toString(),
                textGroupDesc.getText().toString(),
                sortOrder));
    }
}
