package com.copychrist.app.prayer.ui.contactgroups;

import com.copychrist.app.prayer.ui.BasePresenter;

/**
 * Created by jim on 8/14/17.
 */

public interface ContactsPresenter extends BasePresenter<ContactsView> {
    void onContactGroupClicked(long contactGroupId);
    void onAddNewContactGroupClick();
    void onEditContactGroupClick();
    void onSaveContactGroupClick(long contactGroupId, String groupName, String groupDesc, int sortOrder);
    void onDeleteContactGroupClick();
    void onDeleteContactGroupConfirmed();
    void onAddNewContactClick();
    void onSaveContactClick(String firstName, String lastName, String groupName, String pictureUrl);
    void onContactClick(long contactId);
    void onPrayerRequestClick(long prayerRequestId);
}
