package com.copychrist.app.prayer.repository;

import android.util.Log;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;
import com.copychrist.app.prayer.model.PrayerRequest;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import timber.log.Timber;

import static android.R.attr.id;

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

    public Contact getContact(final int contactId) {
        return realm.where(Contact.class).equalTo("id", contactId).findFirst();
    }

    public void addContact(final String firstName, final String lastName, final String pictureUrl,
                           final String group, final OnTransactionCallback onTransactionCallback) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Contact> results = realm.where(Contact.class).findAll();
                int nextId = (results.size() > 0) ? results.max("id").intValue() + 1 : 1;

                Contact contact = realm.createObject(Contact.class);
                contact.setId(nextId);
                contact.setFirstName(firstName);
                contact.setLastName(lastName);
                contact.setPictureUrl(pictureUrl);
                contact.setGroup(createOrGetContactGroup(group, contact, realm));
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

    private ContactGroup createOrGetContactGroup(final String groupName, final Contact contact, final Realm realm) {
        ContactGroup contactGroup = getContactGroup(groupName, realm);
        if(contactGroup == null) {
            contactGroup = addContactGroup(groupName, realm);
        }
        contactGroup.getContacts().add(contact);
        return contactGroup;
    }

    public RealmResults<Contact> getAllContacts() {
        return realm.where(Contact.class).findAll();
    }

    public RealmList<Contact> getContactsGroupBy(final String groupName) {
        return realm.where(ContactGroup.class).equalTo("name", groupName).findFirst().getContacts();
    }

    private ContactGroup getContactGroup(final String groupName, final Realm realm) {
        return realm.where(ContactGroup.class).equalTo("name", groupName).findFirst();
    }

    public ContactGroup addContactGroup (final String group, final Realm realm) {
        RealmResults<ContactGroup> results = realm.where(ContactGroup.class).findAll();
        int nextId = (results.size() > 0) ? results.max("order").intValue() + 1 : 1;

        ContactGroup contactGroup = realm.createObject(ContactGroup.class, group);
        contactGroup.setOrder(nextId);
        contactGroup.setDesc("");
        return contactGroup;

    }

    public interface OnTransactionCallback {
        void onRealmSuccess();
        void onRealmError(final Throwable e);
    }
}
