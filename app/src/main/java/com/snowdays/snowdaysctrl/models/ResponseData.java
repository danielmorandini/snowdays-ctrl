package com.snowdays.snowdaysctrl.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by danielmorandini on 30/12/2016.
 */

public class ResponseData<T> {

    @SerializedName("status")
    private String mStatus;
    @SerializedName("data")
    private T mData;

    public T getData() { return mData; }
    public String getStatus() { return mStatus; }
}
