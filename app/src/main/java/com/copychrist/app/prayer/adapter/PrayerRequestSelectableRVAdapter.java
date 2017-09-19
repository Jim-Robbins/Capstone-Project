package com.copychrist.app.prayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.PrayerListRequest;
import com.copychrist.app.prayer.util.FlipAnimator;
import com.copychrist.app.prayer.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jim on 8/20/17.
 * Adapter View for selecting Prayer Requests to add to a Prayer List.
 */

public class PrayerRequestSelectableRVAdapter extends RecyclerView.Adapter<PrayerRequestSelectableRVAdapter.ViewHolder> {
    private Context context;
    private List<PrayerListRequest> prayerListRequests;
    private PrayerRequestSelectableListener PrayerRequestSelectableListener;
    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_item_content) LinearLayout itemContainer;
        @BindView(R.id.txt_contact_name) TextView txtContactName;
        @BindView(R.id.txt_request_title) TextView txtRequestTitle;
        @BindView(R.id.txt_request_details) TextView txtRequestDesc;
        @BindView(R.id.txt_timestamp) TextView txtTimeStamp;
        @BindView(R.id.layout_item_icon) RelativeLayout iconContainer;
        @BindView(R.id.layout_icon_back) RelativeLayout iconBack;
        @BindView(R.id.layout_icon_front) RelativeLayout iconFront;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(PrayerListRequest prayerListRequest) {
            SimpleDateFormat dateFormat = Utils.getDateFormat(context);

            // displaying text view data
            txtContactName.setText(prayerListRequest.getContactName());
            txtRequestTitle.setText(prayerListRequest.getTitle());
            txtRequestDesc.setText(prayerListRequest.getDesc());

            if(prayerListRequest.getEndDate() == null) {
                txtTimeStamp.setVisibility(View.GONE);
            } else {
                txtTimeStamp.setVisibility(View.VISIBLE);
                txtTimeStamp.setText(dateFormat.format(prayerListRequest.getEndDate()));
            }
        }
    }

    public interface PrayerRequestSelectableListener {
        void onSelectionRowClicked(int position);
        void onSelectionRowLongClicked(int position);
    }

    public PrayerRequestSelectableRVAdapter(Context context, PrayerRequestSelectableListener listener) {
        super();
        this.context = context;
        this.PrayerRequestSelectableListener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prayer_request_selectable, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the request
        PrayerListRequest prayerListRequest = prayerListRequests.get(position);

        if(prayerListRequest != null) {
            holder.bind(prayerListRequest);
        }

        if(selectedItems != null) {
            holder.itemView.setActivated(selectedItems.get(position, false));
        }

        // handle icon animation
        applyIconAnimation(holder, position);

        // apply click events
        applyClickEvents(holder, position);
    }

    @Override
    public int getItemCount() {
        return prayerListRequests != null ? prayerListRequests.size() : 0;
    }

    public void setAdpaterData(List<PrayerListRequest> prayerListRequests) {
        this.prayerListRequests = prayerListRequests;
        clearSelections();
    }

    private void applyClickEvents(ViewHolder holder, final int position) {

        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrayerRequestSelectableListener.onSelectionRowClicked(position);
            }
        });

        holder.itemContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PrayerRequestSelectableListener.onSelectionRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }

    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    private void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }

        return items;
    }

    private void applyIconAnimation(ViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(context, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(context, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }

}
