package com.copychrist.app.prayer.ui.prayerrequest;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.PrayerRequest;

/**
 * Created by jim on 8/19/17.
 */

public class AddPrayerRequestPresenter implements PrayerRequestContract.AddPresenter {

    private final PrayerRequestService dataService;
    private String contactId;
    private Contact myContact;
    private PrayerRequestContract.View myAddView;

    public AddPrayerRequestPresenter(final PrayerRequestService dataService, String contactId) {
        this.dataService = dataService;
        this.contactId = contactId;
    }

    @Override
    public void setView(PrayerRequestContract.View view) {
        myAddView = view;
        if(contactId != null) {
           // myContact = dataService.getContact(contactId);
        }
        //PrayerListsListAdapter prayerListsListAdapter = new PrayerListsListAdapter(dataService.getAllPrayerLists());
       // myAddView.showAddPrayerRequestDetails(myContact, prayerListsListAdapter);
    }

    @Override
    public void clearView() {
        myAddView = null;
    }


    @Override
    public void onSaveNewClick(String title, String desc, String verse, String endDate, String prayerList) {
       // dataService.addPrayerRequestAsync(contactId, title, desc, verse, endDate, prayerList, this);
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
    public PrayerRequest onUpdateClick(String title, String desc, String verse, String endDate, String prayerList) {
        return null;
    }

    @Override
    public void onBibleVerseItemlick(String bibleVerse) {

    }

    @Override
    public void onScheduleReminderClick() {

    }

    @Override
    public void onArchiveRequest() {

    }

    @Override
    public void onDeleteRequest() {

    }
}
