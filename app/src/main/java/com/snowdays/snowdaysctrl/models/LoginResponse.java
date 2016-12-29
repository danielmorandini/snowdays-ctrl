package com.snowdays.snowdaysctrl.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by danielmorandini on 29/12/2016.
 */

public class LoginResponse {

    private String status;
    private Data data;

    public Data getData() { return data; }

    public class Data {
        @SerializedName("authToken")
        private String token;
        @SerializedName("userId")
        private String id;

        public String getToken() { return token; }
        public String getId() { return id; }
    }
}
