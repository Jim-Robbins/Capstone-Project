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
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
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
public class PrayerListRequestSelectableSwipeableAdapter extends RecyclerView.Adapter<PrayerListRequestSelectableSwipeableAdapter.ViewHolder> {
    private Context context;
    private List<PrayerListRequest> prayerListRequests;
    private PrayerListRequestAdapterListener prayerListRequestAdapterListener;
    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_swipeable_item_container) SwipeRevealLayout swipeLayout;
        @BindView(R.id.layout_swipeable_menu) LinearLayout deleteLayout;
        @BindView(R.id.swipe_btn_archive) RelativeLayout btnArchive;
        @BindView(R.id.swipe_btn_remove) RelativeLayout btnRemove;
        @BindView(R.id.swipe_btn_edit) RelativeLayout btnEdit;

        @BindView(R.id.layout_item_content) LinearLayout itemContainer;
        @BindView(R.id.layout_item_icon) RelativeLayout iconContainer;
        @BindView(R.id.layout_icon_back) RelativeLayout iconBack;
        @BindView(R.id.layout_icon_front) RelativeLayout iconFront;
        @BindView(R.id.txt_contact_name) TextView txtContactName;
        @BindView(R.id.txt_request_title) TextView txtRequestTitle;
        @BindView(R.id.txt_request_details) TextView txtRequestDesc;
        @BindView(R.id.txt_icon_text) TextView txtIconText;
        @BindView(R.id.txt_timestamp) TextView txtTimeStamp;
        @BindView(R.id.img_icon_profile) ImageView imgProfile;

        ViewHolder(final View itemView) {
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

            // displaying the first letter of contact first and last name in icon text
            String initials = Utils.getInitials(prayerListRequest.getContact().getFirstName(),
                    prayerListRequest.getContact().getLastName());
            txtIconText.setText(initials);
        }
    }

    public interface PrayerListRequestAdapterListener {
        void onIconClicked(int position);
        void onRowClicked(int position);
        void onRowLongClicked(int position);
        void onRemoveClicked(int position);
        void onArchiveClicked(int position);
        void onMoreClicked(int position);
    }

    public PrayerListRequestSelectableSwipeableAdapter(Context context, PrayerListRequestAdapterListener listener) {
        super();
        this.context = context;
        this.prayerListRequestAdapterListener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();

        viewBinderHelper.setOpenOnlyOne(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prayer_request_swipeable_selectable, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        PrayerListRequest prayerListRequest = prayerListRequests.get(position);

        viewBinderHelper.bind(holder.swipeLayout, prayerListRequest.getKey());

        holder.bind(prayerListRequest);

        boolean rowStateActive = false;
//        if(prayerListRequest.getPrayerRequest().getLastPrayedFor() != null) {
//            long prayedForLast = prayerListRequest.getPrayerRequest().getLastPrayedFor();
//            if (prayedForLast > 0 && prayedForLast < Utils.getCurrentTime(Utils.TWELVE_HOUR_OFFSET)) {
//                // change the row state to activated
//                rowStateActive = true;
//                selectedItems.put(position, true);
//                animationItemsIndex.put(position, true);
//                applyIconAnimation(holder, position);
//            }
//        }

        holder.itemView.setActivated(selectedItems.get(position, rowStateActive));

        // change the font style depending on prayerListRequest read status
        applyPrayerStatus(holder, prayerListRequest);

        // handle icon animation
        applyIconAnimation(holder, position);

        // display profile image
        applyProfilePicture(holder, prayerListRequest);

        // apply click events
        applyClickEvents(holder, position);


    }

//    /**
//     * Only if you need to restore open/close state when the orientation is changed.
//     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
//     */
//    public void saveStates(Bundle outState) {
//        binderHelper.saveStates(outState);
//    }
//
//    /**
//     * Only if you need to restore open/close state when the orientation is changed.
//     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
//     */
//    public void restoreStates(Bundle inState) {
//        binderHelper.restoreStates(inState);
//    }

    public void setAdpaterData(List<PrayerListRequest> prayerListRequests) {
        this.prayerListRequests = prayerListRequests;
        clearSelections();
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

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrayerListRequest prayerListRequest = prayerListRequests.get(position);
                viewBinderHelper.closeLayout(prayerListRequest.getKey());
                prayerListRequestAdapterListener.onRemoveClicked(position);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrayerListRequest prayerListRequest = prayerListRequests.get(position);
                viewBinderHelper.closeLayout(prayerListRequest.getKey());
                prayerListRequestAdapterListener.onMoreClicked(position);
            }
        });

        holder.btnArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrayerListRequest prayerListRequest = prayerListRequests.get(position);
                viewBinderHelper.closeLayout(prayerListRequest.getKey());
                prayerListRequestAdapterListener.onArchiveClicked(position);
            }
        });
    }

    private void applyProfilePicture(ViewHolder holder, PrayerListRequest prayerListRequest) {
        if (!TextUtils.isEmpty(prayerListRequest.getPicture())) {
            Glide.with(context).load(prayerListRequest.getPicture())
                    .thumbnail(0.5f)
                    .crossFade()
                    .transform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgProfile);
            holder.imgProfile.setColorFilter(prayerListRequest.getColor());
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


    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    private void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    private void applyPrayerStatus(ViewHolder holder, PrayerListRequest prayerListRequest) {
        if (prayerListRequest.isPrayedFor()) {
            holder.txtContactName.setTypeface(null, Typeface.NORMAL);
            holder.txtRequestTitle.setTypeface(null, Typeface.NORMAL);
            holder.txtContactName.setTextColor(ContextCompat.getColor(context, R.color.color_item_primary));
            holder.txtRequestTitle.setTextColor(ContextCompat.getColor(context, R.color.color_item_secondary));
        } else {
            holder.txtContactName.setTypeface(null, Typeface.BOLD);
            holder.txtRequestTitle.setTypeface(null, Typeface.BOLD);
            holder.txtContactName.setTextColor(ContextCompat.getColor(context, R.color.color_item_main));
            holder.txtRequestTitle.setTextColor(ContextCompat.getColor(context, R.color.color_item_primary));
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

    private void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        resetAnimationIndex();
        notifyDataSetChanged();
    }


    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }
}
