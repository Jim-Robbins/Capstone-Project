package com.copychrist.app.prayer.ui.contact;

import android.os.Bundle;

import com.copychrist.app.prayer.ui.BaseFilter;

/**
 * Created by jim on 9/3/17.
 */

public class ContactFilter extends BaseFilter {

    public final static String KEY_FILTER = BASE_KEY_FILTER + "CONTACT_FILTER";
    private FilterType filterType = FilterType.ALL;

    protected ContactFilter(Bundle extras) {
        this.filterExtras = extras;
        this.filterType = (FilterType) extras.getSerializable(KEY_FILTER);
    }

    public static ContactFilter from(FilterType filterType){
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_FILTER, filterType);
        return new ContactFilter(bundle);
    }

    @Override
    public FilterType getFilterType() {
        return filterType;
    }

    public enum FilterType {
        ALL,
        REQUESTS,
        ARCHIVED
    }
}
