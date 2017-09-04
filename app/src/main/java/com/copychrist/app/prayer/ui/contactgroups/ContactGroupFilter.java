package com.copychrist.app.prayer.ui.contactgroups;

import android.os.Bundle;

import com.copychrist.app.prayer.ui.BaseFilter;

/**
 * Created by jim on 9/3/17.
 */

public class ContactGroupFilter extends BaseFilter {

    public final static String KEY_FILTER = BASE_KEY_FILTER + "CONTACT_GROUP_FILTER";
    private FilterType filterType = FilterType.ALL;

    protected ContactGroupFilter(Bundle extras) {
        this.filterExtras = extras;
        this.filterType = (FilterType) extras.getSerializable(KEY_FILTER);
    }

    public static ContactGroupFilter from(FilterType filterType){
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_FILTER, filterType);
        return new ContactGroupFilter(bundle);
    }

    @Override
    public FilterType getFilterType() {
        return filterType;
    }

    public enum FilterType {
        /**
         * Do not filter groups.
         */
        ALL
    }
}
