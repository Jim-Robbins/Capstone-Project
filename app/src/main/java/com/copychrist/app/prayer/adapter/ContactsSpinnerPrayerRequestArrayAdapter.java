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

import com.copychrist.app.prayer.model.Contact;

import java.util.List;

/**
 * Created by jim on 9/16/17.
 * Ref: http://www.zoftino.com/android-spinner-custom-adapter-&-layout
 */

public class ContactsSpinnerPrayerRequestArrayAdapter extends ArrayAdapter<Contact>{

    private final LayoutInflater layoutInflater;
    private final Context context;
    private final List<Contact> items;
    private final int resource;
    private final int txtViewId;

    public ContactsSpinnerPrayerRequestArrayAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Contact> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.txtViewId = textViewResourceId;
        items = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = layoutInflater.inflate(resource, parent, false);

        Contact contact = items.get(position);

        TextView txtItem = (TextView) view.findViewById(txtViewId);
        txtItem.setText(contact.getContactGroup().getName() + " : " + contact.getFirstName() + " " + contact.getLastName());
        return view;
    }
}
