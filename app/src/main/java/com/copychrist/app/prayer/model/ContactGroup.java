package com.copychrist.app.prayer.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ContactGroup {
    public static final String DB_NAME = "contactGroups";
    public static final String CREATED = "dateCreated";
    public static final String ORDER_BY = "order";

    @Exclude
    private String key;

    @NonNull
    private Long dateCreated;

    @Nullable
    private String description;

    @NonNull
    private String name;

    @NonNull
    private int order;

    public ContactGroup() {
        // Default constructor required for calls to DataSnapshot.getContact()
    }

    /**
     * Auto-generated Firebase key
     * @return key [String] - Used to reference data value set for this group
     */
    @Exclude
    public String getKey() {
        return key;
    }
    /**
     * Not editable by user, stores reference to Firebase key
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Display name for the Contact Group
     * @return name [String] - Display Name for group
     */
    @NonNull
    public String getName() {
        return name;
    }
    public void setName(@NonNull String name) {
        this.name = name;
    }

    /**
     * Optional group description field, currently just a notation to the user.
     * @return description [String]
     */
    @Nullable
    public String getDescription() {
        return description;
    }
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    /**
     * Auto value set when group is pushed to Firebase, used to aid in sorting.
     * User is not currently able to resort the data, but plan to add that later.
     * @return sort order [int]
     */
    @NonNull
    public int getOrder() {
        return order;
    }
    public void setOrder(@NonNull int order) {
        this.order = order;
    }

    /**
     * System generated timestamp when contact group is created
     * @return
     */
    @NonNull
    public Long getDateCreated() {
        if(dateCreated == null) {
            dateCreated = Utils.getCurrentTime();
        }
        return dateCreated;
    }

    @Exclude
    @Override
    public String toString() {
        return "ContactGroup {\n" +
                "   key = " + getKey() + ",\n " +
                "   dateCreated = " + getDateCreated() + ",\n " +
                "   description = " + getDescription() + ",\n " +
                "   name = " + getName() + ",\n " +
                "   order = " + getOrder() +
                "\n}";
    }

    public void clone(ContactGroup contactGroup) {
        setKey(contactGroup.key);
        setName(contactGroup.name);
        setDescription(contactGroup.description);
        setOrder(contactGroup.order);
    }
}
