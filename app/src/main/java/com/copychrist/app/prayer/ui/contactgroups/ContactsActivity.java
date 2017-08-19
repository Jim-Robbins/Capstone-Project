package com.copychrist.app.prayer.ui.contactgroups;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.adapter.ContactsListAdapter;
import com.copychrist.app.prayer.model.Contact;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.contact.ContactDetailActivity;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class ContactsActivity extends BaseActivity
        implements ContactsView, ContactsListAdapter.OnContactClickListener {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Inject ContactsPresenter contactsPresenter;

    private ContactsListAdapter contactsListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        initList();
    }

    @Override
    protected Object getModule() {
        return new ContactsModule();
    }

    private void initList() {
        contactsListAdapter = new ContactsListAdapter();
        contactsListAdapter.setContactClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(contactsListAdapter);
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
        contactsListAdapter.setContacts(contacts);
    }

    @Override
    public void onContactClick(int id) {
        contactsPresenter.onContactClick(id);
    }

    @OnClick(R.id.fab)
    public void onAddNewContactClick() {
        contactsPresenter.onAddNewContactClick();
    }

    @Override
    public void showContactDetailView(int id) {
        startActivity(ContactDetailActivity.getStartIntent(this, id));
    }

    @Override
    public void showAddNewContactView() {
        AddContactDialog addContactDialog = new AddContactDialog(this, "Family", contactsPresenter);
        addContactDialog.show();
    }

    @Override
    public void showAddContactError() {

    }
}
