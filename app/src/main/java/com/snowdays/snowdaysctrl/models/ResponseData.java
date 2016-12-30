package com.snowdays.snowdaysctrl.models;

/**
 * Created by danielmorandini on 30/12/2016.
 */

public class ResponseData<T> {

    private String status;
    private T data;

    public T getData() { return data; }
    public String getStatus() { return status; }
}
