package com.copychrist.app.prayer.adapter;

import android.content.Context;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.util.CircleTransform;
import com.copychrist.app.prayer.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Jim Robbins on 8/17/17.
 * Load contacts list
 */
public class ContactsSwipeableSelectableRVAdapter extends RecyclerView.Adapter<ContactsSwipeableSelectableRVAdapter.ViewHolder> {

    private final Context context;
    private ContactSwipeableSelectableRVListener contactClickListener;
    private List<Contact> contacts;
    private List<PrayerRequest> prayerRequests;

    // array used to perform multiple animation at once
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_swipeable_item_container) protected SwipeRevealLayout swipeLayout;
        @BindView(R.id.layout_contact_container) protected RelativeLayout layoutItem;
        @BindView(R.id.txt_contact_first_name) protected TextView textFirstName;
        @BindView(R.id.txt_contact_last_name) protected TextView textLastName;
        @BindView(R.id.img_icon_profile) protected ImageView imgProfile;
        @BindView(R.id.txt_icon_text) protected TextView txtIconText;
        @BindView(R.id.item1) protected TextView textItem1;
        @BindView(R.id.item2) protected TextView textItem2;
        @BindView(R.id.item3) protected TextView textItem3;
        @BindView(R.id.more_items) protected TextView textMoreItems;

        @BindView(R.id.swipe_btn_remove) RelativeLayout btnRemove;
        @BindView(R.id.swipe_btn_edit) RelativeLayout btnEdit;

        ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Contact contact) {
            layoutItem.setTag(contact);
            textFirstName.setText(contact.getFirstName());
            textLastName.setText(contact.getLastName());

            // displaying the first letter of contact first and last name in icon text
            String initials = Utils.getInitials(contact.getFirstName(), contact.getLastName());
            txtIconText.setText(initials);
            bindRequests(contact);
        }

        private void bindRequests( Contact contact) {
            textItem1.setVisibility(View.GONE);
            textItem2.setVisibility(View.GONE);
            textItem3.setVisibility(View.GONE);

            List<PrayerRequest> filteredRequests = getPrayerRequests(contact.getKey());
            if(filteredRequests != null && !filteredRequests.isEmpty()) {
                setRequestTextView(textItem1, filteredRequests.get(0));
                if (filteredRequests.size() > 1) {
                    setRequestTextView(textItem2, filteredRequests.get(1));
                    if (filteredRequests.size() > 2) {
                        setRequestTextView(textItem3, filteredRequests.get(2));
                    }
                }
            }
        }

        private void setRequestTextView(final TextView text, final PrayerRequest request) {
            text.setText(request.getTitle());
            text.setVisibility(View.VISIBLE);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (contactClickListener != null) {
                        contactClickListener.onPrayerRequestClick(request);
                    }
                }
            });
        }

        private List<PrayerRequest> getPrayerRequests(String key) {
            List<PrayerRequest> topList = new ArrayList<>();
            int requestCount = 3;
            Timber.d("Contact Key:" + key);
            for (PrayerRequest prayerRequest : prayerRequests) {
                if (prayerRequest.getContactKey().equalsIgnoreCase(key) && requestCount > 0) {
                    topList.add(prayerRequest);
                    requestCount--;
                }
            }
            Timber.d(topList.toString());
            return topList;
        }
    }

    public interface ContactSwipeableSelectableRVListener {
        void onContactClick(int position);
        void onPrayerRequestClick(PrayerRequest prayerRequest);
        void onRowLongClicked(int position);
        void onContactRemoveClicked(int position);
        void onContactEditClicked(int position);

    }

    public ContactsSwipeableSelectableRVAdapter(Context context, ContactSwipeableSelectableRVListener listener) {
        super();
        this.contactClickListener = listener;
        this.context = context;

        viewBinderHelper.setOpenOnlyOne(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_contact_and_top_prayer_requests_swipeable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Contact contact = contacts.get(position);

        viewBinderHelper.bind(holder.swipeLayout, contact.getKey());

        holder.bind(contact);

        // display profile image
        applyProfilePicture(holder, contact);

        // apply click events
        applyClickEvents(holder, position);

    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }

    public void setAdpaterData(final List<Contact> contacts, List<PrayerRequest> prayerRequests) {
        this.contacts = contacts;
        this.prayerRequests = prayerRequests;
        notifyDataSetChanged();
    }

    private void applyClickEvents(ViewHolder holder, final int position) {

        holder.layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                contactClickListener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = contacts.get(position);
                viewBinderHelper.closeLayout(contact.getKey());
                contactClickListener.onContactClick(position);
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = contacts.get(position);
                viewBinderHelper.closeLayout(contact.getKey());
                contactClickListener.onContactRemoveClicked(position);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = contacts.get(position);
                viewBinderHelper.closeLayout(contact.getKey());
                contactClickListener.onContactEditClicked(position);
            }
        });

        holder.textMoreItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = contacts.get(position);
                if (contactClickListener != null) {
                    viewBinderHelper.closeLayout(contact.getKey());
                    contactClickListener.onContactClick(position);
                }
            }
        });

    }

    private void applyProfilePicture(ViewHolder holder, Contact contact) {
        if (!TextUtils.isEmpty(contact.getPictureUrl())) {
            Glide.with(context).load(contact.getPictureUrl())
                    .thumbnail(0.5f)
                    .crossFade()
                    .transform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgProfile);
            holder.imgProfile.setColorFilter(contact.getProfileColor());
            holder.txtIconText.setVisibility(View.GONE);
        } else {
            holder.imgProfile.setImageResource(R.drawable.bg_circle);
            holder.imgProfile.setColorFilter(contact.getProfileColor());
            holder.txtIconText.setVisibility(View.VISIBLE);
        }
        int backgroundColor = ColorUtils.setAlphaComponent(contact.getProfileColor(), 40);
        holder.layoutItem.setBackgroundColor(backgroundColor);
    }

}
