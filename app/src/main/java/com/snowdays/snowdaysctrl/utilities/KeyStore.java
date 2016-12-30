package com.snowdays.snowdaysctrl.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.snowdays.snowdaysctrl.R;

/**
 * Created by danielmorandini on 30/12/2016.
 */

public class KeyStore {

    public static String getToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        );

        return context.getResources().getString(R.string.token_key);
    }

    public static void saveToken(Context context, String token) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
        );

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.token_key), token);
        editor.commit();
    }
}
