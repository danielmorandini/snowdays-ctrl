package com.snowdays.snowdaysctrl.utilities;

import com.snowdays.snowdaysctrl.models.LoginResponse;
import com.snowdays.snowdaysctrl.models.MainCard;
import com.snowdays.snowdaysctrl.models.Participant;
import com.snowdays.snowdaysctrl.models.ResponseData;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("/api/v1/participants?fields=all")
    Call<Participant[]> getAllParticipants(
            @HeaderMap Map<String, String> headers
    );

    @GET("/api/v1/participants")
    Call<ResponseData<Participant[]>> getParticipantsWithFields(
            @HeaderMap Map<String, String> headers,
            @Query("fields") String fields,
            @Query("value") Boolean value
    );

    @GET("/api/v1/activities")
    Call<ResponseData<MainCard[]>> getActivities(
            @HeaderMap Map<String, String> headers
    );
}
