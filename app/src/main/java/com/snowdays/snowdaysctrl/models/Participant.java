package com.snowdays.snowdaysctrl.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


/**
 * Created by danielmorandini on 11/01/2017.
 */

public class Participant implements Parcelable {

    @SerializedName("_id")
    private String id;
    private String token;
    private String owner;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
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
    }

    public static final Creator<Participant> CREATOR = new Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };

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

    public String getUniversity() {
        return university;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(token);
        dest.writeString(owner);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }
}
