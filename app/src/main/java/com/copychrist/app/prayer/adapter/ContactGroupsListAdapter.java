package com.copychrist.app.prayer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.data.model.ContactGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jim on 8/20/17.
 */

public class ContactGroupsListAdapter extends CursorAdapter {

    public ContactGroupsListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false);
        ViewHolder viewHolder  = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (cursor != null) {
            final ContactGroup contactGroup = ContactGroup.getContactGroupFromCursor(cursor);
            viewHolder.textItem.setText(contactGroup.getName());
        }
    }

    public class ViewHolder {

        @BindView(R.id.layout_spinner_item_container) LinearLayout layoutItem;
        @BindView(R.id.text_spinner_item) TextView textItem;

        public ViewHolder(final View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
