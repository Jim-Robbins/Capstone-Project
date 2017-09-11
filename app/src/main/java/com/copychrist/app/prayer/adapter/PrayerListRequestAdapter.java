package com.copychrist.app.prayer.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.PrayerListRequest;
import com.copychrist.app.prayer.util.CircleTransform;
import com.copychrist.app.prayer.util.FlipAnimator;
import com.copychrist.app.prayer.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jim on 8/17/17.
 *
 * Inspired by Ravi Tamada on 21/02/17.
 * https://www.androidhive.info/2017/02/android-creating-gmail-like-inbox-using-recyclerview/
 */
public class PrayerListRequestAdapter extends RecyclerView.Adapter<PrayerListRequestAdapter.ViewHolder> {
    private Context mContext;
    private List<PrayerListRequest> prayerListRequests;
    private PrayerListRequestAdapterListener prayerListRequestAdapterListener;
    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        @BindView(R.id.layout_request_item_container) LinearLayout itemContainer;
        @BindView(R.id.icon_container) RelativeLayout iconContainer;
        @BindView(R.id.icon_back) RelativeLayout iconBack;
        @BindView(R.id.icon_front) RelativeLayout iconFront;
        @BindView(R.id.txt_contact_name) TextView txtContactName;
        @BindView(R.id.txt_request_title) TextView txtRequestTitle;
        @BindView(R.id.txt_request_details) TextView txtRequestDesc;
        @BindView(R.id.txt_icon_text) TextView txtIconText;
        @BindView(R.id.txt_timestamp) TextView txtTimeStamp;
        @BindView(R.id.icon_profile) ImageView imgProfile;

        ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            prayerListRequestAdapterListener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }

    public interface PrayerListRequestAdapterListener {
        void onIconClicked(int position);
        void onRowClicked(int position);
        void onRowLongClicked(int position);
    }

    public PrayerListRequestAdapter(Context mContext, PrayerListRequestAdapterListener listener) {
        super();
        this.mContext = mContext;
        this.prayerListRequestAdapterListener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prayer_list_request, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SimpleDateFormat dateFormat = Utils.getDateFormat(this.mContext);
        PrayerListRequest prayerListRequest = prayerListRequests.get(position);

        // displaying text view data
        holder.txtContactName.setText(prayerListRequest.getContactName());
        holder.txtRequestTitle.setText(prayerListRequest.getTitle());
        holder.txtRequestDesc.setText(prayerListRequest.getDesc());

        if(prayerListRequest.getEndDate() != null)
            holder.txtTimeStamp.setText(dateFormat.format(prayerListRequest.getEndDate()));


        // displaying the first letter of From in icon text
        holder.txtIconText.setText(prayerListRequest.getContactName().substring(0, 1));

        // change the row state to activated
        holder.itemView.setActivated(selectedItems.get(position, false));

        // change the font style depending on prayerListRequest read status
        applyPrayerStatus(holder, prayerListRequest);

        // handle icon animation
        applyIconAnimation(holder, position);

        // display profile image
        applyProfilePicture(holder, prayerListRequest);

        // apply click events
        applyClickEvents(holder, position);
    }

    public void setAdpaterData(List<PrayerListRequest> prayerListRequests) {
        this.prayerListRequests = prayerListRequests;
        notifyDataSetChanged();
    }
    private void applyClickEvents(ViewHolder holder, final int position) {
        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prayerListRequestAdapterListener.onIconClicked(position);
            }
        });

        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prayerListRequestAdapterListener.onRowClicked(position);
            }
        });

        holder.itemContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                prayerListRequestAdapterListener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }

    private void applyProfilePicture(ViewHolder holder, PrayerListRequest prayerListRequest) {
        if (!TextUtils.isEmpty(prayerListRequest.getPicture())) {
            Glide.with(mContext).load(prayerListRequest.getPicture())
                    .thumbnail(0.5f)
                    .crossFade()
                    .transform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgProfile);
            holder.imgProfile.setColorFilter(null);
            holder.txtIconText.setVisibility(View.GONE);
        } else {
            holder.imgProfile.setImageResource(R.drawable.bg_circle);
            holder.imgProfile.setColorFilter(prayerListRequest.getColor());
            holder.txtIconText.setVisibility(View.VISIBLE);
        }
    }

    private void applyIconAnimation(ViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }


    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    private void applyPrayerStatus(ViewHolder holder, PrayerListRequest prayerListRequest) {
        if (prayerListRequest.isPrayedFor()) {
            holder.txtContactName.setTypeface(null, Typeface.NORMAL);
            holder.txtRequestTitle.setTypeface(null, Typeface.NORMAL);
            holder.txtContactName.setTextColor(ContextCompat.getColor(mContext, R.color.color_item_primary));
            holder.txtRequestTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_item_secondary));
        } else {
            holder.txtContactName.setTypeface(null, Typeface.BOLD);
            holder.txtRequestTitle.setTypeface(null, Typeface.BOLD);
            holder.txtContactName.setTextColor(ContextCompat.getColor(mContext, R.color.color_item_main));
            holder.txtRequestTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_item_primary));
        }
    }

    @Override
    public int getItemCount() {
        return prayerListRequests != null ? prayerListRequests.size() : 0;
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

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        prayerListRequests.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }
}
