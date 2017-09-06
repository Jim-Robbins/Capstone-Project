package com.copychrist.app.prayer.repository;

import android.text.TextUtils;
import android.widget.Toast;

import com.copychrist.app.prayer.model.BibleVerse;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;
import com.copychrist.app.prayer.model.PrayerList;
import com.copychrist.app.prayer.model.PrayerRequest;
import com.copychrist.app.prayer.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import io.databaseReference.DatabaseReference;
import io.databaseReference.DatabaseReferenceList;
import io.databaseReference.List;
import timber.log.Timber;

import static io.databaseReference.internal.android.JsonUtils.stringToDate;

/**
 * Created by jim on 8/14/17.
 * Reference : https://www.thedroidsonroids.com/blog/android/example-databaseReference-mvp-dagger/
 */

public class DatabaseSerivce {
    private final DatabaseReference dbReference;

    public DatabaseSerivce() {
        this.dbReference = FirebaseDatabase.getInstance().getReference();
    }

    private boolean isUserLoggedIn(DataSnapshot snapshot) {
        User user = snapshot.getValue(User.class);

        return (user == null);
    }

//    public String getUid() {
//        return FirebaseAuth.getInstance().getCurrentUser().getUid();
//    }
    /* ========= Public Contact Queries ================ */

    /**
     * Query for entire list of contacts
     * @return
     */
    public List<Contact> getAllContacts() {
        return getAllContactsByDatabaseReference(dbReference);
    }

    /**
     * Look up contact by id
     * @param contactId
     * @return Contact DatabaseReferenceObject
     */
    public Contact getContact(final int contactId) {
        return getContactByDatabaseReference(contactId, dbReference);
    }

    /**
     * Query on first and last name to see if the contact exists
     * @param firstName
     * @param lastName
     * @return List/<Contact/>
     */
    public List<Contact> getContactByName(final String firstName, final String lastName) {
        return getContactByNameByDatabaseReference(firstName, lastName, dbReference);
    }

    private List<Contact> getAllContactsByDatabaseReference(DatabaseReference databaseReference) {
        return databaseReference.child(Contact.DB_NAME).child();
    }

    private Contact getContactByDatabaseReference(final int contactId, DatabaseReference databaseReference) {
        return databaseReference.where(Contact.class).equalTo("id", contactId).findFirst();
    }

    private List<Contact> getContactByNameByDatabaseReference(final String firstName,
                                                         final String lastName,
                                                         DatabaseReference databaseReference) {
        return databaseReference.where(Contact.class)
                .equalTo("firstName", firstName)
                .equalTo("lastName", lastName)
                .findAll();
    }


    /**
     * Add new contact to the dbReference db
     * @param firstName
     * @param lastName
     * @param pictureUrl
     * @param groupName
     * @param onTransactionCallback
     */
    public void addContact(final String firstName, final String lastName, final String pictureUrl,
                           final String groupName, final OnTransactionCallback onTransactionCallback) {

        Contact contact = new Contact(groupName, firstName, lastName, pictureUrl, null);
        ContactGroup group = createOrGetContactGroup(groupName, contact);

        if(groupName.isEmpty())
            contact.setGroup(group.getName());

        dbReference.child(Contact.DB_NAME).child(contact.getId()).setValue(contact);

        onTransactionCallback();
    }

