package com.copychrist.app.prayer.ui.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.util.Utils;

/**
 * Created by jim on 8/26/17.
 *
 */

public class DeleteDialogFragment extends AppCompatDialogFragment {

    private static String TITLE = "title";
    private static String ITEM_NAME = "itemName";

    private DeleteActionDialogListener listener;

    /**
     * Public interface to listen for Confirm button clicked
     */
    public interface DeleteActionDialogListener {
        void onConfirmedDeleteDialog();
    }

    /**
     * New Instance creates the dialog fragment, and allows us to pass some values in.
     * @param title     Title of the dialog
     * @param itemName  Name of the Item to delete
     * @return DeleteDialogFragment
     */
    public static DeleteDialogFragment newInstance(String title, String itemName) {
        DeleteDialogFragment frag = new DeleteDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(ITEM_NAME, itemName);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get params we passed to newInstance
        String title = getArguments().getString(TITLE);
        String itemName = getArguments().getString(ITEM_NAME);

        // Create alert dialog
        Resources res = getActivity().getResources();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // Set Dialog text
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(Utils.fromHtml(res.getString(R.string.delete_warning, itemName)));

        // Setup Confirm button
        alertDialogBuilder.setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onConfirmedDeleteDialog();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DeleteActionDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement DeleteActionDialogListener");
        }
    }
}
