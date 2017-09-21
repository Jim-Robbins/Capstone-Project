package com.copychrist.app.prayer.ui.prayerlist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.BiblePassageSwipableAdapter;
import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.model.PrayerListRequest;
import com.copychrist.app.prayer.util.Utils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by jim on 9/10/17.
 */

public class CardViewPrayerRequestDialogFragment extends AppCompatDialogFragment
    implements BiblePassageSwipableAdapter.BiblePassageAdapterListener {
    private static final String DATA_OBJECT = "VPRDF_DATA";

    private PrayerListRequest prayerListRequest;
    private BiblePassageSwipableAdapter bibleVerseAdapter;

    private int position;

    public static CardViewPrayerRequestDialogFragment newAddInstance(PrayerListRequest prayerListRequest,
                                                                     int position) {
        CardViewPrayerRequestDialogFragment frag = new CardViewPrayerRequestDialogFragment();
        frag.prayerListRequest = prayerListRequest;
        frag.position = position;
        return frag;
    }

    public void setAdapter(List<BiblePassage> biblePassages) {
        bibleVerseAdapter.setAdapterData(biblePassages);
    }

    public interface ViewPrayerRequestDialogListener {
        void onPrayerCardPrayedForClick(int position);
        void onBibleVersesLoad(List<String> passages);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(DATA_OBJECT, prayerListRequest);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            prayerListRequest = savedInstanceState.getParcelable(DATA_OBJECT);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Inflate custom view
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_prayer_request_card_view, null);
        alertDialogBuilder.setView(dialogView);

        TextView txtGroupName = dialogView.findViewById(R.id.txt_group_name);
        ImageView imgProfile = dialogView.findViewById(R.id.icon_profile);
        TextView txtIconText = dialogView.findViewById(R.id.txt_icon_text);
        TextView txtFirstName = dialogView.findViewById(R.id.txt_contact_first_name);
        TextView txtLastName = dialogView.findViewById(R.id.txt_contact_last_name);
        TextView txtTitle = dialogView.findViewById(R.id.txt_request_title);
        TextView txtDesc = dialogView.findViewById(R.id.txt_request_desc);
        TextView txtEndDate = dialogView.findViewById(R.id.txt_timestamp);
        RelativeLayout layoutContactItem = dialogView.findViewById(R.id.layout_contact_container);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_view);

        txtGroupName.setText(prayerListRequest.getGroupName());
        txtFirstName.setText(prayerListRequest.getContact().getFirstName());
        txtLastName.setText(prayerListRequest.getContact().getLastName());
        txtTitle.setText(prayerListRequest.getTitle());
        txtDesc.setText(prayerListRequest.getDesc());

        // displaying the first letter of contact first and last name in icon text
        String initials = Utils.getInitials(prayerListRequest.getContact().getFirstName(),
                prayerListRequest.getContact().getLastName());
        txtIconText.setText(initials);
        imgProfile.setImageResource(R.drawable.bg_circle);
        imgProfile.setColorFilter(prayerListRequest.getContact().getProfileColor());

        int backgroundColor = ColorUtils.setAlphaComponent(prayerListRequest.getContact().getProfileColor(), 40);
        layoutContactItem.setBackgroundColor(backgroundColor);

        bibleVerseAdapter = new BiblePassageSwipableAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(bibleVerseAdapter);

        List<String> passages = prayerListRequest.getPrayerRequest().getBiblePassagesAsList();
        ((ViewPrayerRequestDialogListener) getActivity()).onBibleVersesLoad(passages);

        SimpleDateFormat dateFormat = Utils.getDateFormat(this.getContext());
        if(prayerListRequest.getEndDate() != null)
            txtEndDate.setText(dateFormat.format(prayerListRequest.getEndDate()));

        // Setup Confirm button
        alertDialogBuilder.setPositiveButton(R.string.btn_pray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((ViewPrayerRequestDialogListener) getActivity()).onPrayerCardPrayedForClick(position);
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

    @Override
    public void onRemoveClicked(int position) {

    }

    @Override
    public void onRowLongClicked(int position) {

    }
}
