package com.copychrist.app.prayer.ui.contactgroups;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.copychrist.app.prayer.R;

/**
 * Created by jim on 8/16/17.
 */

public class AddContactDialog {
    private final Context context;
    private final Dialog dialog;
    private final String groupName;
    private final ContactsPresenter presenter;

    EditText txtFirstName;
    EditText txtLastName;
    
    public AddContactDialog(Context context, String groupName, ContactsPresenter presenter) {
        this.context = context;
        this.groupName = groupName;
        this.presenter = presenter;
        
        dialog = new Dialog(context);
        dialog.setTitle(R.string.dialog_add_contact_title);
        dialog.setContentView(R.layout.dialog_add_contact);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        
        txtFirstName = (EditText) dialog.findViewById(R.id.txtFirstName);
        txtLastName = (EditText) dialog.findViewById(R.id.txtLastName);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnOk);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewContact();
                dialog.dismiss();
            }
        });

        centerDialogInView();
    }

    public void show() {
        dialog.show();
    }

    private void addNewContact() {
        presenter.onAddClick(
                txtFirstName.getText().toString(),
                txtLastName.getText().toString(),
                groupName,
                "");
    }

    private void centerDialogInView() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 0.70);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.25);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);
    }
}
