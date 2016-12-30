package com.snowdays.snowdaysctrl.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by danielmorandini on 29/12/2016.
 */

public class LoginResponse {
    @SerializedName("authToken")
    private String token;
    @SerializedName("userId")
    private String id;

    public String getToken() { return token; }
    public String getId() { return id; }
}
