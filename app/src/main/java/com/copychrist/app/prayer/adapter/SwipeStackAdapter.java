package com.copychrist.app.prayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.PrayerListRequest;
import com.copychrist.app.prayer.util.Utils;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jim on 9/13/17.
 */

public class SwipeStackAdapter extends BaseAdapter {

    private List<PrayerListRequest> mData;

    public SwipeStackAdapter(List<PrayerListRequest> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public PrayerListRequest getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_prayer_request_card_view, parent, false);

        new ViewHolder(convertView, mData.get(position));

        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.txt_group_name) TextView txtGroupName;
        @BindView(R.id.txt_contact_first_name) TextView txtFirstName;
        @BindView(R.id.txt_contact_last_name) TextView txtLastName;
        @BindView(R.id.txt_request_title) TextView txtTitle;
        @BindView(R.id.txt_request_desc) TextView txtDesc;
//        @BindView(R.id.txt_passage) TextView txtPassage;
        @BindView(R.id.txt_timestamp) TextView txtEndDate;

        @BindView(R.id.profile_icon) ImageView profile_icon;

        ViewHolder(View itemView, final PrayerListRequest prayerListRequest) {
            ButterKnife.bind(this, itemView);

            txtGroupName.setText(prayerListRequest.getGroupName());
            txtFirstName.setText(prayerListRequest.getContact().getFirstName());
            txtLastName.setText(prayerListRequest.getContact().getLastName());
            txtTitle.setText(prayerListRequest.getTitle());
            txtDesc.setText(prayerListRequest.getDesc());
//            if(!prayerListRequest.getPrayerRequest().getBiblePassages().isEmpty())
//                txtPassage.setText(prayerListRequest.getPrayerRequest().getBiblePassagesAsList().get(0));

            SimpleDateFormat dateFormat = Utils.getDateFormat(itemView.getContext());
            if(prayerListRequest.getEndDate() != null)
                txtEndDate.setText(dateFormat.format(prayerListRequest.getEndDate()));


        }
    }
}
