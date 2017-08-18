package com.copychrist.app.prayer.repository;

import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.model.ContactGroup;

import io.realm.Realm;
import io.realm.RealmResults;

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
                int nextId = results.max("id").intValue() + 1;

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

    private ContactGroup getContactGroup(final String groupName, final Realm realm) {
        return realm.where(ContactGroup.class).equalTo("name", groupName).findFirst();
    }

    public ContactGroup addContactGroup (final String group, final Realm realm) {
        RealmResults<ContactGroup> results = realm.where(ContactGroup.class).findAll();
        int nextId = results.max("order").intValue() + 1;

        ContactGroup contactGroup = realm.createObject(ContactGroup.class);
        contactGroup.setName(group);
        contactGroup.setOrder(nextId);
        contactGroup.setDesc("");
        return contactGroup;

    }

    public interface OnTransactionCallback {
        void onRealmSuccess();
        void onRealmError(final Throwable e);
    }
}
