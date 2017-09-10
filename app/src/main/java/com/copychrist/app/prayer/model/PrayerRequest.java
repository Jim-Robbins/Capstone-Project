package com.copychrist.app.prayer.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.copychrist.app.prayer.util.Utils;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class PrayerRequest {
    public static final String DB_NAME = "prayerRequests";

    @NonNull private String key;

    @NonNull private String contactKey;
    public static final String CONTACT_KEY = "contactKey";

    @NonNull private String title;
    public static final String TITLE = "title";

    @Nullable private String description;
    public static final String DESCRIPTION = "description";

    @Nullable private Date endDate;
    public static final String END_DATE = "description";

    @Nullable private List<String> passages = new ArrayList<>();
    public static final String PASSAGES = "passages";

    @Nullable private List<String> prayedForOn;
    public static final String PRAYED_FOR_ON = "prayedForOn";

    @Nullable private Date answered;
    public static final String ANSWERED = "answered";

    @NonNull private Long dateCreated;
    public static final String DATE_CREATED = "dateCreated";

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

    @NonNull
    public String getContactKey() {
        return contactKey;
    }
    public void setContactKey(@NonNull String contactKey) {
        this.contactKey = contactKey;
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
    public List<String> addPassages(List<String> passages) {
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

    @Exclude
    @Override
    public String toString() {
        return "ContactEntry {"+
                "key='" + key + '\'' +
                ", contactKeyKey ='" + contactKey + '\'' +
                ", title ='" + title + '\'' +
                ", description ='" + description + '\'' +
                "}";
    }


}