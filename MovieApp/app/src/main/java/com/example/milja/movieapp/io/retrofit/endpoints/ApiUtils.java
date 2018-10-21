package com.example.milja.movieapp.io.retrofit.endpoints;

import com.example.milja.movieapp.io.retrofit.ServiceGenerator;

public class ApiUtils {
    private ApiUtils() {}


    private static final String BASE_URL = "https://api.themoviedb.org/3/";




    public static ApiService getApiService() {
        return ServiceGenerator.getClient(BASE_URL).create(ApiService.class);
    }
}
