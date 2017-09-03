package com.copychrist.app.prayer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.data.model.BibleVerse;
import com.copychrist.app.prayer.data.model.PrayerRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jim on 8/17/17.
 */

public class PrayerRequestsListAdapter extends CursorRecyclerViewAdapter<PrayerRequestsListAdapter.ViewHolder> {

    private OnPrayerRequestClickListener prayerRequestClickListener;

    public PrayerRequestsListAdapter(Context context){
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor) {
        final PrayerRequest prayerRequest = PrayerRequest.getPrayerRequestFromCursor(cursor);

        // Todo: Check if end date is < today
        holder.textRequestTitle.setText(prayerRequest.getTitle());
        holder.textRequestDesc.setText(prayerRequest.getDescription());
        List<BibleVerse> verses = prayerRequest.getVerses();
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
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void setCursor(Cursor cursor) {
        changeCursor(cursor);
    }

    public void setPrayerRequestClickListener(final OnPrayerRequestClickListener onPrayerRequestClickListener) {
        prayerRequestClickListener = onPrayerRequestClickListener;
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
        void onPrayerRequestClick(long requestId);
    }
}
