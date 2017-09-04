package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.data.AppDataSource;
import com.copychrist.app.prayer.data.model.Contact;

/**
 * Created by jim on 8/19/17.
 */

public class AddPrayerRequestPresenterImpl
        implements AddPrayerRequestPresenter {

//    private final AppDataSource dataSource;
    private long contactId;
    private Contact myContact;
    private AddPrayerRequestView myAddView = new AddPrayerRequestView.EmptyAddPrayerRequestView();

    public AddPrayerRequestPresenterImpl(long contactId) {
//        this.dataSource = dataSource;
        this.contactId = contactId;
    }

    @Override
    public void start() {

    }

    //
//    @Override
//    public void setView(AddPrayerRequestView view) {
//        myAddView = view;
//        if(contactId != 0) {
////            dataSource.getContact(contactId, this);
//        }

//        ToDo: Get prayerlists data
//        PrayerListsListAdapter prayerListsListAdapter = new PrayerListsListAdapter(prayerLists);
//        myAddView.showAddPrayerRequestDetails(myContact, prayerListsListAdapter);
    //}

//    @Override
//    public void onContactDataNotAvailable() {
//
//    }
//
//    @Override
//    public void onContactLoaded(Contact contact) {
//
//    }

//    @Override
//    public void clearView() {
//        myAddView = new AddPrayerRequestView.EmptyAddPrayerRequestView();
//    }


    @Override
    public void onSaveNewClick(String title, String desc, String verse, String endDate, String prayerList) {
//        dataSource.addPrayerRequestAsync(contactId, title, desc, verse, endDate, prayerList, this);
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

//    @Override
//    public void onDBSuccess() {
//        myAddView.finish();
//    }
//
//    @Override
//    public void onDBError(Throwable e) {
//        e.printStackTrace();
//        myAddView.showAddPrayerRequestError();
//    }
}
