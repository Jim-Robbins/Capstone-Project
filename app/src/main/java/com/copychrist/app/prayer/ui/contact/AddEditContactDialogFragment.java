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
import com.copychrist.app.prayer.model.ContactGroup;
import com.copychrist.app.prayer.util.Utils;

/**
 * Created by jim on 8/16/17.
 */

public class AddEditContactDialogFragment extends AppCompatDialogFragment {
    private static final String CONTACT = "AddEditContactDialogFragment_Contact";
    private static final String CONTACT_GROUP = "AddEditContactDialogFragment_Contact_Group";
    private Contact contact;
    private ContactGroup contactGroup;

    private EditText txtFirstName, txtLastName;

    public interface AddEditContactDialogListener {
        void onContactSaveClick(Contact contact);
    }

    public static AddEditContactDialogFragment newEditInstance(Contact contact) {
        AddEditContactDialogFragment frag = new AddEditContactDialogFragment();
        frag.contact = contact;
        return frag;
    }

    public static AddEditContactDialogFragment newAddInstance(ContactGroup contactGroup) {
        AddEditContactDialogFragment frag = new AddEditContactDialogFragment();
        frag.contact = null;
        frag.contactGroup = contactGroup;
        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CONTACT, contact);
        outState.putParcelable(CONTACT_GROUP, contactGroup);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            contact = savedInstanceState.getParcelable(CONTACT);
            contactGroup = savedInstanceState.getParcelable(CONTACT_GROUP);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Inflate custom view
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_save_contact, null);
        alertDialogBuilder.setView(dialogView);

        txtFirstName = dialogView.findViewById(R.id.txt_contact_first_name);
        txtLastName = dialogView.findViewById(R.id.txt_contact_last_name);

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
        contact.setContactGroupKey(contactGroup.getKey());
        contact.setContactGroup(contactGroup);
        contact.setProfileColor(Utils.getRandomMaterialColor(getContext(), "400"));
        ((AddEditContactDialogListener) getActivity()).onContactSaveClick(contact);
    }

    private void editContact() {
        contact.setFirstName(txtFirstName.getText().toString());
        contact.setLastName(txtLastName.getText().toString());
        ((AddEditContactDialogListener) getActivity()).onContactSaveClick(contact);
    }
}
