package com.copychrist.app.prayer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.database.Exclude;

import timber.log.Timber;

/**
 * Created by jim on 9/10/17.
 */

public class PrayerListRequest implements Parcelable {

    @NonNull private Contact contact;

    @NonNull private ContactGroup group;

    @NonNull private PrayerRequest prayerRequest;

    @NonNull private boolean prayedFor = false;

    @NonNull private int color;

    public PrayerListRequest() {
    }

    @NonNull
    public Contact getContact() {
        if (contact == null) {
            Timber.e("Someone forgot to initialize the contact object");
        }
        return contact;
    }
    public void setContact(@NonNull Contact contact) {
        this.contact = contact;
    }

    @NonNull
    public ContactGroup getGroup() {
        if (group == null) {
            Timber.e("Someone forgot to initialize the contact group object");
        }
        return group;
    }
    public void setGroup(@NonNull ContactGroup group) {
        this.group = group;
    }

    @NonNull
    public PrayerRequest getPrayerRequest() {
        if (prayerRequest == null) {
            Timber.e("Someone forgot to initialize the prayer request object");
        }
        return prayerRequest;
    }
    public void setPrayerRequest(@NonNull PrayerRequest prayerRequest) {
        this.prayerRequest = prayerRequest;
    }

    public String getContactName() {
        String value;
        try {
            value = contact.getFirstName() + " " + contact.getLastName();
        } catch (Error e) {
            Timber.e(e.getMessage());
            value = "Failed to load name...";
        }
        return value;
    }

    public String getGroupName() {
        String value;
        try {
            value = group.getName();
        } catch (Error e) {
            Timber.e(e.getMessage());
            value = "Failed to load name...";
        }
        return value;
    }

    public String getTitle() {
        String value;
        try {
            value = prayerRequest.getTitle();
        } catch (Error e) {
            Timber.e(e.getMessage());
            value = "Failed to load title...";
        }
        return value;
    }

    public String getDesc() {
        String value;
        try {
            value = prayerRequest.getDescription();
        } catch (Error e) {
            Timber.e(e.getMessage());
            value = "Failed to load desc...";
        }
        return value;
    }

    public Long getEndDate() {
        Long value;
        try {
            value = prayerRequest.getEndDate();
        } catch (Error e) {
            Timber.e(e.getMessage());
            value = Utils.getCurrentTime();
        }
        return value;
    }

    public String getPicture() {
        String value;
        try {
            value = contact.getPictureUrl();
        } catch (Error e) {
            Timber.e(e.getMessage());
            value = "Failed to find image name";
        }
        return value;
    }

    public String getKey() {
        return prayerRequest.getKey();
    }

    public boolean isPrayedFor() {
        return prayedFor;
    }
    public void setPrayedFor(boolean b) {
        prayedFor = true;
    }

    public int getColor() {
        return color;
    }
    public void setColor(@NonNull int color) {
        this.color = color;
    }

    //-------------- Create Parcel----------------

    protected PrayerListRequest(Parcel in) {
        contact = (Contact) in.readValue(Contact.class.getClassLoader());
        group = (ContactGroup) in.readValue(ContactGroup.class.getClassLoader());
        prayerRequest = (PrayerRequest) in.readValue(PrayerRequest.class.getClassLoader());
        prayedFor = in.readByte() != 0x00;
        color = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(contact);
        dest.writeValue(group);
        dest.writeValue(prayerRequest);
        dest.writeByte((byte) (prayedFor ? 0x01 : 0x00));
        dest.writeInt(color);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PrayerListRequest> CREATOR = new Parcelable.Creator<PrayerListRequest>() {
        @Override
        public PrayerListRequest createFromParcel(Parcel in) {
            return new PrayerListRequest(in);
        }

        @Override
        public PrayerListRequest[] newArray(int size) {
            return new PrayerListRequest[size];
        }
    };

}
