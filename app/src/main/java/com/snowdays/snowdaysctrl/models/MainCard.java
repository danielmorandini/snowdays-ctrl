package com.snowdays.snowdaysctrl.models;

/**
 * Created by danielmorandini on 20/12/2016.
 */


// We'll have to save here the class NFC-Activity class that will be handling the calls, such as
// a bus checkin,
public class MainCard {
    private String mTitle;

    public MainCard(String title) {
        mTitle = title;
    }
    public String getTitle() {
        return mTitle;
    }
}
