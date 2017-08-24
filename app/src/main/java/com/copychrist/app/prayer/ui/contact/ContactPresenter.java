package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.ui.BasePresenter;

/**
 * Created by jim on 8/19/17.
 */

public interface ContactPresenter extends BasePresenter<ContactView> {
    void onPrayerRequestClick(int requestId);
    void onAddNewRequestClick();
    void onActiveRequestsClick();
    void onArchiveClick();
    void onContactEditClick(int contactId);
    //void onEditClick(int contactId, String firstName, String lastName);
}
