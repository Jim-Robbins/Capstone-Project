package com.copychrist.app.prayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.Contact;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by jim on 8/17/17.
 */

public class ContactsListAdapter
        extends RecyclerView.Adapter<ContactsListAdapter.ViewHolder>
        implements RealmChangeListener {

    private RealmResults<Contact> contacts;
    private OnContactClickListener contactClickListener;

    public ContactsListAdapter() {}

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,
                                                                     parent,
                                                                     false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Contact contact = contacts.get(position);

        holder.textFirstName.setText(contact.getFirstName());
        holder.textLastName.setText(contact.getLastName());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contactClickListener != null) {
                    contactClickListener.onContactClick(contact.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts != null ? contacts.size() : 0;
    }

    @Override
    public void onChange(Object o) {
        notifyDataSetChanged();
    }

    public void setContactClickListener(final OnContactClickListener onContactClickListener) {
        contactClickListener = onContactClickListener;
    }

    public void setContacts(final RealmResults<Contact> contacts) {
        this.contacts = contacts;
        this.contacts.addChangeListener(this);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_item_container) LinearLayout layoutItem;
        @BindView(R.id.text_first_name) TextView textFirstName;
        @BindView(R.id.text_last_name) TextView textLastName;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnContactClickListener {
        void onContactClick(int id);
    }
}
