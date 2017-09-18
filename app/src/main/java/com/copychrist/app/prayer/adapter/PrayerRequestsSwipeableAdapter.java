package com.copychrist.app.prayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.PrayerRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jim on 8/17/17.
 *
 */

public class PrayerRequestsSwipeableAdapter extends RecyclerView.Adapter<PrayerRequestsSwipeableAdapter.ViewHolder> {
    private List<PrayerRequest> prayerRequests;
    private PrayerRequestSwipeableListener prayerRequestClickListener;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_swipeable_item_container) SwipeRevealLayout swipeLayout;
        @BindView(R.id.swipe_btn_edit) RelativeLayout btnEdit;
        @BindView(R.id.swipe_btn_archive) RelativeLayout btnArchive;
        @BindView(R.id.swipe_btn_remove) RelativeLayout btnRemove;
        @BindView(R.id.layout_item_container) RelativeLayout layoutItem;
        @BindView(R.id.txt_request_title) TextView txtRequestTitle;
        @BindView(R.id.txt_request_desc) TextView txtRequestDesc;
        @BindView(R.id.txt_verse) TextView txtVerse;

        ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final PrayerRequest prayerRequest) {
            txtRequestTitle.setText(prayerRequest.getTitle());
            txtRequestDesc.setText(prayerRequest.getDescription());
            List<String> verses = prayerRequest.getBiblePassagesAsList();
            if (verses != null && verses.size() > 0) {
                txtVerse.setText(TextUtils.join(",", verses));
            }
        }
    }

    public interface PrayerRequestSwipeableListener {
        void onPrayerRequestClick(int request);
        void onRemoveClicked(int position);
        void onArchiveClicked(int position);
        void onMoreClicked(int position);
        void onRowLongClicked(int position);
    }

    public PrayerRequestsSwipeableAdapter() {
        super();
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prayer_request_swipeable_for_contact,
                                                                     parent,
                                                                     false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PrayerRequest prayerRequest = prayerRequests.get(position);

        viewBinderHelper.bind(holder.swipeLayout, prayerRequest.getKey());

        holder.bind(prayerRequest);

        // apply click events
        applyClickEvents(holder, position);
    }

    @Override
    public int getItemCount() {
        return prayerRequests != null ? prayerRequests.size() : 0;
    }

    public void setPrayerRequestClickListener(final PrayerRequestSwipeableListener prayerRequestSwipeableListener) {
        prayerRequestClickListener = prayerRequestSwipeableListener;
    }

    public void setPrayerRequests(List<PrayerRequest> prayerRequests) {
        this.prayerRequests = prayerRequests;
        notifyDataSetChanged();
    }

    private void applyClickEvents(ViewHolder holder, final int position) {

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSwipeLayout(position);
                if (prayerRequestClickListener != null) {
                    prayerRequestClickListener.onPrayerRequestClick(position);
                }
            }
        });

        holder.layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                closeSwipeLayout(position);
                if (prayerRequestClickListener != null) {
                    prayerRequestClickListener.onRowLongClicked(position);
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                }
                return true;
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSwipeLayout(position);
                if (prayerRequestClickListener != null) {
                    prayerRequestClickListener.onRemoveClicked(position);
                }
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSwipeLayout(position);
                if (prayerRequestClickListener != null) {
                    prayerRequestClickListener.onMoreClicked(position);
                }
            }
        });

        holder.btnArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSwipeLayout(position);
                if (prayerRequestClickListener != null) {
                    prayerRequestClickListener.onArchiveClicked(position);
                }
            }
        });
    }

    private void closeSwipeLayout(int position) {
        PrayerRequest prayerRequest = prayerRequests.get(position);
        viewBinderHelper.closeLayout(prayerRequest.getKey());
    }

}
