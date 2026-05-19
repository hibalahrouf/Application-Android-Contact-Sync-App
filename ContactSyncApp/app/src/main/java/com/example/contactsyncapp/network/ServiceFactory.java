package com.example.contactsyncapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ServiceFactory {
    private static final String BASE_URL = "http://10.0.2.2/contactsync/server-api/endpoints/";
    private static Retrofit retrofit;

    private ServiceFactory() {
    }

    public static ContactGateway contacts() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ContactGateway.class);
    }
}
