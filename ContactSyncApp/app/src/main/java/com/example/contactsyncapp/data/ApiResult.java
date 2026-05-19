package com.example.contactsyncapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class ApiResult {
    @SerializedName("success")
    private boolean successful;

    @SerializedName("message")
    private String message;

    @SerializedName("contacts")
    private List<PhoneEntry> contacts;

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public List<PhoneEntry> getContacts() {
        return contacts == null ? Collections.emptyList() : contacts;
    }
}
