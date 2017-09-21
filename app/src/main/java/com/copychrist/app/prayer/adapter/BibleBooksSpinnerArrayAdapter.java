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

import com.faithcomesbyhearing.dbt.model.Book;

import java.util.List;

/**
 * Created by jim on 9/16/17.
 * Ref: http://www.zoftino.com/android-spinner-custom-adapter-&-layout
 */

public class BibleBooksSpinnerArrayAdapter extends ArrayAdapter<Book>{

    private final LayoutInflater layoutInflater;
    private final List<Book> list;
    private final int resource;
    private final int txtViewId;

    public BibleBooksSpinnerArrayAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Book> list) {
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

        Book book = list.get(position);

        TextView txtItem = view.findViewById(txtViewId);
        txtItem.setText(book.getBookId());
        return view;
    }
}
