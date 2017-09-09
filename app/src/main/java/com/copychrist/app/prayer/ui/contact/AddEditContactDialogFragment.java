package com.copychrist.app.prayer.ui.contact;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.EditText;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.ui.contactgroups.ContactGroupContract;

/**
 * Created by jim on 8/16/17.
 */

public class AddEditContactDialogFragment extends AppCompatDialogFragment {
    private Contact contact;
    private ContactGroupContract.Presenter contactsPresenter;
    private ContactContract.Presenter contactPresenter;
    private String groupName = "";

    private EditText txtFirstName, txtLastName;

    public static AddEditContactDialogFragment newEditInstance(Contact contact,
                                                               ContactContract.Presenter contactPresenter) {
        AddEditContactDialogFragment frag = new AddEditContactDialogFragment();
        frag.contact = contact;
        frag.contactPresenter = contactPresenter;
        return frag;
    }

    public static AddEditContactDialogFragment newAddInstance(String groupName,
                                                              ContactGroupContract.Presenter contactsPresenter) {
        AddEditContactDialogFragment frag = new AddEditContactDialogFragment();
        frag.contact = null;
        frag.groupName = groupName;
        frag.contactsPresenter = contactsPresenter;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Inflate custom view
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_save_contact, null);
        alertDialogBuilder.setView(dialogView);

        txtFirstName = dialogView.findViewById(R.id.text_contact_first_name);
        txtLastName = dialogView.findViewById(R.id.text_contact_last_name);

        if(contact != null) {
            alertDialogBuilder.setTitle(R.string.dialog_edit_contact_title);
            txtFirstName.setText(contact.getFirstName());
            txtLastName.setText(contact.getLastName());
        } else {
            alertDialogBuilder.setTitle(R.string.dialog_add_contact_title);
        }

        // Setup Confirm button
        alertDialogBuilder.setPositiveButton(R.string.btn_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (contact == null) {
                    addContact();
                } else  {
                    editContact();
                }

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

    private void addContact() {
        contact = new Contact();
        contact.setFirstName(txtFirstName.getText().toString());
        contact.setLastName(txtLastName.getText().toString());
        contact.setGroupKey(groupName);
        contactsPresenter.onSaveContactClick(contact);
    }

    private void editContact() {
        contact.setFirstName(txtFirstName.getText().toString());
        contact.setLastName(txtLastName.getText().toString());
        contactPresenter.onSaveContactClick(contact);
    }
}
