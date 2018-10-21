package com.example.milja.movieapp;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MovieApp extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MovieApp.context = getApplicationContext();
        Realm.init(this);


    }
}
