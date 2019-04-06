package com.along.androidchat;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Long
 * Date: 4/5/2019
 * Time: 11:22 PM
 */
public class PreferencesHelper {
    private static final String FILE_NAME = "com.example.aymen.androidchat";

    private final SharedPreferences mPrefs;

    public PreferencesHelper(Context context) {
        mPrefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setToken(String Token) {
        mPrefs.edit().putString("Token", Token);
    }

    public String getToken() {
        return mPrefs.getString("Token","");
    }

    public void setUsers(String Users) {
        mPrefs.edit().putString("Users", Users);
    }

    public String getUsers() {
        return mPrefs.getString("Users","{}");
    }

}
