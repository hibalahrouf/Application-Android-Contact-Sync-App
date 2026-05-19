package com.example.contactsyncapp.network;

import com.example.contactsyncapp.data.ApiResult;
import com.example.contactsyncapp.data.PhoneEntry;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ContactGateway {
    @POST("add_entry.php")
    Call<ApiResult> sendContact(@Body PhoneEntry contact);

    @GET("list_entries.php")
    Call<ApiResult> loadStoredContacts();

    @GET("find_entries.php")
    Call<ApiResult> searchStoredContacts(@Query("q") String keyword);
}
