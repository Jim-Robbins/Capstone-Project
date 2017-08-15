package com.copychrist.app.prayer.ui.contact;

import android.os.Bundle;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.ui.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class ContactsActivity extends BaseActivity implements ContactsView {

    @Inject ContactsPresenter contactsPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
    }

    @Override
    protected Object getModule() {
        return new ContactsModule();
    }

    @Override
    protected void onStart() {
        super.onStart();
        contactsPresenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        contactsPresenter.clearView();
    }

    @Override
    protected void closeRealm() {
        contactsPresenter.closeRealm();
    }

    @Override
    public void showContacts(RealmResults<Contact> contacts) {
//        TODO: Setup adapter
    }

//  TODO: Hook up these methods
//    @Override
//    public void onContactClick(final int id) {
//        contactsPresenter.onContactClick(id);
//    }
//
//    @OnClick(R.id.fab)
//    public void onAddNewContactClick() {
//        contactsPresenter.onAddNewContactClick();
//    }

    @Override
    public void showContactDetailView(int id) {
//        TODO:startActivity(ContactDetailActivity.getStartIntent(this, id));
    }

    @Override
    public void showAddNewContactView() {
//        TODO:startActivity(new Intent(this, AddContactActivity.class));

    }
}
