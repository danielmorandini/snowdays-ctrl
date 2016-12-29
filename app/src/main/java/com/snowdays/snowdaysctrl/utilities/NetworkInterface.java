package com.snowdays.snowdaysctrl.utilities;

import com.snowdays.snowdaysctrl.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by danielmorandini on 29/12/2016.
 */

public interface NetworkInterface {

    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<LoginResponse> login(
        @Field("username") String username,
        @Field("password") String password
    );
}
