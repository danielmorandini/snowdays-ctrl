package com.snowdays.snowdaysctrl.models;

import android.graphics.drawable.Icon;

import com.google.gson.annotations.SerializedName;
import com.snowdays.snowdaysctrl.R;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danielmorandini on 20/12/2016.
 */


// We'll have to save here the class NFC-Activity class that will be handling the calls, such as
// a bus checkin,
public class MainCard implements Serializable {

    private String id;
    private String name;
    private  String subtitle;
    private String type;
    private Date startDate;
    private Date endDate;
    private boolean checkRequired;
    private String checkAction;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getType() {
        return type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isCheckRequired() {
        return checkRequired;
    }

    public String getCheckAction() {
        return checkAction;
    }

    public int getIconID() {

        int icon;
        switch (getType()) {
            case "transportation":
                icon = R.drawable.ic_003_school_bus;
                break;
            case "meal":
                icon = R.drawable.ic_002_groceries;
                break;
            default:
                icon = R.drawable.ic_001_ski;
        }

        return icon;
    }


    /* Example
        {
      "_id": "cwMHDpjyiWuJHEo6y",
      "name": "Test2",
      "subtitle": "Test2",
      "type": "other",
      "startDate": "2017-03-03T16:15:00.000Z",
      "endDate": "2017-03-03T17:00:00.000Z",
      "checkRequired": true,
      "checkAction": "day2.meal1"
    }
    */
}
