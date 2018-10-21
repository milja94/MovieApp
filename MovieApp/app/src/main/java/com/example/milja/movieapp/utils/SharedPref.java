package com.example.milja.movieapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {
    private static final String TOKEN = "token";
    private static final String SESSION_ID = "sessionId";
    private static final String ACCOUNT_ID = "accountId";

    private static SharedPreferences getSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setToken(final Context context, final String token) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(TOKEN, token).apply();
    }

    public static String getToken(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(TOKEN, null);
    }

    public static void setSessionId(final Context context, final String session) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(SESSION_ID, session).apply();
    }

    public static String getSessionId(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(SESSION_ID, null);
    }

    public static void setAccountId(final Context context, final int id) {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putInt(ACCOUNT_ID, id).apply();
    }

    public static int getAccountId(final Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getInt(ACCOUNT_ID, 0);
    }
}