    /**
     * Edit and existing Contact DatabaseReferenceObject
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
        editContactByDatabaseReference(contactId, firstName, lastName, pictureUrl, groupName,
                onTransactionCallback, dbReference);
    }

    private void editContactByDatabaseReference(final int contactId, final String firstName, final String lastName,
                                final String pictureUrl, final String group,
                                final OnTransactionCallback onTransactionCallback,
                                DatabaseReference databaseReference) {




        databaseReference.executeTransactionAsync(new DatabaseReference.Transaction() {
            @Override
            public void execute(DatabaseReference databaseReference) {
                final Contact contact = getContactByDatabaseReference(contactId, databaseReference);
                    contact.setFirstName(firstName);
                    contact.setLastName(lastName);
                    contact.setPictureUrl(pictureUrl);
                    if (group != null) {
                        ContactGroup contactGroup = contact.getGroup();
                        if(contactGroup != null && contactGroup.getName() != group) {
                            contactGroup.getContacts().remove(contact);
                            if(!TextUtils.isEmpty(group)) {
                                contact.setGroup(createOrGetContactGroup(group, contact, databaseReference));
                            }
                        }
                    } else {
                        contact.setGroup(null);
                    }
                }
            }, new DatabaseReference.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    if (onTransactionCallback != null) {
                        onTransactionCallback.onDatabaseReferenceSuccess();
                    }
                }
            }, new DatabaseReference.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    if (onTransactionCallback != null) {
                        Timber.e(error);
                        onTransactionCallback.onDatabaseReferenceError(error);
                    }
                }
            });
    }

    /**
     * Remove contact
     * @param contactId
     */
    public void deleteContact(final int contactId) {
        final Contact contact = getContact(contactId);
        if(contact != null) {
            dbReference.executeTransaction(new DatabaseReference.Transaction() {
                @Override
                public void execute(DatabaseReference databaseReference) {
                    contact.getGroup().getContacts().remove(contact);
                    contact.deleteFromDatabaseReference();
                }
            });
        }
    }

    /* ========= Contacts Group Queries ================ */

    /**
     * Return all Contact Groups
     * @return List<ContactGroup>
     */
    public List<ContactGroup> getAllContactGroups() {
        return getAllContactGroupsByDatabaseReference(dbReference);
    }

    /**
     * Look up Contact Group by groupName
     * @param groupName
     * @return ContactGroup
     */
    public ContactGroup getContactGroupByName(final String groupName) {
        return getContactGroupByNameByDatabaseReference(groupName, dbReference);
    }

    /**
     * Look up Contact Group by id
     * @param groupId
     * @return ContactGroup
     */
    public ContactGroup getContactGroup(final int groupId) {
        return getContactGroupByDatabaseReference(groupId, dbReference);
    }

