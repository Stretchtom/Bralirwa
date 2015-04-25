package com.example.wendy_guo.j4sp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Wendy_Guo on 3/15/15.
 */
public class PreferencesManager {
    SharedPreferences mSharedPreferences;

    public PreferencesManager(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getStringPreference(String pref) {
        return mSharedPreferences.getString(pref, "default");
    }
    public int getIntPreference(String pref) {
        return mSharedPreferences.getInt(pref, 1);
    }
    public boolean getBooleanPreference(String pref) {
        return mSharedPreferences.getBoolean(pref, true);
    }

    public void setStringPreference(String key, String value) {
        mSharedPreferences
                .edit()
                .putString(key, value)
                .apply();
    }
    public void setIntPreference(String key, int value) {
        mSharedPreferences
                .edit()
                .putInt(key, value)
                .apply();
    }
    public void setBooleanPreference(String key, boolean value) {
        mSharedPreferences
                .edit()
                .putBoolean(key, value)
                .apply();
    }
}
