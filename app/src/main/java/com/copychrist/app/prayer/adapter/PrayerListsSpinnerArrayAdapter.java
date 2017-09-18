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

import com.copychrist.app.prayer.model.PrayerList;

import java.util.List;

/**
 * Created by jim on 8/20/17.
 *
 */

public class PrayerListsSpinnerArrayAdapter extends ArrayAdapter<PrayerList> {

    private final LayoutInflater layoutInflater;
    private final List<PrayerList> list;
    private final int resource;
    private final int txtViewId;

    public PrayerListsSpinnerArrayAdapter(@NonNull Context context, @LayoutRes int resource,
                                          @IdRes int textViewResourceId, @NonNull List<PrayerList> list) {
        super(context, resource, textViewResourceId, list);

        layoutInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.txtViewId = textViewResourceId;
        this.list = list;
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

        PrayerList prayerList = list.get(position);

        TextView txtItem = view.findViewById(txtViewId);
        txtItem.setText(prayerList.getName());
        return view;
    }

}
