package com.copychrist.app.prayer.ui.prayerlist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.PrayerListRequest;
import com.copychrist.app.prayer.util.Utils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by jim on 9/10/17.
 */

public class ViewPrayerRequestDialogFragment extends AppCompatDialogFragment {

    private PrayerListRequest prayerListRequest;
    private PrayerListContract.Presenter presenter;
    private int position;

    public static ViewPrayerRequestDialogFragment newAddInstance(PrayerListRequest prayerListRequest,
                                                                 PrayerListContract.Presenter prayerListPresenter,
                                                                 int position) {
        ViewPrayerRequestDialogFragment frag = new ViewPrayerRequestDialogFragment();
        frag.prayerListRequest = prayerListRequest;
        frag.presenter = prayerListPresenter;
        frag.position = position;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Inflate custom view
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_prayer_request_card_view, null);
        alertDialogBuilder.setView(dialogView);

        TextView txtGroupName = dialogView.findViewById(R.id.txt_group_name);
        ImageView imgProfile = dialogView.findViewById(R.id.profile_icon);
        TextView txtFirstName = dialogView.findViewById(R.id.txt_contact_first_name);
        TextView txtLastName = dialogView.findViewById(R.id.txt_contact_last_name);
        TextView txtTitle = dialogView.findViewById(R.id.txt_request_title);
        TextView txtDesc = dialogView.findViewById(R.id.txt_request_desc);
//        TextView txtPassage = dialogView.findViewById(R.id.txt_passage);
        TextView txtEndDate = dialogView.findViewById(R.id.txt_timestamp);

        txtGroupName.setText(prayerListRequest.getGroupName());
        txtFirstName.setText(prayerListRequest.getContact().getFirstName());
        txtLastName.setText(prayerListRequest.getContact().getLastName());
        txtTitle.setText(prayerListRequest.getTitle());
        txtDesc.setText(prayerListRequest.getDesc());

        List<String> passages = prayerListRequest.getPrayerRequest().getBiblePassagesAsList();
//        if(!passages.isEmpty()) {
//            txtPassage.setText(passages.get(0));
//        }


        SimpleDateFormat dateFormat = Utils.getDateFormat(this.getContext());
        if(prayerListRequest.getEndDate() != null)
            txtEndDate.setText(dateFormat.format(prayerListRequest.getEndDate()));

        // Setup Confirm button
        alertDialogBuilder.setPositiveButton(R.string.btn_pray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.onPrayerCardPrayedForClick(position);
                dialog.dismiss();
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
}
