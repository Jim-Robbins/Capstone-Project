package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.adapter.PrayerListsListAdapter;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.repository.RealmService;

/**
 * Created by jim on 8/19/17.
 */

public class AddPrayerRequestPresenterImpl
        implements AddPrayerRequestPresenter, RealmService.OnTransactionCallback {

    private final RealmService realmService;
    private int contactId;
    private Contact myContact;
    private AddPrayerRequestView myAddView = new AddPrayerRequestView.EmptyAddPrayerRequestView();

    public AddPrayerRequestPresenterImpl(final RealmService realmService, int contactId) {
        this.realmService = realmService;
        this.contactId = contactId;
    }

    @Override
    public void setView(AddPrayerRequestView view) {
        myAddView = view;
        if(contactId != 0) {
            myContact = realmService.getContact(contactId);
        }
        PrayerListsListAdapter prayerListsListAdapter = new PrayerListsListAdapter(realmService.getAllPrayerLists());
        myAddView.showAddPrayerRequestDetails(myContact, prayerListsListAdapter);
    }

    @Override
    public void clearView() {
        myAddView = new AddPrayerRequestView.EmptyAddPrayerRequestView();
    }

    @Override
    public void closeRealm() {
        realmService.closeRealm();
    }

    @Override
    public void onSaveNewClick(String title, String desc, String verse, String endDate, String prayerList) {
        realmService.addPrayerRequestAsync(contactId, title, desc, verse, endDate, prayerList, this);
    }

    @Override
    public void onCancelClick() {

    }

    @Override
    public void onContactIconClick() {

    }

    @Override
    public void onBibleIconClick() {

    }

    @Override
    public void onDateIconClick() {

    }

    @Override
    public void onRealmSuccess() {
        myAddView.finish();
    }

    @Override
    public void onRealmError(Throwable e) {
        e.printStackTrace();
        myAddView.showAddPrayerRequestError();
    }
}
