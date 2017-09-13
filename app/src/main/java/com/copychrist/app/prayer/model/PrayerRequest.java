package com.copychrist.app.prayer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;
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

    @Nullable private Date endDate;
    public static final String CHILD_END_DATE = "endDate";

    @Nullable private List<String> passages = new ArrayList<>();
    public static final String CHILD_PASSAGES = "passages";

    @Nullable private List<String> prayedForOn = new ArrayList<>();
    public static final String CHILD_PRAYED_FOR_ON = "prayedForOn";

    @Nullable private Date answered;
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
        this.passages.clear();
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
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(@Nullable Date endDate) {
        this.endDate = endDate;
    }

    @Nullable
    public Date getAnswered() {
        return answered;
    }
    public void setAnswered(@Nullable Date answered) {
        this.answered = answered;
    }

    @Nullable
    public List<String> getPrayedForOn() {
        return prayedForOn;
    }
    public void setPrayedForOn(@Nullable List<String> prayedForOn) {
        this.prayedForOn = prayedForOn;
    }
    @Exclude
    public List<String> addPrayedForOn(String prayedOn) {
        this.prayedForOn.add(prayedOn);
        return this.prayedForOn;
    }
    @Exclude
    public List<String> removePrayedForOn(String prayedOn) {
        this.prayedForOn.remove(prayedOn);
        return this.prayedForOn;
    }

    @Nullable
    public HashMap<String, Boolean> getPrayerLists() {
        return prayerLists;
    }
    public void setPrayerLists(@Nullable HashMap<String, Boolean> prayerLists) {
        this.prayerLists = prayerLists;
    }
    @Exclude
    public HashMap<String, Boolean> addPrayerLists(String prayerLists) {
        this.prayerLists.put(prayerLists, true);
        return this.prayerLists;
    }
    @Exclude
    public HashMap<String, Boolean> removePrayerLists(String prayerLists) {
        this.prayerLists.remove(prayerLists);
        return this.prayerLists;
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
        long tmpEndDate = in.readLong();
        endDate = tmpEndDate != -1 ? new Date(tmpEndDate) : null;
        if (in.readByte() == 0x01) {
            passages = new ArrayList<String>();
            in.readList(passages, String.class.getClassLoader());
        } else {
            passages = null;
        }
        if (in.readByte() == 0x01) {
            prayedForOn = new ArrayList<String>();
            in.readList(prayedForOn, String.class.getClassLoader());
        } else {
            prayedForOn = null;
        }
        long tmpAnswered = in.readLong();
        answered = tmpAnswered != -1 ? new Date(tmpAnswered) : null;
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
        dest.writeLong(endDate != null ? endDate.getTime() : -1L);
        if (passages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(passages);
        }
        if (prayedForOn == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(prayedForOn);
        }
        dest.writeLong(answered != null ? answered.getTime() : -1L);
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