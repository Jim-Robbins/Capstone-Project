package com.copychrist.app.prayer.ui.contact;

import com.copychrist.app.prayer.ui.BasePresenter;

/**
 * Created by jim on 8/14/17.
 */

public interface ContactsPresenter extends BasePresenter<ContactsView> {
    void onContactClick(int id);
    void onAddNewContactClick();
}
