package com.copychrist.app.prayer.adapter;

import android.content.Context;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jim Robbins on 8/17/17.
 * Load contacts list
 */
public class ContactsSwipeableSelectableRVAdapter extends RecyclerView.Adapter<ContactsSwipeableSelectableRVAdapter.ViewHolder> {

    private final Context context;
    private ContactSwipeableSelectableRVListener contactClickListener;
    private List<Contact> contacts;

    // array used to perform multiple animation at once
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_swipeable_item_container) SwipeRevealLayout swipeLayout;
        @BindView(R.id.layout_contact_container) RelativeLayout layoutItem;
        @BindView(R.id.txt_contact_first_name) TextView textFirstName;
        @BindView(R.id.txt_contact_last_name) TextView textLastName;
        @BindView(R.id.img_icon_profile) ImageView imgProfile;
        @BindView(R.id.txt_icon_text) TextView txtIconText;
        @BindView(R.id.item1) TextView textItem1;
        @BindView(R.id.item2) TextView textItem2;
        @BindView(R.id.item3) TextView textItem3;
        @BindView(R.id.more_items) TextView textMoreItems;

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

//        List<PrayerRequest> prayerRequests = contact.getPrayerRequests();
//        if(prayerRequests != null && !prayerRequests.isEmpty()) {
//            final PrayerRequest request1 = prayerRequests.get(0);
//            Timber.d("0:"+request1.getTitle());
//            holder.textItem1.setText(request1.getTitle());
//            holder.textItem1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (contactClickListener != null) {
//                        contactClickListener.onPrayerRequestClick(request1);
//                    }
//                }
//            });
//            if(prayerRequests.size() > 1) {
//                final PrayerRequest request2 = prayerRequests.get(1);
//                Timber.d("1:"+request2.getTitle());
//                holder.textItem2.setText(request2.getTitle());
//                holder.textItem2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (contactClickListener != null) {
//                            contactClickListener.onPrayerRequestClick(request2);
//                        }
//                    }
//                });
//                if(prayerRequests.size() > 2) {
//                    final PrayerRequest request3 = prayerRequests.get(2);
//                    Timber.d("2:"+request3.getTitle());
//                    holder.textItem3.setText(request3.getTitle());
//                    holder.textItem3.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (contactClickListener != null) {
//                                contactClickListener.onPrayerRequestClick(request3);
//                            }
//                        }
//                    });
//                } else {
                    holder.textItem3.setVisibility(View.GONE);
//                }
//            } else {
                holder.textItem2.setVisibility(View.GONE);
//            }
//        } else {
            holder.textItem1.setVisibility(View.GONE);
//        }
//
        // display profile image
        applyProfilePicture(holder, contact);

        // apply click events
        applyClickEvents(holder, position);

    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }

    public void setAdpaterData(final List<Contact> contacts) {
        this.contacts = contacts;
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
            holder.imgProfile.setColorFilter(contact.getColor());
            holder.txtIconText.setVisibility(View.GONE);
        } else {
            holder.imgProfile.setImageResource(R.drawable.bg_circle);
            holder.imgProfile.setColorFilter(contact.getColor());
            holder.txtIconText.setVisibility(View.VISIBLE);
        }
    }

}
