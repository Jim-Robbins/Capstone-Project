package com.copychrist.app.prayer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.data.model.Contact;
import com.copychrist.app.prayer.data.model.PrayerRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by jim on 8/17/17.
 */

public class ContactsListAdapter extends CursorRecyclerViewAdapter<ContactsListAdapter.ViewHolder> {

     public ContactsListAdapter(Context context){
         super(context);
     }

    private OnContactClickListener contactClickListener;

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        final Contact contact = Contact.getFrom(cursor);

        holder.textFirstName.setText(contact.getFirstName());
        holder.textLastName.setText(contact.getLastName());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contactClickListener != null) {
                    contactClickListener.onContactClick(contact);
                }
            }
        });

        holder.textItem3.setVisibility(View.GONE);
        holder.textItem2.setVisibility(View.GONE);
        holder.textItem1.setVisibility(View.GONE);
//        //Todo: Need to figure out how to get list of requests in a simple cursor
//        List<PrayerRequest> prayerRequests = contact.getRequests();
//        if(prayerRequests.size() > 0) {
//            final PrayerRequest request1 = prayerRequests.get(0);
//            Timber.d("0:"+request1.getTitle());
//            holder.textItem1.setText(request1.getTitle());
//            holder.textItem1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (contactClickListener != null) {
//                        contactClickListener.onPrayerRequestClick(request1.getId());
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
//                            contactClickListener.onPrayerRequestClick(request2.getId());
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
//                                contactClickListener.onPrayerRequestClick(request3.getId());
//                            }
//                        }
//                    });
//                } else {
//                    holder.textItem3.setVisibility(View.GONE);
//                }
//            } else {
//                holder.textItem2.setVisibility(View.GONE);
//            }
//        } else {
//            holder.textItem1.setVisibility(View.GONE);
//        }

//        holder.textMoreItems.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (contactClickListener != null) {
//                    contactClickListener.onContactClick(contact.getId());
//                }
//            }
//        });
    }

    @Override
    public void setCursor(Cursor cursor) {
        changeCursor(cursor);
    }

    public void setContactClickListener(final OnContactClickListener onContactClickListener) {
        contactClickListener = onContactClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contact_container) RelativeLayout layoutItem;
        @BindView(R.id.text_first_name) TextView textFirstName;
        @BindView(R.id.text_last_name) TextView textLastName;
        @BindView(R.id.item1) TextView textItem1;
        @BindView(R.id.item2) TextView textItem2;
        @BindView(R.id.item3) TextView textItem3;
        @BindView(R.id.more_items) TextView textMoreItems;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnContactClickListener {
        void onContactClick(@NonNull Contact contact);
        void onPrayerRequestClick(@NonNull PrayerRequest prayerRequest);
    }
}
