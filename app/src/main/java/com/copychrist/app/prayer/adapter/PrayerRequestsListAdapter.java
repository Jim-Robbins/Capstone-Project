package com.copychrist.app.prayer.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.PrayerRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jim on 8/17/17.
 *
 */

public class PrayerRequestsListAdapter
        extends RecyclerView.Adapter<PrayerRequestsListAdapter.ViewHolder> {

    private List<PrayerRequest> prayerRequests;
    private OnPrayerRequestClickListener prayerRequestClickListener;

    public PrayerRequestsListAdapter() {}

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request,
                                                                     parent,
                                                                     false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PrayerRequest prayerRequest = prayerRequests.get(position);

        // Todo: Check if end date is < today
        holder.textRequestTitle.setText(prayerRequest.getTitle());
        holder.textRequestDesc.setText(prayerRequest.getDescription());
        List<String> verses = prayerRequest.getPassages();
        if (verses != null && verses.size() > 0) {
            holder.textVerse.setText(prayerRequest.getPassages().get(0));
        }
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prayerRequestClickListener != null) {
                    prayerRequestClickListener.onPrayerRequestClick(prayerRequest.getKey());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return prayerRequests != null ? prayerRequests.size() : 0;
    }

    public void setPrayerRequestClickListener(final OnPrayerRequestClickListener onPrayerRequestClickListener) {
        prayerRequestClickListener = onPrayerRequestClickListener;
    }

    public void setPrayerRequests(List<PrayerRequest> prayerRequests) {
        this.prayerRequests = prayerRequests;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_item_container) CardView layoutItem;
        @BindView(R.id.text_request_title) TextView textRequestTitle;
        @BindView(R.id.text_request_desc) TextView textRequestDesc;
        @BindView(R.id.text_verse) TextView textVerse;

        ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnPrayerRequestClickListener {
        void onPrayerRequestClick(String requestId);
    }
}
