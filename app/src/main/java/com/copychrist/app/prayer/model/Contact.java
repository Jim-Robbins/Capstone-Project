package com.copychrist.app.prayer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Contact implements Parcelable {
    public static final String DB_NAME = "contacts";

    @Exclude
    private String key;

    @NonNull
    private Long dateCreated;
    public static final String CHILD_DATE_CREATED = "dateCreated";

    @NonNull
    private String contactGroupKey;
    public static final String CHILD_CONTACT_GROUP_KEY = "contactGroupKey";

    @NonNull
    private ContactGroup contactGroup;
    public static final String CHILD_CONTACT_GROUP = "contactGroup";

    @NonNull
    private String firstName;
    public static final String CHILD_FIRST_NAME = "firstName";

    @Nullable
    private String lastName;
    public static final String CHILD_LAST_NAME = "lastName";

    @Nullable
    private String pictureUrl;
    public static final String CHILD_PICTURE_URL = "pictureUrl";

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
    public String getContactGroupKey() {
        return contactGroupKey;
    }
    public void setContactGroupKey(String contactGroupKey) {
        this.contactGroupKey = contactGroupKey;
    }

    @NonNull
    public ContactGroup getContactGroup() {
        return contactGroup;
    }
    public void setContactGroup(ContactGroup contactGroup) {
        this.contactGroup = contactGroup;
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
                ", contactGroupKey='" + contactGroupKey + '\'' +
                ", contactGroup='" + contactGroup.toString() + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pictureUrl=" + pictureUrl + '\'' +
                "}";
    }

    //-------------- Create Parcel----------------

    protected Contact(Parcel in) {
        key = in.readString();
        dateCreated = in.readByte() == 0x00 ? null : in.readLong();
        contactGroupKey = in.readString();
        contactGroup = (ContactGroup) in.readValue(ContactGroup.class.getClassLoader());
        firstName = in.readString();
        lastName = in.readString();
        pictureUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        if (dateCreated == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(dateCreated);
        }
        dest.writeString(contactGroupKey);
        dest.writeValue(contactGroup);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(pictureUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}