package com.copychrist.app.prayer.repository;

import android.text.TextUtils;

import com.copychrist.app.prayer.model.BibleVerse;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;
import com.copychrist.app.prayer.model.PrayerList;
import com.copychrist.app.prayer.model.PrayerRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import timber.log.Timber;

import static android.R.attr.format;
import static io.realm.internal.android.JsonUtils.stringToDate;

/**
 * Created by jim on 8/14/17.
 * Reference : https://www.thedroidsonroids.com/blog/android/example-realm-mvp-dagger/
 */

public class RealmService {
    private final Realm realm;

    public RealmService(final Realm realm) {
        this.realm = realm;
    }

    public void closeRealm() {
        realm.close();
    }

    /* ========= Public Contact Queries ================ */

    /**
     * Query for entire list of contacts
     * @return
     */
    public RealmResults<Contact> getAllContacts() {
        return getAllContactsByRealm(realm);
    }

    /**
     * Look up contact by id
     * @param contactId
     * @return Contact RealmObject
     */
    public Contact getContact(final int contactId) {
        return getContactByRealm(contactId, realm);
    }

    /**
     * Query on first and last name to see if the contact exists
     * @param firstName
     * @param lastName
     * @return RealmResults/<Contact/>
     */
    public RealmResults<Contact> getContactByName(final String firstName, final String lastName) {
        return getContactByNameByRealm(firstName, lastName, realm);
    }

    private RealmResults<Contact> getAllContactsByRealm(Realm realm) {
        return realm.where(Contact.class).findAll();
    }

    private Contact getContactByRealm(final int contactId, Realm realm) {
        return realm.where(Contact.class).equalTo("id", contactId).findFirst();
    }

    private RealmResults<Contact> getContactByNameByRealm(final String firstName,
                                                         final String lastName,
                                                         Realm realm) {
        return realm.where(Contact.class)
                .equalTo("firstName", firstName)
                .equalTo("lastName", lastName)
                .findAll();
    }


