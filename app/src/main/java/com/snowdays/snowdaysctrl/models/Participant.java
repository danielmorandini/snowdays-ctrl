package com.snowdays.snowdaysctrl.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by danielmorandini on 11/01/2017.
 */

public class Participant {

    @SerializedName("_id")
    private String id;
    private String token;
    private String owner;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean isVolleyPlayer;
    private Boolean isFootballPlayer;
    private Boolean hasPersonalID;
    private Boolean hasStudentID;

    private Day1 day1;
    private Day2 day2;
    private Day3 day3;

    public Participant() {
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getOwner() {
        return owner;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Boolean getVolleyPlayer() {
        return isVolleyPlayer;
    }

    public Boolean getFootballPlayer() {
        return isFootballPlayer;
    }

    public Boolean getHasPersonalID() {
        return hasPersonalID;
    }

    public Boolean getHasStudentID() {
        return hasStudentID;
    }

    public Day1 getDay1() {
        return day1;
    }

    public Day2 getDay2() {
        return day2;
    }

    public Day3 getDay3() {
        return day3;
    }
}
