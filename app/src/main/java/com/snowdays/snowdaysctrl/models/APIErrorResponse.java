package com.snowdays.snowdaysctrl.models;

/**
 * Created by danielmorandini on 10/01/2017.
 */

public class APIErrorResponse {
    private int statusCode;
    private String message;

    public APIErrorResponse() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}
