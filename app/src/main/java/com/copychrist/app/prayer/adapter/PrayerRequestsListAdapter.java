package com.copychrist.app.prayer.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.BibleVerse;
import com.copychrist.app.prayer.model.PrayerRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by jim on 8/17/17.
 */

public class PrayerRequestsListAdapter
        extends RecyclerView.Adapter<PrayerRequestsListAdapter.ViewHolder>
        implements RealmChangeListener {

    private RealmResults<PrayerRequest> prayerRequests;
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
        RealmList<BibleVerse> verses = prayerRequest.getVerses();
        if (verses.size() > 0) {
            holder.textVerse.setText(prayerRequest.getVerses().get(0).getPassage());
        }
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prayerRequestClickListener != null) {
                    prayerRequestClickListener.onPrayerRequestClick(prayerRequest.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return prayerRequests != null ? prayerRequests.size() : 0;
    }

    @Override
    public void onChange(Object o) {
        notifyDataSetChanged();
    }

    public void setPrayerRequestClickListener(final OnPrayerRequestClickListener onPrayerRequestClickListener) {
        prayerRequestClickListener = onPrayerRequestClickListener;
    }

    public void setPrayerRequests(final RealmResults<PrayerRequest> prayerRequests) {
        this.prayerRequests = prayerRequests;
        this.prayerRequests.addChangeListener(this);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_item_container) CardView layoutItem;
        @BindView(R.id.text_request_title) TextView textRequestTitle;
        @BindView(R.id.text_request_desc) TextView textRequestDesc;
        @BindView(R.id.text_verse) TextView textVerse;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnPrayerRequestClickListener {
        void onPrayerRequestClick(int requestId);
    }
}
