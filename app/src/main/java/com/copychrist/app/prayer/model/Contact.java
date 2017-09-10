package com.copychrist.app.prayer.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Contact  {
    public static final String DB_NAME = "contacts";
    public static final String CREATED = "dateCreated";
    public static final String ORDER_BY = CREATED;
    public static final String CONTACT_GROUP = "groupKey";

    @Exclude
    private String key;

    @NonNull
    private Long dateCreated;

    @NonNull
    private String groupKey;

    @NonNull
    private String firstName;

    @Nullable
    private String lastName;

    @Nullable
    private String pictureUrl;

    public Contact() {
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

    @NonNull
    public String getGroupKey() {
        return groupKey;
    }
    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    @Nullable
    public String getLastName() {
        return lastName;
    }
    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    @Nullable
    public String getPictureUrl() {
        return pictureUrl;
    }
    public void setPictureUrl(@Nullable String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Exclude
    @Override
    public String toString() {
        return "ContactEntry {"+
                "key='" + key + '\'' +
                ", groupKey='" + groupKey + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pictureUrl=" + pictureUrl + '\'' +
                "}";
    }
}