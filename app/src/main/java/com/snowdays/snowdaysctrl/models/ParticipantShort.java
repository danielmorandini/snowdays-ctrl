package com.snowdays.snowdaysctrl.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by danielmorandini on 08/03/2017.
 */

public class ParticipantShort implements Serializable {

    @SerializedName("_id")
    private String id;
    private String firstName;
    private String lastName;

    public String getDorm() {
        return dorm;
    }

    private String dorm;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
