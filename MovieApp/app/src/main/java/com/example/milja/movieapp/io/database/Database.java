package com.example.milja.movieapp.io.database;

import com.example.milja.movieapp.io.model.Results;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Database {
    public static void storeListDataToServer(RealmList<Results> list) {
        Realm realm = Realm.getDefaultInstance();
        if (list == null || realm == null)
            return;
        realm.beginTransaction();
        realm.insertOrUpdate(list);
        realm.commitTransaction();
    }
    public static RealmList<Results> getTopRatedMoviesString(){
        RealmList<Results> list = new RealmList<>();
        Realm realm = Realm.getDefaultInstance();
        list.addAll(realm.where(Results.class).findAll());
        return list;
    }
    public static void deleteListDataFromLocale() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Results> results = realm.where(Results.class).findAll();
        realm.beginTransaction();
        results.deleteAllFromRealm();
        realm.commitTransaction();

    }
}
