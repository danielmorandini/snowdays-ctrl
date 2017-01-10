package com.snowdays.snowdaysctrl.utilities;

import com.snowdays.snowdaysctrl.models.LoginResponse;
import com.snowdays.snowdaysctrl.models.ResponseData;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by danielmorandini on 29/12/2016.
 */

public interface NetworkInterface {

    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<ResponseData<LoginResponse>> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @PUT("/api/v1/participants/{id}")
    Call<ResponseData<String>> updateParticipant(
            @HeaderMap Map<String, String> headers,
            @Path("id") String participantId,
            @Body HashMap<String, HashMap<String, Boolean>> body
    );
}
