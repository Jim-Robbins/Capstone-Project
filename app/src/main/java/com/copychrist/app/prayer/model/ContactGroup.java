package com.copychrist.app.prayer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ContactGroup implements Parcelable {
    public static final String DB_NAME = "contactGroups";

    @Exclude
    private String key;

    @NonNull
    private Long dateCreated;
    public static final String CHILD_DATE_CREATED = "dateCreated";

    @Nullable
    private String description;
    public static final String CHILD_DESCRIPTION = "description";

    @NonNull
    private String name;
    public static final String CHILD_NAME = "name";

    @NonNull
    private int sortOrder;
    public static final String CHILD_SORT_ORDER = "sortOrder";

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
     * @return sort sortOrder [int]
     */
    @NonNull
    public int getSortOrder() {
        return sortOrder;
    }
    public void setSortOrder(@NonNull int sortOrder) {
        this.sortOrder = sortOrder;
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
                "   sortOrder = " + getSortOrder() +
                "\n}";
    }


    //-------------- Create Parcel----------------

    protected ContactGroup(Parcel in) {
        key = in.readString();
        dateCreated = in.readByte() == 0x00 ? null : in.readLong();
        description = in.readString();
        name = in.readString();
        sortOrder = in.readInt();
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
        dest.writeString(description);
        dest.writeString(name);
        dest.writeInt(sortOrder);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ContactGroup> CREATOR = new Parcelable.Creator<ContactGroup>() {
        @Override
        public ContactGroup createFromParcel(Parcel in) {
            return new ContactGroup(in);
        }

        @Override
        public ContactGroup[] newArray(int size) {
            return new ContactGroup[size];
        }
    };
}
