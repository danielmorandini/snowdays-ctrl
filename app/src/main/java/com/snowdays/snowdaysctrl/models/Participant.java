package com.snowdays.snowdaysctrl.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by danielmorandini on 11/01/2017.
 */

public class Participant implements Serializable {

    @SerializedName("_id")
    private String id;
    private String token;
    private String owner;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String dorm;
    private String gender;
    private String university;
    private Boolean isVolleyPlayer;
    private Boolean isFootballPlayer;
    private Boolean hasPersonalID;
    private Boolean hasStudentID;

    public Participant() {
    }

    protected Participant(Parcel in) {
        id = in.readString();
        token = in.readString();
        owner = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        university = in.readString();
        dorm = in.readString();
        phone = in.readString();
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

    public String getDorm() { return dorm; }
    
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

    public String getUniversity() {
        return university;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }
}
