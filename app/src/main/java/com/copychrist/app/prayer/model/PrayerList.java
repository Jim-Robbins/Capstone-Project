package com.copychrist.app.prayer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class PrayerList implements Parcelable {
    public static String DB_NAME = "prayerLists";

    @Exclude
    private String key;

    @NonNull
    private String name;
    public static final String CHILD_NAME = "name";

    @NonNull
    private int sortOrder;
    public static final String CHILD_ORDER = "sortOrder";

    @NonNull private Long dateCreated;
    public static final String CHILD_DATE_CREATED = "dateCreated";

    public PrayerList() {}

    /**
     * Auto-generated Firebase key
     * @return key [String] - Used to reference data value set for this list
     */
    @Exclude @NonNull public String getKey() {
        return key;
    }
    public void setKey(@NonNull String key) {
        this.key = key;
    }

    /**
     * System generated timestamp when prayer request is created
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
    public String getName() {
        return name;
    }
    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public int getSortOrder() {
        return sortOrder;
    }
    public void setSortOrder(@NonNull int sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Exclude
    @Override
    public String toString() {
        return "ContactEntry {"+
                "key='" + key + '\'' +
                ", name ='" + name + '\'' +
                ", order ='" + sortOrder + '\'' +
                "}";
    }

    //-------------- Create Parcel----------------

    protected PrayerList(Parcel in) {
        key = in.readString();
        name = in.readString();
        sortOrder = in.readInt();
        dateCreated = in.readByte() == 0x00 ? null : in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeInt(sortOrder);
        if (dateCreated == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(dateCreated);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PrayerList> CREATOR = new Parcelable.Creator<PrayerList>() {
        @Override
        public PrayerList createFromParcel(Parcel in) {
            return new PrayerList(in);
        }

        @Override
        public PrayerList[] newArray(int size) {
            return new PrayerList[size];
        }
    };
}