    /**
     * Add new contact to the realm db
     * @param firstName
     * @param lastName
     * @param pictureUrl
     * @param groupName
     * @param onTransactionCallback
     */
    public void addContact(final String firstName, final String lastName, final String pictureUrl,
                           final String groupName, final OnTransactionCallback onTransactionCallback) {

        final int nextId = getNextId(getAllContacts());

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Contact contact = realm.createObject(Contact.class, nextId);
                contact.setFirstName(firstName);
                contact.setLastName(lastName);
                contact.setPictureUrl(pictureUrl);
                contact.setGroup(createOrGetContactGroup(groupName, contact, realm));
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (onTransactionCallback != null) {
                    Timber.e(error);
                    onTransactionCallback.onRealmError(error);
                }
            }
        });
    }

    /**
     * Edit and existing Contact RealmObject
     * @param contactId
     * @param firstName
     * @param lastName
     * @param pictureUrl
     * @param groupName
     * @param onTransactionCallback
     */
    public void editContact(final int contactId, final String firstName, final String lastName,
                            final String pictureUrl, final String groupName,
                            final OnTransactionCallback onTransactionCallback) {
        editContactByRealm(contactId, firstName, lastName, pictureUrl, groupName,
                onTransactionCallback, realm);
    }

    private void editContactByRealm(final int contactId, final String firstName, final String lastName,
                                final String pictureUrl, final String group,
                                final OnTransactionCallback onTransactionCallback,
                                Realm realm) {

        final Contact contact = getContactByRealm(contactId, realm);

        if(contact != null) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    contact.setFirstName(firstName);
                    contact.setLastName(lastName);
                    contact.setPictureUrl(pictureUrl);
                    if (group != null) {
                        ContactGroup contactGroup = contact.getGroup();
                        if(contactGroup != null && contactGroup.getName() != group) {
                            contactGroup.getContacts().remove(contact);
                            if(!TextUtils.isEmpty(group)) {
                                contact.setGroup(createOrGetContactGroup(group, contact, realm));
                            }
                        }
                    } else {
                        contact.setGroup(null);
                    }
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    if (onTransactionCallback != null) {
                        onTransactionCallback.onRealmSuccess();
                    }
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    if (onTransactionCallback != null) {
                        Timber.e(error);
                        onTransactionCallback.onRealmError(error);
                    }
                }
            });
        } else {
            Timber.e("Contact does not exist");
            Throwable error = new Throwable();
            onTransactionCallback.onRealmError(error);
        }
    }

    /**
     * Remove contact
     * @param contactId
     */
    public void deleteContact(final int contactId) {
        final Contact contact = getContact(contactId);
        if(contact != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    contact.getGroup().getContacts().remove(contact);
                    contact.deleteFromRealm();
                }
            });
        }
    }

    /* ========= Contacts Group Queries ================ */

    /**
     * Return all Contact Groups
     * @return RealmResults<ContactGroup>
     */
    public RealmResults<ContactGroup> getAllContactGroups() {
        return getAllContactGroupsByRealm(realm);
    }

    /**
     * Look up Contact Group by groupName
     * @param groupName
     * @return ContactGroup
     */
    public ContactGroup getContactGroupByName(final String groupName) {
        return getContactGroupByNameByRealm(groupName, realm);
    }

    /**
     * Look up Contact Group by id
     * @param groupId
     * @return ContactGroup
     */
    public ContactGroup getContactGroup(final int groupId) {
        return getContactGroupByRealm(groupId, realm);
    }

    /**
     * Add a new Contact Group object
     * @param groupName
     * @param groupDesc
     */
    public void addContactGroup(final String groupName, final String groupDesc) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                addContactGroupByRealm(groupName, groupDesc, realm);
            }
        });
    }

    /**
     * Edit an existing contact group, find group, then replace values
     * @param groupId
     * @param groupName
     * @param groupDesc
     * @param sortOrder
     */
    public void editContactGroup(final int groupId, final String groupName,
                                 final String groupDesc, final int sortOrder) {
        final ContactGroup contactGroup = getContactGroup(groupId);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (contactGroup != null) {
                    if(contactGroup.getName() != groupName) {
                        contactGroup.setName(groupName);
                    }
                    if(groupDesc != contactGroup.getDesc()) {
                        contactGroup.setDesc(groupDesc);
                    }
                    if(sortOrder != contactGroup.getOrder()) {
                        contactGroup.setOrder(sortOrder);
                    }
                }
            }
        });
    }

    /**
     * Remove contact group
     * @param groupId
     */
    public void deleteContactGroup(final int groupId) {
        final ContactGroup contactGroup = getContactGroup(groupId);
        if(contactGroup != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
//                    RealmList<Contact> contacts = contactGroup.getContacts();
//                    for (Contact contact : contacts) {
//                        editContactByRealm(contact.getId(), contact.getFirstName(),
//                                contact.getLastName(), contact.getPictureUrl(),
//                                null, null, realm);
//                    }
                    contactGroup.deleteFromRealm();
                }
            });
        }
    }

    // ToDo: Add ability to resort contact groups

    private RealmResults<ContactGroup> getAllContactGroupsByRealm(Realm realm) {
        return realm.where(ContactGroup.class).findAll();
    }

    private ContactGroup getContactGroupByNameByRealm(final String groupName, final Realm realm) {
        return realm.where(ContactGroup.class).equalTo("name", groupName).findFirst();
    }

    private ContactGroup getContactGroupByRealm(final int groupId, final Realm realm) {
        return realm.where(ContactGroup.class).equalTo("id", groupId).findFirst();
    }

    private ContactGroup createOrGetContactGroup(final String groupName, final Contact contact, final Realm realm) {
        ContactGroup contactGroup = getContactGroupByNameByRealm(groupName, realm);
        if(contactGroup == null) {
            contactGroup = addContactGroupByRealm(groupName, "", realm);
        }
        contactGroup.getContacts().add(contact);
        return contactGroup;
    }

    private ContactGroup addContactGroupByRealm (final String groupName, final String groupDesc, final Realm realm) {
        int nextId = getNextId(getAllContactGroupsByRealm(realm), "id");

        ContactGroup contactGroup = realm.createObject(ContactGroup.class, nextId);
        contactGroup.setName(groupName);
        contactGroup.setOrder(nextId);
        contactGroup.setDesc(groupDesc);
        return contactGroup;
    }


    /* ========= Prayer List Queries ================ */

    public RealmResults<PrayerList> getAllPrayerLists() {
        return getAllPrayerListsByRealm(realm);
    }

    public PrayerList getPrayerListByName(String listName) {
        return getPrayerListByNameByRealm(listName, realm);
    }

    public PrayerList getPrayerList(int listId) {
        return getPrayerListByRealm(listId, realm);
    }

    public void addPrayerList(final String listName) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                addPrayerListByRealm(listName, realm);
            }
        });
    }

    public void editPrayerList(final int listId, final String listName) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                editPrayerListByRealm(listId, listName, realm);
            }
        });
    }

    private RealmResults<PrayerList> getAllPrayerListsByRealm(Realm realm) {
        return realm.where(PrayerList.class).findAll();
    }

    private PrayerList addPrayerListByRealm(String listName, Realm realm) {
        int nextId = getNextId(getAllPrayerListsByRealm(realm), "id");
        PrayerList prayerList = realm.createObject(PrayerList.class, nextId);
        prayerList.setName(listName);
        prayerList.setOrder(nextId);
        return prayerList;
    }

    private PrayerList editPrayerListByRealm(int listId, String listName, Realm realm) {
        PrayerList prayerList = getPrayerListByRealm(listId, realm);
        if(prayerList != null) {
            if(!TextUtils.isEmpty(listName)) {
                prayerList.setName(listName);
            }
        }
        return prayerList;
    }

    //Todo : allow sorting of lists

    private PrayerList getPrayerListByNameByRealm(String listName, Realm realm) {
        return realm.where(PrayerList.class).equalTo("name", listName).findFirst();
    }

    private PrayerList getPrayerListByRealm(int listId, Realm realm) {
        return realm.where(PrayerList.class).equalTo("id", listId).findFirst();
    }

    /* ========= Prayer Request Queries ================ */

    public RealmResults<PrayerRequest> getActiveRequestsByContact(Contact contact) {
        return contact.getRequests().where().isNull("answered").findAll();
    }

    public RealmResults<PrayerRequest> getArchivedRequestsByContact(Contact contact) {
        return contact.getRequests().where().isNotNull("answered").findAll();
    }

    public RealmResults<PrayerRequest> getAllPrayerRequests() {
        return getAllPrayerRequestsByRealm(realm);
    }

    public PrayerRequest getPrayerRequest(int prayerRequestId) {
        return getPrayerRequestByRealm(prayerRequestId, realm);
    }

    private RealmResults<PrayerRequest> getAllPrayerRequestsByRealm(Realm realm) {
        return realm.where(PrayerRequest.class).findAll();
    }

    private PrayerRequest getPrayerRequestByRealm(int prayerRequestId, Realm realm) {
        return realm.where(PrayerRequest.class).equalTo("id", prayerRequestId).findFirst();
    }

    // Todo: hook up verse
    public void addPrayerRequestAsync(final int contactId, final String title,
                                           final String desc, final String verse, final String endDate,
                                           final String prayerListName,
                                           final OnTransactionCallback onTransactionCallback) {

        final int nextId = getNextId(getAllPrayerRequests());
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PrayerRequest prayerRequest = realm.createObject(PrayerRequest.class, nextId);
                prayerRequest.setTitle(title);
                prayerRequest.setDescription(desc);
                prayerRequest.setEndDate(stringToDate(endDate));

                //Add contact to request and request to contact
                Contact contact = getContactByRealm(contactId, realm);
                prayerRequest.setContact(contact);
                contact.getRequests().add(prayerRequest);

                if(!TextUtils.isEmpty(verse)) {
                    BibleVerse bibleVerse = createOrGetBibleVerse(verse, realm);
                    prayerRequest.getVerses().add(bibleVerse);
                }

                //Add prayer list to request and request to prayer list
                if(!TextUtils.isEmpty(prayerListName)) {
                    PrayerList prayerlist = getPrayerListByNameByRealm(prayerListName, realm);
                    if (prayerlist != null) {
                        prayerlist.getRequests().add(prayerRequest);
                    }
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onRealmSuccess();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (onTransactionCallback != null) {
                    Timber.e(error);
                    onTransactionCallback.onRealmError(error);
                }
            }
        });
    }

    public PrayerRequest editPrayerRequest(final int prayerRequestId, final String title,
                                      final String desc, final String verse, final String endDate,
                                      final String prayerListName) {

        final PrayerRequest prayerRequest = getPrayerRequest(prayerRequestId);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                prayerRequest.setTitle(title);
                prayerRequest.setDescription(desc);
                prayerRequest.setEndDate(stringToDate(endDate));

                if(!TextUtils.isEmpty(verse)) {
                    BibleVerse bibleVerse = createOrGetBibleVerse(verse, realm);
                    if (!prayerRequest.getVerses().contains(bibleVerse)) {
                        prayerRequest.getVerses().add(bibleVerse);
                    }
                }

                //Add prayer list to request and request to prayer list
                if(!TextUtils.isEmpty(prayerListName)) {
                    PrayerList prayerlist = getPrayerListByNameByRealm(prayerListName, realm);
                    if (prayerlist != null && !prayerlist.getRequests().contains(prayerRequest)) {
                        prayerlist.getRequests().add(prayerRequest);
                    }
                }
            }
        });

        return prayerRequest;
    }

    /* ========= BibleVerse Queries ================ */

    public BibleVerse getBibleVerse(String verse) {
        return getBibleVerseByRealm(verse, realm);
    }

    public BibleVerse getBibleVerseByRealm(String verse, Realm realm) {
        return realm.where(BibleVerse.class)
                .equalTo("passage", verse)
                .findFirst();
//        BibleVerse tempVerseObject = parseVerseIntoObject(verse);
//
//        return realm.where(BibleVerse.class)
//                .equalTo("book", tempVerseObject.getBook())
//                .equalTo("chapter", tempVerseObject.getChapter())
//                .equalTo("verse", tempVerseObject.getVerse())
//                .findFirst();
    }

    public void addBibleVerse(final String verse) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                BibleVerse bibleVerse = createOrGetBibleVerse(verse, realm);
            }
        });
    }

    private BibleVerse createOrGetBibleVerse(final String verse, final Realm realm) {
        BibleVerse bibleVerse = getBibleVerseByRealm(verse, realm);
        if(bibleVerse == null) {
            bibleVerse = addBibleVerseByRealm(verse, realm);
        }
        return bibleVerse;
    }

    private BibleVerse addBibleVerseByRealm (final String verse, final Realm realm) {
        if(!verse.contains(" ")) {
            return null;
        }
        String[] reference = verse.split(" ");

        BibleVerse bibleVerse = realm.createObject(BibleVerse.class);
        bibleVerse.setBook(reference[0]);

        if(reference[1].contains(":")) {
            String[] passage = reference[1].split(":");
            bibleVerse.setChapter(Integer.parseInt(passage[0]));
            bibleVerse.setVerse(passage[1]);
        }

        bibleVerse.setPassage(verse);

        return bibleVerse;
    }

    /* ========= OnTransactionCallback ================ */

    public interface OnTransactionCallback {
        void onRealmSuccess();
        void onRealmError(final Throwable e);
    }

    /* ========= Utilities ================ */

    private int getNextId(RealmResults results) {
        return getNextId(results, "id");
    }

    private int getNextId(RealmResults results, String key) {
        return (results.size() > 0) ? results.max(key).intValue() + 1 : 1;
    }

    private Date stringToDate(String dateString) {
        if (TextUtils.isEmpty(dateString))
            return null;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date newDate = new Date();
        try {
            newDate = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }
}