    /**
     * Add a new Contact Group object
     * @param groupName
     * @param groupDesc
     */
    public void addContactGroup(final String groupName, final String groupDesc) {
        dbReference.executeTransaction(new DatabaseReference.Transaction() {
            @Override
            public void execute(DatabaseReference databaseReference) {
                addContactGroupByDatabaseReference(groupName, groupDesc, databaseReference);
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
        dbReference.executeTransaction(new DatabaseReference.Transaction() {
            @Override
            public void execute(DatabaseReference databaseReference) {
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
            dbReference.executeTransaction(new DatabaseReference.Transaction() {
                @Override
                public void execute(DatabaseReference databaseReference) {
//                    DatabaseReferenceList<Contact> contacts = contactGroup.getContacts();
//                    for (Contact contact : contacts) {
//                        editContactByDatabaseReference(contact.getId(), contact.getFirstName(),
//                                contact.getLastName(), contact.getPictureUrl(),
//                                null, null, dbReference);
//                    }
                    contactGroup.deleteFromDatabaseReference();
                }
            });
        }
    }

    // ToDo: Add ability to resort contact groups

    private List<ContactGroup> getAllContactGroupsByDatabaseReference(DatabaseReference databaseReference) {
        return databaseReference.where(ContactGroup.class).findAll();
    }

    private ContactGroup getContactGroupByNameByDatabaseReference(final String groupName, final DatabaseReference databaseReference) {
        return databaseReference.where(ContactGroup.class).equalTo("name", groupName).findFirst();
    }

    private ContactGroup getContactGroupByDatabaseReference(final int groupId, final DatabaseReference databaseReference) {
        return databaseReference.where(ContactGroup.class).equalTo("id", groupId).findFirst();
    }

    private ContactGroup createOrGetContactGroup(final String groupName, final Contact contact=) {
        ContactGroup contactGroup = getContactGroupByNameByDatabaseReference(groupName, dbReference);
        if(contactGroup == null) {
            contactGroup = addContactGroupByDatabaseReference(groupName, "", dbReference);
        }
        contactGroup.getContacts().add(contact);
        return contactGroup;
    }

    private ContactGroup addContactGroupByDatabaseReference (final String groupName, final String groupDesc, final DatabaseReference databaseReference) {
        int nextId = getNextId(getAllContactGroupsByDatabaseReference(databaseReference), "id");

        ContactGroup contactGroup = databaseReference.createObject(ContactGroup.class, nextId);
        contactGroup.setName(groupName);
        contactGroup.setOrder(nextId);
        contactGroup.setDesc(groupDesc);
        return contactGroup;
    }


    /* ========= Prayer List Queries ================ */

    public List<PrayerList> getAllPrayerLists() {
        return getAllPrayerListsByDatabaseReference(dbReference);
    }

    public PrayerList getPrayerListByName(String listName) {
        return getPrayerListByNameByDatabaseReference(listName, dbReference);
    }

    public PrayerList getPrayerList(int listId) {
        return getPrayerListByDatabaseReference(listId, dbReference);
    }

    public void addPrayerList(final String listName) {
        dbReference.executeTransaction(new DatabaseReference.Transaction() {
            @Override
            public void execute(DatabaseReference databaseReference) {
                addPrayerListByDatabaseReference(listName, databaseReference);
            }
        });
    }

    public void editPrayerList(final int listId, final String listName) {
        dbReference.executeTransaction(new DatabaseReference.Transaction() {
            @Override
            public void execute(DatabaseReference databaseReference) {
                editPrayerListByDatabaseReference(listId, listName, databaseReference);
            }
        });
    }

    private List<PrayerList> getAllPrayerListsByDatabaseReference(DatabaseReference databaseReference) {
        return databaseReference.where(PrayerList.class).findAll();
    }

    private PrayerList addPrayerListByDatabaseReference(String listName, DatabaseReference databaseReference) {
        int nextId = getNextId(getAllPrayerListsByDatabaseReference(databaseReference), "id");
        PrayerList prayerList = databaseReference.createObject(PrayerList.class, nextId);
        prayerList.setName(listName);
        prayerList.setOrder(nextId);
        return prayerList;
    }

    private PrayerList editPrayerListByDatabaseReference(int listId, String listName, DatabaseReference databaseReference) {
        PrayerList prayerList = getPrayerListByDatabaseReference(listId, databaseReference);
        if(prayerList != null) {
            if(!TextUtils.isEmpty(listName)) {
                prayerList.setName(listName);
            }
        }
        return prayerList;
    }

    //Todo : allow sorting of lists

    private PrayerList getPrayerListByNameByDatabaseReference(String listName, DatabaseReference databaseReference) {
        return databaseReference.where(PrayerList.class).equalTo("name", listName).findFirst();
    }

    private PrayerList getPrayerListByDatabaseReference(int listId, DatabaseReference databaseReference) {
        return databaseReference.where(PrayerList.class).equalTo("id", listId).findFirst();
    }

    /* ========= Prayer Request Queries ================ */

    public List<PrayerRequest> getActiveRequestsByContact(Contact contact) {
        return contact.getRequests().where().isNull("answered").findAll();
    }

    public List<PrayerRequest> getArchivedRequestsByContact(Contact contact) {
        return contact.getRequests().where().isNotNull("answered").findAll();
    }

    public List<PrayerRequest> getAllPrayerRequests() {
        return getAllPrayerRequestsByDatabaseReference(dbReference);
    }

    public PrayerRequest getPrayerRequest(int prayerRequestId) {
        return getPrayerRequestByDatabaseReference(prayerRequestId, dbReference);
    }

    private List<PrayerRequest> getAllPrayerRequestsByDatabaseReference(DatabaseReference databaseReference) {
        return databaseReference.where(PrayerRequest.class).findAll();
    }

    private PrayerRequest getPrayerRequestByDatabaseReference(int prayerRequestId, DatabaseReference databaseReference) {
        return databaseReference.where(PrayerRequest.class).equalTo("id", prayerRequestId).findFirst();
    }

    public void addPrayerRequestAsync(final int contactId, final String title,
                                           final String desc, final String verse,
                                           final String endDate, final String prayerListName,
                                           final OnTransactionCallback onTransactionCallback) {

        final int nextId = getNextId(getAllPrayerRequests());
        dbReference.executeTransactionAsync(new DatabaseReference.Transaction() {
            @Override
            public void execute(DatabaseReference databaseReference) {
                PrayerRequest prayerRequest = databaseReference.createObject(PrayerRequest.class, nextId);
                prayerRequest.setTitle(title);
                prayerRequest.setDescription(desc);
                prayerRequest.setEndDate(stringToDate(endDate));

                //Add contact to request and request to contact
                Contact contact = getContactByDatabaseReference(contactId, databaseReference);
                prayerRequest.setContact(contact);
                contact.getRequests().add(prayerRequest);

                if(!TextUtils.isEmpty(verse)) {
                    BibleVerse bibleVerse = createOrGetBibleVerse(verse, "", "", "KJV", databaseReference);
                    prayerRequest.getVerses().add(bibleVerse);
                }

                //Add prayer list to request and request to prayer list
                if(!TextUtils.isEmpty(prayerListName)) {
                    PrayerList prayerlist = getPrayerListByNameByDatabaseReference(prayerListName, databaseReference);
                    if (prayerlist != null) {
                        prayerlist.getRequests().add(prayerRequest);
                    }
                }
            }
        }, new DatabaseReference.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (onTransactionCallback != null) {
                    onTransactionCallback.onDatabaseReferenceSuccess();
                }
            }
        }, new DatabaseReference.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (onTransactionCallback != null) {
                    Timber.e(error);
                    onTransactionCallback.onDatabaseReferenceError(error);
                }
            }
        });
    }

    public PrayerRequest editPrayerRequest(final int prayerRequestId, final String title,
                                      final String desc, final String verse, final String endDate,
                                      final String prayerListName) {

        final PrayerRequest prayerRequest = getPrayerRequest(prayerRequestId);

        dbReference.executeTransaction(new DatabaseReference.Transaction() {
            @Override
            public void execute(DatabaseReference databaseReference) {
                prayerRequest.setTitle(title);
                prayerRequest.setDescription(desc);
                prayerRequest.setEndDate(stringToDate(endDate));

                if(!TextUtils.isEmpty(verse)) {
                    BibleVerse bibleVerse = createOrGetBibleVerse(verse, "", "", "KJV", databaseReference);
                    if (!prayerRequest.getVerses().contains(bibleVerse)) {
                        prayerRequest.getVerses().add(bibleVerse);
                    }
                }

                //Add prayer list to request and request to prayer list
                if(!TextUtils.isEmpty(prayerListName)) {
                    PrayerList prayerlist = getPrayerListByNameByDatabaseReference(prayerListName, databaseReference);
                    if (prayerlist != null && !prayerlist.getRequests().contains(prayerRequest)) {
                        prayerlist.getRequests().add(prayerRequest);
                    }
                }
            }
        });

        return prayerRequest;
    }

    public void prayedForPrayerRequest (int prayerRequestId, final String dateStrToAdd) {
        final PrayerRequest prayerRequest = getPrayerRequest(prayerRequestId);

        if(prayerRequest != null) {
            dbReference.executeTransaction(new DatabaseReference.Transaction() {
                @Override
                public void execute(DatabaseReference databaseReference) {

                    prayerRequest.getPrayedForOn().add(dateStrToAdd);
                }
            });
        }
    }

    public void archivePrayerRequest (int prayerRequestId) {
        final PrayerRequest prayerRequest = getPrayerRequest(prayerRequestId);
        if(prayerRequest != null) {
            dbReference.executeTransaction(new DatabaseReference.Transaction() {
                @Override
                public void execute(DatabaseReference databaseReference) {
                    Calendar now = Calendar.getInstance();
                    prayerRequest.setAnswered(now.getTime());
                }
            });
        }
    }

    public void deletePrayerRequest (int prayerRequestId) {
        final PrayerRequest prayerRequest = getPrayerRequest(prayerRequestId);
        if(prayerRequest != null) {
            dbReference.executeTransaction(new DatabaseReference.Transaction() {
                @Override
                public void execute(DatabaseReference databaseReference) {
                    prayerRequest.deleteFromDatabaseReference();
                }
            });
        }
    }

    /* ========= BibleVerse Queries ================ */

    public BibleVerse getBibleVerse(String verse, String version) {
        return getBibleVerseByDatabaseReference(verse, version, dbReference);
    }

    public BibleVerse getBibleVerseByDatabaseReference(String verse, String version, DatabaseReference databaseReference) {
        return databaseReference.where(BibleVerse.class)
                .equalTo("passage", verse)
                .equalTo("version", version)
                .findFirst();
    }

    public void addBibleVerse(final String verse, final String verseText,
                              final String apiUrl, final String version) {
        dbReference.executeTransaction(new DatabaseReference.Transaction() {
            @Override
            public void execute(DatabaseReference databaseReference) {
                BibleVerse bibleVerse = createOrGetBibleVerse(verse, verseText, apiUrl, version, databaseReference);
            }
        });
    }

    private BibleVerse createOrGetBibleVerse(String verse, String verseText,
                                             String apiUrl, String version,
                                             DatabaseReference databaseReference) {
        BibleVerse bibleVerse = getBibleVerseByDatabaseReference(verse, version, databaseReference);
        if(bibleVerse == null) {
            bibleVerse = addBibleVerseByDatabaseReference(verse, verseText, apiUrl, version, databaseReference);
        }
        return bibleVerse;
    }

    private BibleVerse addBibleVerseByDatabaseReference (String verse, String verseText, String apiUrl,
                                             String version, DatabaseReference databaseReference) {
        if(!verse.contains(" ")) {
            return null;
        }
        String[] reference = verse.split(" ");

        BibleVerse bibleVerse = databaseReference.createObject(BibleVerse.class);
        bibleVerse.setBook(reference[0]);

        if(reference[1].contains(":")) {
            String[] passage = reference[1].split(":");
            bibleVerse.setChapter(Integer.parseInt(passage[0]));
            bibleVerse.setVerse(passage[1]);
        }

        bibleVerse.setPassage(verse);
        bibleVerse.setText(verseText);
        bibleVerse.setApiUrl(apiUrl);
        bibleVerse.setVersion(version);

        return bibleVerse;
    }

    private BibleVerse editBibleVerseByDatabaseReference (String verse, String version, final String verseText,
                                              final String apiUrl, DatabaseReference databaseReference) {
        if(!verse.contains(" ")) {
            return null;
        }

        final BibleVerse bibleVerse = getBibleVerseByDatabaseReference(verse, version, databaseReference);
        if(bibleVerse != null) {
            databaseReference.executeTransaction(new DatabaseReference.Transaction() {
                @Override
                public void execute(DatabaseReference databaseReference) {
                    bibleVerse.setText(verseText);
                    bibleVerse.setApiUrl(apiUrl);
                }
            });
        }

        return bibleVerse;
    }

    private void deleteBibleVerse (String verse, String version) {
        final BibleVerse bibleVerse = getBibleVerseByDatabaseReference(verse, version, dbReference);
        if(bibleVerse != null) {
            dbReference.executeTransaction(new DatabaseReference.Transaction() {
                @Override
                public void execute(DatabaseReference databaseReference) {
                   bibleVerse.deleteFromDatabaseReference();
                }
            });
        }
    }


    /* ========= OnTransactionCallback ================ */

    public interface OnTransactionCallback {
        void onDatabaseReferenceSuccess();
        void onDatabaseReferenceError(final Throwable e);
    }

    /* ========= Utilities ================ */

    private int getNextId(List results) {
        return getNextId(results, "id");
    }

    private int getNextId(List results, String key) {
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
