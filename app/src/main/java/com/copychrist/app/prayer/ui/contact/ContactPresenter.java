package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.ui.BasePresenter;

/**
 * Created by jim on 8/19/17.
 */

public interface ContactPresenter extends BasePresenter<ContactView> {
    void onPrayerRequestClick(long requestId);
    void onAddNewRequestClick();
    void onActiveRequestsClick();
    void onArchiveClick();
    void onContactEditClick(long contactId);
    void onEditClick(long contactId, String firstName, String lastName, String pictureUrl);
    void onContactDeleteClick(long id);
    void onDeleteConfirm(long id);
}
