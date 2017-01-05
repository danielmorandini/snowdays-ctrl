package com.snowdays.snowdaysctrl.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by danielmorandini on 20/12/2016.
 */


// We'll have to save here the class NFC-Activity class that will be handling the calls, such as
// a bus checkin,
public class MainCard implements Serializable {
    @SerializedName("title")
    private String mTitle;
    @SerializedName("actionKey")
    private String mActionKey;
    @SerializedName("dayKey")
    private String mDayKey;

    public MainCard(String title, String actionKey, String dayKey) {
        mTitle = title;
        mActionKey = actionKey;
        mDayKey = dayKey;
    }

    // Placeholder just for now
    public MainCard(String title) {
        mTitle = title;
    }
    public String getTitle() {
        return mTitle;
    }
    public String getActionKey() { return mActionKey; }
    public String getmDayKey() {
        return mDayKey;
    }
}
