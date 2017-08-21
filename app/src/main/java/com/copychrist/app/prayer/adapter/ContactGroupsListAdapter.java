package com.copychrist.app.prayer.adapter;

import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.ContactGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by jim on 8/20/17.
 */

public class ContactGroupsListAdapter extends RealmBaseAdapter<ContactGroup> implements ListAdapter {

    public ContactGroupsListAdapter(@Nullable OrderedRealmCollection<ContactGroup> realmResults) {
        super(realmResults);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_spinner, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (adapterData != null) {
            final ContactGroup contactGroup = adapterData.get(position);
            viewHolder.textItem.setText(contactGroup.getName());
        }

        return view;
    }

    public class ViewHolder {

        @BindView(R.id.layout_spinner_item_container) LinearLayout layoutItem;
        @BindView(R.id.text_spinner_item) TextView textItem;

        public ViewHolder(final View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
