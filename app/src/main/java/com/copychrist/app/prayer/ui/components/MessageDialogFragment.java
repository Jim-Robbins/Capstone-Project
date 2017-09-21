package com.copychrist.app.prayer.ui.components;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.copychrist.app.prayer.R;

/**
 * Created by jim on 8/26/17.
 */

public class MessageDialogFragment extends AppCompatDialogFragment {

    private static String TITLE = "title";
    private static String MESSAGE = "message";

    /**
     * Public interface to listen for Confirm button clicked
     */
    public interface MessageActionDialogListener {
        void onDialogConfirmClicked();
    }

    /**
     * New Instance creates the dialog fragment, and allows us to pass some values in.
     * @param title
     * @param message
     * @return
     */
    public static MessageDialogFragment newInstance(String title, String message) {
        MessageDialogFragment frag = new MessageDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get params we passed to newInstance
        String title = getArguments().getString(TITLE);
        String message = getArguments().getString(MESSAGE);

        // Create alert dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // Set Dialog text
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        // Setup Confirm button
        alertDialogBuilder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MessageActionDialogListener) getActivity()).onDialogConfirmClicked();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

        });


        return alertDialogBuilder.create();
    }
}
