package com.copychrist.app.prayer.repository;

/**
 * Created by jim on 8/14/17.
 * Reference : https://www.thedroidsonroids.com/blog/android/example-realm-mvp-dagger/
 */

public class RealmService {
//    private final Realm realm;
//
//    public RealmService(final Realm realm) {
//        this.realm = realm;
//    }
//
//    public void closeRealm() {
//        realm.close();
//    }
//
//    /* ========= Public ContactEntry Queries ================ */
//
//    /**
//     * Query for entire list of contacts
//     * @return
//     */
//    public RealmResults<ContactEntry> getAllContacts() {
//        return getAllContactsByRealm(realm);
//    }
//
//    /**
//     * Look up contact by id
//     * @param contactId
//     * @return ContactEntry RealmObject
//     */
//    public ContactEntry getContact(final int contactId) {
//        return getContactByRealm(contactId, realm);
//    }
//
//    /**
//     * Query on first and last name to see if the contact exists
//     * @param firstName
//     * @param lastName
//     * @return RealmResults/<ContactEntry/>
//     */
//    public RealmResults<ContactEntry> getContactByName(final String firstName, final String lastName) {
//        return getContactByNameByRealm(firstName, lastName, realm);
//    }
//
//    private RealmResults<ContactEntry> getAllContactsByRealm(Realm realm) {
//        return realm.where(ContactEntry.class).findAll();
//    }
//
//    private ContactEntry getContactByRealm(final int contactId, Realm realm) {
//        return realm.where(ContactEntry.class).equalTo("id", contactId).findFirst();
//    }
//
//    private RealmResults<ContactEntry> getContactByNameByRealm(final String firstName,
//                                                         final String lastName,
//                                                         Realm realm) {
//        return realm.where(ContactEntry.class)
//                .equalTo("firstName", firstName)
//                .equalTo("lastName", lastName)
//                .findAll();
//    }
//
//
//    /**
//     * Add new contact to the realm db
//     * @param firstName
//     * @param lastName
//     * @param pictureUrl
//     * @param groupName
//     * @param onTransactionCallback
//     */
//    public void addContact(final String firstName, final String lastName, final String pictureUrl,
//                           final String groupName, final OnTransactionCallback onTransactionCallback) {
//
//        final int nextId = getNextId(getAllContacts());
//
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                ContactEntry contact = realm.createObject(ContactEntry.class, nextId);
//                contact.setFirstName(firstName);
//                contact.setLastName(lastName);
//                contact.setPictureUrl(pictureUrl);
//                contact.setGroup(createOrGetContactGroup(groupName, contact, realm));
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                if (onTransactionCallback != null) {
//                    onTransactionCallback.onRealmSuccess();
//                }
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                if (onTransactionCallback != null) {
//                    Timber.e(error);
//                    onTransactionCallback.onRealmError(error);
//                }
//            }
//        });
//    }
//
//    /**
//     * Edit and existing ContactEntry RealmObject
//     * @param contactId
//     * @param firstName
//     * @param lastName
//     * @param pictureUrl
//     * @param groupName
//     * @param onTransactionCallback
//     */
//    public void editContact(final int contactId, final String firstName, final String lastName,
//                            final String pictureUrl, final String groupName,
//                            final OnTransactionCallback onTransactionCallback) {
//        editContactByRealm(contactId, firstName, lastName, pictureUrl, groupName,
//                onTransactionCallback, realm);
//    }
//
//    private void editContactByRealm(final int contactId, final String firstName, final String lastName,
//                                final String pictureUrl, final String group,
//                                final OnTransactionCallback onTransactionCallback,
//                                Realm realm) {
//
//
//
//
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                final ContactEntry contact = getContactByRealm(contactId, realm);
//                    contact.setFirstName(firstName);
//                    contact.setLastName(lastName);
//                    contact.setPictureUrl(pictureUrl);
//                    if (group != null) {
//                        ContactGroupEntry contactGroup = contact.getGroup();
//                        if(contactGroup != null && contactGroup.getName() != group) {
//                            contactGroup.getContacts().remove(contact);
//                            if(!TextUtils.isEmpty(group)) {
//                                contact.setGroup(createOrGetContactGroup(group, contact, realm));
//                            }
//                        }
//                    } else {
//                        contact.setGroup(null);
//                    }
//                }
//            }, new Realm.Transaction.OnSuccess() {
//                @Override
//                public void onSuccess() {
//                    if (onTransactionCallback != null) {
//                        onTransactionCallback.onRealmSuccess();
//                    }
//                }
//            }, new Realm.Transaction.OnError() {
//                @Override
//                public void onError(Throwable error) {
//                    if (onTransactionCallback != null) {
//                        Timber.e(error);
//                        onTransactionCallback.onRealmError(error);
//                    }
//                }
//            });
//    }
//
//    /**
//     * Remove contact
//     * @param contactId
//     */
//    public void deleteContact(final int contactId) {
//        final ContactEntry contact = getContact(contactId);
//        if(contact != null) {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    contact.getGroup().getContacts().remove(contact);
//                    contact.deleteFromRealm();
//                }
//            });
//        }
//    }
//
//    /* ========= Contacts Group Queries ================ */
//
//    /**
//     * Return all ContactEntry Groups
//     * @return RealmResults<ContactGroupEntry>
//     */
//    public RealmResults<ContactGroupEntry> getAllContactGroups() {
//        return getAllContactGroupsByRealm(realm);
//    }
//
//    /**
//     * Look up ContactEntry Group by groupName
//     * @param groupName
//     * @return ContactGroupEntry
//     */
//    public ContactGroupEntry getContactGroupByName(final String groupName) {
//        return getContactGroupByNameByRealm(groupName, realm);
//    }
//
//    /**
//     * Look up ContactEntry Group by id
//     * @param groupId
//     * @return ContactGroupEntry
//     */
//    public ContactGroupEntry getContactGroup(final int groupId) {
//        return getContactGroupByRealm(groupId, realm);
//    }
//
//    /**
//     * Add a new ContactEntry Group object
//     * @param groupName
//     * @param groupDesc
//     */
//    public void addContactGroup(final String groupName, final String groupDesc) {
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                addContactGroupByRealm(groupName, groupDesc, realm);
//            }
//        });
//    }
//
//    /**
//     * Edit an existing contact group, find group, then replace values
//     * @param groupId
//     * @param groupName
//     * @param groupDesc
//     * @param sortOrder
//     */
//    public void editContactGroup(final int groupId, final String groupName,
//                                 final String groupDesc, final int sortOrder) {
//        final ContactGroupEntry contactGroup = getContactGroup(groupId);
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                if (contactGroup != null) {
//                    if(contactGroup.getName() != groupName) {
//                        contactGroup.setName(groupName);
//                    }
//                    if(groupDesc != contactGroup.getDesc()) {
//                        contactGroup.setDesc(groupDesc);
//                    }
//                    if(sortOrder != contactGroup.getOrder()) {
//                        contactGroup.setOrder(sortOrder);
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * Remove contact group
//     * @param groupId
//     */
//    public void deleteContactGroup(final int groupId) {
//        final ContactGroupEntry contactGroup = getContactGroup(groupId);
//        if(contactGroup != null) {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
////                    RealmList<ContactEntry> contacts = contactGroup.getContacts();
////                    for (ContactEntry contact : contacts) {
////                        editContactByRealm(contact.getId(), contact.getFirstName(),
////                                contact.getLastName(), contact.getPictureUrl(),
////                                null, null, realm);
////                    }
//                    contactGroup.deleteFromRealm();
//                }
//            });
//        }
//    }
//
//    // ToDo: Add ability to resort contact groups
//
//    private RealmResults<ContactGroupEntry> getAllContactGroupsByRealm(Realm realm) {
//        return realm.where(ContactGroupEntry.class).findAll();
//    }
//
//    private ContactGroupEntry getContactGroupByNameByRealm(final String groupName, final Realm realm) {
//        return realm.where(ContactGroupEntry.class).equalTo("name", groupName).findFirst();
//    }
//
//    private ContactGroupEntry getContactGroupByRealm(final int groupId, final Realm realm) {
//        return realm.where(ContactGroupEntry.class).equalTo("id", groupId).findFirst();
//    }
//
//    private ContactGroupEntry createOrGetContactGroup(final String groupName, final ContactEntry contact, final Realm realm) {
//        ContactGroupEntry contactGroup = getContactGroupByNameByRealm(groupName, realm);
//        if(contactGroup == null) {
//            contactGroup = addContactGroupByRealm(groupName, "", realm);
//        }
//        contactGroup.getContacts().add(contact);
//        return contactGroup;
//    }
//
//    private ContactGroupEntry addContactGroupByRealm (final String groupName, final String groupDesc, final Realm realm) {
//        int nextId = getNextId(getAllContactGroupsByRealm(realm), "id");
//
//        ContactGroupEntry contactGroup = realm.createObject(ContactGroupEntry.class, nextId);
//        contactGroup.setName(groupName);
//        contactGroup.setOrder(nextId);
//        contactGroup.setDesc(groupDesc);
//        return contactGroup;
//    }
//
//
//    /* ========= Prayer List Queries ================ */
//
//    public RealmResults<PrayerListEntry> getAllPrayerLists() {
//        return getAllPrayerListsByRealm(realm);
//    }
//
//    public PrayerListEntry getPrayerListByName(String listName) {
//        return getPrayerListByNameByRealm(listName, realm);
//    }
//
//    public PrayerListEntry getPrayerList(int listId) {
//        return getPrayerListByRealm(listId, realm);
//    }
//
//    public void addPrayerList(final String listName) {
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                addPrayerListByRealm(listName, realm);
//            }
//        });
//    }
//
//    public void editPrayerList(final int listId, final String listName) {
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                editPrayerListByRealm(listId, listName, realm);
//            }
//        });
//    }
//
//    private RealmResults<PrayerListEntry> getAllPrayerListsByRealm(Realm realm) {
//        return realm.where(PrayerListEntry.class).findAll();
//    }
//
//    private PrayerListEntry addPrayerListByRealm(String listName, Realm realm) {
//        int nextId = getNextId(getAllPrayerListsByRealm(realm), "id");
//        PrayerListEntry prayerList = realm.createObject(PrayerListEntry.class, nextId);
//        prayerList.setName(listName);
//        prayerList.setOrder(nextId);
//        return prayerList;
//    }
//
//    private PrayerListEntry editPrayerListByRealm(int listId, String listName, Realm realm) {
//        PrayerListEntry prayerList = getPrayerListByRealm(listId, realm);
//        if(prayerList != null) {
//            if(!TextUtils.isEmpty(listName)) {
//                prayerList.setName(listName);
//            }
//        }
//        return prayerList;
//    }
//
//    //Todo : allow sorting of lists
//
//    private PrayerListEntry getPrayerListByNameByRealm(String listName, Realm realm) {
//        return realm.where(PrayerListEntry.class).equalTo("name", listName).findFirst();
//    }
//
//    private PrayerListEntry getPrayerListByRealm(int listId, Realm realm) {
//        return realm.where(PrayerListEntry.class).equalTo("id", listId).findFirst();
//    }
//
//    /* ========= Prayer Request Queries ================ */
//
//    public RealmResults<PrayerRequestEntry> getActiveRequestsByContact(ContactEntry contact) {
//        return contact.getRequests().where().isNull("answered").findAll();
//    }
//
//    public RealmResults<PrayerRequestEntry> getArchivedRequestsByContact(ContactEntry contact) {
//        return contact.getRequests().where().isNotNull("answered").findAll();
//    }
//
//    public RealmResults<PrayerRequestEntry> getAllPrayerRequests() {
//        return getAllPrayerRequestsByRealm(realm);
//    }
//
//    public PrayerRequestEntry getPrayerRequest(int prayerRequestId) {
//        return getPrayerRequestByRealm(prayerRequestId, realm);
//    }
//
//    private RealmResults<PrayerRequestEntry> getAllPrayerRequestsByRealm(Realm realm) {
//        return realm.where(PrayerRequestEntry.class).findAll();
//    }
//
//    private PrayerRequestEntry getPrayerRequestByRealm(int prayerRequestId, Realm realm) {
//        return realm.where(PrayerRequestEntry.class).equalTo("id", prayerRequestId).findFirst();
//    }
//
//    public void addPrayerRequestAsync(final int contactId, final String title,
//                                           final String desc, final String verse,
//                                           final String endDate, final String prayerListName,
//                                           final OnTransactionCallback onTransactionCallback) {
//
//        final int nextId = getNextId(getAllPrayerRequests());
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                PrayerRequestEntry prayerRequest = realm.createObject(PrayerRequestEntry.class, nextId);
//                prayerRequest.setTitle(title);
//                prayerRequest.setDescription(desc);
//                prayerRequest.setEndDate(stringToDate(endDate));
//
//                //Add contact to request and request to contact
//                ContactEntry contact = getContactByRealm(contactId, realm);
//                prayerRequest.setContact(contact);
//                contact.getRequests().add(prayerRequest);
//
//                if(!TextUtils.isEmpty(verse)) {
//                    BibleVerseEntry bibleVerse = createOrGetBibleVerse(verse, "", "", "KJV", realm);
//                    prayerRequest.getVerses().add(bibleVerse);
//                }
//
//                //Add prayer list to request and request to prayer list
//                if(!TextUtils.isEmpty(prayerListName)) {
//                    PrayerListEntry prayerlist = getPrayerListByNameByRealm(prayerListName, realm);
//                    if (prayerlist != null) {
//                        prayerlist.getRequests().add(prayerRequest);
//                    }
//                }
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                if (onTransactionCallback != null) {
//                    onTransactionCallback.onRealmSuccess();
//                }
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                if (onTransactionCallback != null) {
//                    Timber.e(error);
//                    onTransactionCallback.onRealmError(error);
//                }
//            }
//        });
//    }
//
//    public PrayerRequestEntry editPrayerRequest(final int prayerRequestId, final String title,
//                                      final String desc, final String verse, final String endDate,
//                                      final String prayerListName) {
//
//        final PrayerRequestEntry prayerRequest = getPrayerRequest(prayerRequestId);
//
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                prayerRequest.setTitle(title);
//                prayerRequest.setDescription(desc);
//                prayerRequest.setEndDate(stringToDate(endDate));
//
//                if(!TextUtils.isEmpty(verse)) {
//                    BibleVerseEntry bibleVerse = createOrGetBibleVerse(verse, "", "", "KJV", realm);
//                    if (!prayerRequest.getVerses().contains(bibleVerse)) {
//                        prayerRequest.getVerses().add(bibleVerse);
//                    }
//                }
//
//                //Add prayer list to request and request to prayer list
//                if(!TextUtils.isEmpty(prayerListName)) {
//                    PrayerListEntry prayerlist = getPrayerListByNameByRealm(prayerListName, realm);
//                    if (prayerlist != null && !prayerlist.getRequests().contains(prayerRequest)) {
//                        prayerlist.getRequests().add(prayerRequest);
//                    }
//                }
//            }
//        });
//
//        return prayerRequest;
//    }
//
//    public void prayedForPrayerRequest (int prayerRequestId, final String dateStrToAdd) {
//        final PrayerRequestEntry prayerRequest = getPrayerRequest(prayerRequestId);
//
//        if(prayerRequest != null) {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//
//                    prayerRequest.getPrayedForOn().add(dateStrToAdd);
//                }
//            });
//        }
//    }
//
//    public void archivePrayerRequest (int prayerRequestId) {
//        final PrayerRequestEntry prayerRequest = getPrayerRequest(prayerRequestId);
//        if(prayerRequest != null) {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    Calendar now = Calendar.getInstance();
//                    prayerRequest.setAnswered(now.getTime());
//                }
//            });
//        }
//    }
//
//    public void deletePrayerRequest (int prayerRequestId) {
//        final PrayerRequestEntry prayerRequest = getPrayerRequest(prayerRequestId);
//        if(prayerRequest != null) {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    prayerRequest.deleteFromRealm();
//                }
//            });
//        }
//    }
//
//    /* ========= BibleVerseEntry Queries ================ */
//
//    public BibleVerseEntry getBibleVerse(String verse, String version) {
//        return getBibleVerseByRealm(verse, version, realm);
//    }
//
//    public BibleVerseEntry getBibleVerseByRealm(String verse, String version, Realm realm) {
//        return realm.where(BibleVerseEntry.class)
//                .equalTo("passage", verse)
//                .equalTo("version", version)
//                .findFirst();
//    }
//
//    public void addBibleVerse(final String verse, final String verseText,
//                              final String apiUrl, final String version) {
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                BibleVerseEntry bibleVerse = createOrGetBibleVerse(verse, verseText, apiUrl, version, realm);
//            }
//        });
//    }
//
//    private BibleVerseEntry createOrGetBibleVerse(String verse, String verseText,
//                                             String apiUrl, String version,
//                                             Realm realm) {
//        BibleVerseEntry bibleVerse = getBibleVerseByRealm(verse, version, realm);
//        if(bibleVerse == null) {
//            bibleVerse = addBibleVerseByRealm(verse, verseText, apiUrl, version, realm);
//        }
//        return bibleVerse;
//    }
//
//    private BibleVerseEntry addBibleVerseByRealm (String verse, String verseText, String apiUrl,
//                                             String version, Realm realm) {
//        if(!verse.contains(" ")) {
//            return null;
//        }
//        String[] reference = verse.split(" ");
//
//        BibleVerseEntry bibleVerse = realm.createObject(BibleVerseEntry.class);
//        bibleVerse.setBook(reference[0]);
//
//        if(reference[1].contains(":")) {
//            String[] passage = reference[1].split(":");
//            bibleVerse.setChapter(Integer.parseInt(passage[0]));
//            bibleVerse.setVerse(passage[1]);
//        }
//
//        bibleVerse.setPassage(verse);
//        bibleVerse.setText(verseText);
//        bibleVerse.setApiUrl(apiUrl);
//        bibleVerse.setVersion(version);
//
//        return bibleVerse;
//    }
//
//    private BibleVerseEntry editBibleVerseByRealm (String verse, String version, final String verseText,
//                                              final String apiUrl, Realm realm) {
//        if(!verse.contains(" ")) {
//            return null;
//        }
//
//        final BibleVerseEntry bibleVerse = getBibleVerseByRealm(verse, version, realm);
//        if(bibleVerse != null) {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    bibleVerse.setText(verseText);
//                    bibleVerse.setApiUrl(apiUrl);
//                }
//            });
//        }
//
//        return bibleVerse;
//    }
//
//    private void deleteBibleVerse (String verse, String version) {
//        final BibleVerseEntry bibleVerse = getBibleVerseByRealm(verse, version, realm);
//        if(bibleVerse != null) {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                   bibleVerse.deleteFromRealm();
//                }
//            });
//        }
//    }
//
//
//    /* ========= OnTransactionCallback ================ */
//
//    public interface OnTransactionCallback {
//        void onRealmSuccess();
//        void onRealmError(final Throwable e);
//    }
//
//    /* ========= Utilities ================ */
//
//    private int getNextId(RealmResults results) {
//        return getNextId(results, "id");
//    }
//
//    private int getNextId(RealmResults results, String key) {
//        return (results.size() > 0) ? results.max(key).intValue() + 1 : 1;
//    }
//
//    private Date stringToDate(String dateString) {
//        if (TextUtils.isEmpty(dateString))
//            return null;
//
//        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
//        Date newDate = new Date();
//        try {
//            newDate = format.parse(dateString);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return newDate;
//    }
}
