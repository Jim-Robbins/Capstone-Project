package com.copychrist.app.prayer.model;

import com.copychrist.app.prayer.ui.ViewMode;

import java.util.List;

/**
 * Created by jim on 9/18/17.
 */

public class PresenterState {

    public static class ContactGroupState {
        public static ContactGroup selectedContactGroup;
        public static List<ContactGroup> contactGroups;
        public static List<Contact> contacts;
        private ContactGroupState() {}
        public static void reset() {
            selectedContactGroup = null;
            contactGroups = null;
            contacts = null;
        }
    }


    public static class ContactState {
        public static Contact selectedContact;
        public static List<PrayerRequest> prayerRequests;
        private ContactState() {}
    }

    public static class PrayerRequestState {
        public static ViewMode viewMode;
        public static Contact selectedContact;
        public static PrayerRequest selectedPrayerRequest;
        public static String prayerListKey;
        public static List<BiblePassage> listResults;
        public static List<BiblePassage> nonListResults;
        private PrayerRequestState() {}
    }

    public static class PrayerListState {
        public static PrayerList selectedPrayerList;
        public static List<PrayerListRequest> currentListPrayerListRequests;
        public static List<PrayerListRequest> unselectedPrayerListRequests;
        public static List<PrayerList> prayerLists;
        private PrayerListState() {}
        public static void reset() {
            selectedPrayerList = null;
            currentListPrayerListRequests = null;
            unselectedPrayerListRequests = null;
            prayerLists = null;
        }
    }

}

