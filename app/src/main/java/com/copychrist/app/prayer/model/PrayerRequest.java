package com.copychrist.app.prayer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@IgnoreExtraProperties
public class PrayerRequest implements Parcelable {
    public static final String DB_NAME = "prayerRequests";

    @NonNull private String key;

    @NonNull private String contactKey;
    public static final String CHILD_CONTACT_KEY = "contactKey";

    @NonNull private Contact contact;
    public static final String CHILD_CONTACT = "contact";

    @NonNull private String title;
    public static final String CHILD_TITLE = "title";

    @Nullable private String description;
    public static final String CHILD_DESCRIPTION = "description";

    @Nullable private Long endDate;
    public static final String CHILD_END_DATE = "endDate";

    @Nullable private List<String> passages = new ArrayList<>();
    public static final String CHILD_PASSAGES = "passages";

    @Nullable private Long lastPrayedFor;
    public static final String CHILD_LAST_PRAYED_FOR = "lastPrayedFor";

    @Nullable private Long answered;
    public static final String CHILD_ANSWERED = "answered";

    @NonNull private Long dateCreated;
    public static final String CHILD_DATE_CREATED = "dateCreated";

    @Nullable private HashMap<String, Boolean> prayerLists = new HashMap<String, Boolean>();
    public static final String CHILD_PRAYER_LISTS = "prayerLists";

    public PrayerRequest() {
        // Default constructor required for calls to DataSnapshot.getContact()
    }

    /**
     * Auto-generated Firebase key
     * @return key [String] - Used to reference data value set for this request
     */
    @Exclude public String getKey() {
        return key;
    }
    public void setKey(String key) {
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

    @NonNull public String getContactKey() {
        return contactKey;
    }
    public void setContactKey(String contactKey) {
        this.contactKey = contactKey;
    }

    @NonNull
    public Contact getContact() {
        return contact;
    }
    public void setContact(@NonNull Contact contact) {
        this.contact = contact;
    }

    public ContactGroup getContactGroup() {
        return contact.getContactGroup();
    }

    @NonNull
    public String getTitle() {
        return title;
    }
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public List<String> getPassages() {
        return passages;
    }
    @Exclude
    public List<String> setPassages(List<String> passages) {
        this.passages.addAll(passages);
        return this.passages;
    }
    @Exclude
    public List<String> addPassage(String passage) {
        this.passages.add(passage);
        return this.passages;
    }
    @Exclude
    public List<String> removePassage(String passage) {
        this.passages.remove(passage);
        return this.passages;
    }

    @Nullable
    public Long getEndDate() {
        return endDate;
    }
    public void setEndDate(@Nullable Long endDate) {
        this.endDate = endDate;
    }

    @Nullable
    public Long getAnswered() {
        return answered;
    }
    public void setAnswered(@Nullable Long answered) {
        this.answered = answered;
    }

    @Nullable
    public Long getLastPrayedFor() {
        return lastPrayedFor;
    }
    public void setLastPrayedFor(@Nullable Long lastPrayedFor) {
        this.lastPrayedFor = lastPrayedFor;
    }

    @Nullable
    public HashMap<String, Boolean> getPrayerLists() {
        return prayerLists;
    }

    @Exclude
    @Override
    public String toString() {
        return "PrayerRequestEntry {"+
                "key='" + key + '\'' +
                ", contactKey ='" + contactKey + '\'' +
                ", contact ='" + contact.toString() + '\'' +
                ", title ='" + title + '\'' +
                ", description ='" + description + '\'' +
                "}";
    }
    protected PrayerRequest(Parcel in) {
        key = in.readString();
        contactKey = in.readString();
        contact = (Contact) in.readValue(Contact.class.getClassLoader());
        title = in.readString();
        description = in.readString();
        endDate = in.readByte() == 0x00 ? null : in.readLong();
        if (in.readByte() == 0x01) {
            passages = new ArrayList<String>();
            in.readList(passages, String.class.getClassLoader());
        } else {
            passages = null;
        }
        lastPrayedFor = in.readByte() == 0x00 ? null : in.readLong();
        answered = in.readByte() == 0x00 ? null : in.readLong();
        dateCreated = in.readByte() == 0x00 ? null : in.readLong();
        prayerLists = (HashMap) in.readValue(HashMap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(contactKey);
        dest.writeValue(contact);
        dest.writeString(title);
        dest.writeString(description);
        if (endDate == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(endDate);
        }
        if (passages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(passages);
        }
        if (lastPrayedFor == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(lastPrayedFor);
        }
        if (answered == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(answered);
        }
        if (dateCreated == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(dateCreated);
        }
        dest.writeValue(prayerLists);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PrayerRequest> CREATOR = new Parcelable.Creator<PrayerRequest>() {
        @Override
        public PrayerRequest createFromParcel(Parcel in) {
            return new PrayerRequest(in);
        }

        @Override
        public PrayerRequest[] newArray(int size) {
            return new PrayerRequest[size];
        }
    };
}