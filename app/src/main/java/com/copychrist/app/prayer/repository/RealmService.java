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
//    /* ========= Public ContactContract Queries ================ */
//
//    /**
//     * Query for entire list of contacts
//     * @return
//     */
//    public RealmResults<ContactContract> getAllContacts() {
//        return getAllContactsByRealm(realm);
//    }
//
//    /**
//     * Look up contact by id
//     * @param contactId
//     * @return ContactContract RealmObject
//     */
//    public ContactContract getContact(final int contactId) {
//        return getContactByRealm(contactId, realm);
//    }
//
//    /**
//     * Query on first and last name to see if the contact exists
//     * @param firstName
//     * @param lastName
//     * @return RealmResults/<ContactContract/>
//     */
//    public RealmResults<ContactContract> getContactByName(final String firstName, final String lastName) {
//        return getContactByNameByRealm(firstName, lastName, realm);
//    }
//
//    private RealmResults<ContactContract> getAllContactsByRealm(Realm realm) {
//        return realm.where(ContactContract.class).findAll();
//    }
//
//    private ContactContract getContactByRealm(final int contactId, Realm realm) {
//        return realm.where(ContactContract.class).equalTo("id", contactId).findFirst();
//    }
//
//    private RealmResults<ContactContract> getContactByNameByRealm(final String firstName,
//                                                         final String lastName,
//                                                         Realm realm) {
//        return realm.where(ContactContract.class)
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
//                ContactContract contact = realm.createObject(ContactContract.class, nextId);
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
//     * Edit and existing ContactContract RealmObject
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
//                final ContactContract contact = getContactByRealm(contactId, realm);
//                    contact.setFirstName(firstName);
//                    contact.setLastName(lastName);
//                    contact.setPictureUrl(pictureUrl);
//                    if (group != null) {
//                        ContactGroupContract contactGroup = contact.getGroup();
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
//        final ContactContract contact = getContact(contactId);
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
//     * Return all ContactContract Groups
//     * @return RealmResults<ContactGroupContract>
//     */
//    public RealmResults<ContactGroupContract> getAllContactGroups() {
//        return getAllContactGroupsByRealm(realm);
//    }
//
//    /**
//     * Look up ContactContract Group by groupName
//     * @param groupName
//     * @return ContactGroupContract
//     */
//    public ContactGroupContract getContactGroupByName(final String groupName) {
//        return getContactGroupByNameByRealm(groupName, realm);
//    }
//
//    /**
//     * Look up ContactContract Group by id
//     * @param groupId
//     * @return ContactGroupContract
//     */
//    public ContactGroupContract getContactGroup(final int groupId) {
//        return getContactGroupByRealm(groupId, realm);
//    }
//
//    /**
//     * Add a new ContactContract Group object
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
//        final ContactGroupContract contactGroup = getContactGroup(groupId);
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
//        final ContactGroupContract contactGroup = getContactGroup(groupId);
//        if(contactGroup != null) {
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
////                    RealmList<ContactContract> contacts = contactGroup.getContacts();
////                    for (ContactContract contact : contacts) {
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
//    private RealmResults<ContactGroupContract> getAllContactGroupsByRealm(Realm realm) {
//        return realm.where(ContactGroupContract.class).findAll();
//    }
//
//    private ContactGroupContract getContactGroupByNameByRealm(final String groupName, final Realm realm) {
//        return realm.where(ContactGroupContract.class).equalTo("name", groupName).findFirst();
//    }
//
//    private ContactGroupContract getContactGroupByRealm(final int groupId, final Realm realm) {
//        return realm.where(ContactGroupContract.class).equalTo("id", groupId).findFirst();
//    }
//
//    private ContactGroupContract createOrGetContactGroup(final String groupName, final ContactContract contact, final Realm realm) {
//        ContactGroupContract contactGroup = getContactGroupByNameByRealm(groupName, realm);
//        if(contactGroup == null) {
//            contactGroup = addContactGroupByRealm(groupName, "", realm);
//        }
//        contactGroup.getContacts().add(contact);
//        return contactGroup;
//    }
//
//    private ContactGroupContract addContactGroupByRealm (final String groupName, final String groupDesc, final Realm realm) {
//        int nextId = getNextId(getAllContactGroupsByRealm(realm), "id");
//
//        ContactGroupContract contactGroup = realm.createObject(ContactGroupContract.class, nextId);
//        contactGroup.setName(groupName);
//        contactGroup.setOrder(nextId);
//        contactGroup.setDesc(groupDesc);
//        return contactGroup;
//    }
//
//
//    /* ========= Prayer List Queries ================ */
//
//    public RealmResults<PrayerListContract> getAllPrayerLists() {
//        return getAllPrayerListsByRealm(realm);
//    }
//
//    public PrayerListContract getPrayerListByName(String listName) {
//        return getPrayerListByNameByRealm(listName, realm);
//    }
//
//    public PrayerListContract getPrayerList(int listId) {
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
//    private RealmResults<PrayerListContract> getAllPrayerListsByRealm(Realm realm) {
//        return realm.where(PrayerListContract.class).findAll();
//    }
//
//    private PrayerListContract addPrayerListByRealm(String listName, Realm realm) {
//        int nextId = getNextId(getAllPrayerListsByRealm(realm), "id");
//        PrayerListContract prayerList = realm.createObject(PrayerListContract.class, nextId);
//        prayerList.setName(listName);
//        prayerList.setOrder(nextId);
//        return prayerList;
//    }
//
//    private PrayerListContract editPrayerListByRealm(int listId, String listName, Realm realm) {
//        PrayerListContract prayerList = getPrayerListByRealm(listId, realm);
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
//    private PrayerListContract getPrayerListByNameByRealm(String listName, Realm realm) {
//        return realm.where(PrayerListContract.class).equalTo("name", listName).findFirst();
//    }
//
//    private PrayerListContract getPrayerListByRealm(int listId, Realm realm) {
//        return realm.where(PrayerListContract.class).equalTo("id", listId).findFirst();
//    }
//
//    /* ========= Prayer Request Queries ================ */
//
//    public RealmResults<PrayerRequestContract> getActiveRequestsByContact(ContactContract contact) {
//        return contact.getRequests().where().isNull("answered").findAll();
//    }
//
//    public RealmResults<PrayerRequestContract> getArchivedRequestsByContact(ContactContract contact) {
//        return contact.getRequests().where().isNotNull("answered").findAll();
//    }
//
//    public RealmResults<PrayerRequestContract> getAllPrayerRequests() {
//        return getAllPrayerRequestsByRealm(realm);
//    }
//
//    public PrayerRequestContract getPrayerRequest(int prayerRequestId) {
//        return getPrayerRequestByRealm(prayerRequestId, realm);
//    }
//
//    private RealmResults<PrayerRequestContract> getAllPrayerRequestsByRealm(Realm realm) {
//        return realm.where(PrayerRequestContract.class).findAll();
//    }
//
//    private PrayerRequestContract getPrayerRequestByRealm(int prayerRequestId, Realm realm) {
//        return realm.where(PrayerRequestContract.class).equalTo("id", prayerRequestId).findFirst();
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
//                PrayerRequestContract prayerRequest = realm.createObject(PrayerRequestContract.class, nextId);
//                prayerRequest.setTitle(title);
//                prayerRequest.setDescription(desc);
//                prayerRequest.setEndDate(stringToDate(endDate));
//
//                //Add contact to request and request to contact
//                ContactContract contact = getContactByRealm(contactId, realm);
//                prayerRequest.setContact(contact);
//                contact.getRequests().add(prayerRequest);
//
//                if(!TextUtils.isEmpty(verse)) {
//                    BibleVerseContract bibleVerse = createOrGetBibleVerse(verse, "", "", "KJV", realm);
//                    prayerRequest.getVerses().add(bibleVerse);
//                }
//
//                //Add prayer list to request and request to prayer list
//                if(!TextUtils.isEmpty(prayerListName)) {
//                    PrayerListContract prayerlist = getPrayerListByNameByRealm(prayerListName, realm);
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
//    public PrayerRequestContract editPrayerRequest(final int prayerRequestId, final String title,
//                                      final String desc, final String verse, final String endDate,
//                                      final String prayerListName) {
//
//        final PrayerRequestContract prayerRequest = getPrayerRequest(prayerRequestId);
//
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                prayerRequest.setTitle(title);
//                prayerRequest.setDescription(desc);
//                prayerRequest.setEndDate(stringToDate(endDate));
//
//                if(!TextUtils.isEmpty(verse)) {
//                    BibleVerseContract bibleVerse = createOrGetBibleVerse(verse, "", "", "KJV", realm);
//                    if (!prayerRequest.getVerses().contains(bibleVerse)) {
//                        prayerRequest.getVerses().add(bibleVerse);
//                    }
//                }
//
//                //Add prayer list to request and request to prayer list
//                if(!TextUtils.isEmpty(prayerListName)) {
//                    PrayerListContract prayerlist = getPrayerListByNameByRealm(prayerListName, realm);
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
//        final PrayerRequestContract prayerRequest = getPrayerRequest(prayerRequestId);
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
//        final PrayerRequestContract prayerRequest = getPrayerRequest(prayerRequestId);
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
//        final PrayerRequestContract prayerRequest = getPrayerRequest(prayerRequestId);
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
//    /* ========= BibleVerseContract Queries ================ */
//
//    public BibleVerseContract getBibleVerse(String verse, String version) {
//        return getBibleVerseByRealm(verse, version, realm);
//    }
//
//    public BibleVerseContract getBibleVerseByRealm(String verse, String version, Realm realm) {
//        return realm.where(BibleVerseContract.class)
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
//                BibleVerseContract bibleVerse = createOrGetBibleVerse(verse, verseText, apiUrl, version, realm);
//            }
//        });
//    }
//
//    private BibleVerseContract createOrGetBibleVerse(String verse, String verseText,
//                                             String apiUrl, String version,
//                                             Realm realm) {
//        BibleVerseContract bibleVerse = getBibleVerseByRealm(verse, version, realm);
//        if(bibleVerse == null) {
//            bibleVerse = addBibleVerseByRealm(verse, verseText, apiUrl, version, realm);
//        }
//        return bibleVerse;
//    }
//
//    private BibleVerseContract addBibleVerseByRealm (String verse, String verseText, String apiUrl,
//                                             String version, Realm realm) {
//        if(!verse.contains(" ")) {
//            return null;
//        }
//        String[] reference = verse.split(" ");
//
//        BibleVerseContract bibleVerse = realm.createObject(BibleVerseContract.class);
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
//    private BibleVerseContract editBibleVerseByRealm (String verse, String version, final String verseText,
//                                              final String apiUrl, Realm realm) {
//        if(!verse.contains(" ")) {
//            return null;
//        }
//
//        final BibleVerseContract bibleVerse = getBibleVerseByRealm(verse, version, realm);
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
//        final BibleVerseContract bibleVerse = getBibleVerseByRealm(verse, version, realm);
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
