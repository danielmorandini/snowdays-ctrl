package com.snowdays.snowdaysctrl.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.snowdays.snowdaysctrl.R;

/**
 * Created by danielmorandini on 30/12/2016.
 */

public class KeyStore {

    public static String getToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(context.getString(R.string.token_key), "notFound");
    }

    public static String getUserId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(context.getString(R.string.user_id_key), "notFound");
    }

    public static void saveToken(Context context, String token) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.token_key), token)
                .apply();
    }

    public static void saveUserId(Context context, String userId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.user_id_key), userId)
                .apply();
    }

    public static void clearAll(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .clear()
                .commit();
    }
}
