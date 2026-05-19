package com.example.contactsyncapp.data;

import com.google.gson.annotations.SerializedName;

public class PhoneEntry {
    @SerializedName("id")
    private int serverId;

    @SerializedName("name")
    private String displayName;

    @SerializedName("phone")
    private String phoneValue;

    @SerializedName("source")
    private String originTag;

    @SerializedName("created_at")
    private String savedAt;

    public PhoneEntry(String displayName, String phoneValue) {
        this.displayName = displayName;
        this.phoneValue = phoneValue;
        this.originTag = "android_device";
    }

    public int getServerId() {
        return serverId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneValue() {
        return phoneValue;
    }

    public String getOriginTag() {
        return originTag;
    }

    public String getSavedAt() {
        return savedAt;
    }
}
