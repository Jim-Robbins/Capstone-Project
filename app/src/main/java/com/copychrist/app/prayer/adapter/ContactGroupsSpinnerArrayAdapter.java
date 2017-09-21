package com.copychrist.app.prayer.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.ContactGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jim on 8/20/17.
 *
 */

public class ContactGroupsSpinnerArrayAdapter extends ArrayAdapter<ContactGroup> {

    public ContactGroupsSpinnerArrayAdapter(@NonNull Context context, @LayoutRes int resource,
                                            @IdRes int textViewResourceId, @NonNull List<ContactGroup> list) {
        super(context, resource, textViewResourceId, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_spinner, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewHolder != null) {
            final ContactGroup contactGroup = getItem(position);
            if(contactGroup != null) {
                viewHolder.textItem.setText(contactGroup.getName());
            }
        }

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.text_spinner_item) TextView textItem;

        private ViewHolder(final View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
