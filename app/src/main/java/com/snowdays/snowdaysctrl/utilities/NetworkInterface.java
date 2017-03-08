package com.snowdays.snowdaysctrl.utilities;

import com.snowdays.snowdaysctrl.models.LoginResponse;
import com.snowdays.snowdaysctrl.models.MainCard;
import com.snowdays.snowdaysctrl.models.Participant;
import com.snowdays.snowdaysctrl.models.ParticipantShort;
import com.snowdays.snowdaysctrl.models.ResponseData;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
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

    @FormUrlEncoded
    @POST("/api/v1/reports")
    Call<ResponseData<String>> sendReport(
            @HeaderMap Map<String, String> headers,
            @Field("message") String message
    );

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @PUT("/api/v1/participants/{id}")
    Call<ResponseData<String>> updateParticipant(
            @HeaderMap Map<String, String> headers,
            @Path("id") String participantId,
            @FieldMap Map<String, Boolean> fields
    );

    @GET("/api/v1/participants")
    Call<ResponseData<ParticipantShort[]>> getParticipants(
            @HeaderMap Map<String, String> headers
    );

    @GET("/api/v1/participants")
    Call<ResponseData<ParticipantShort[]>> getParticipantsWithFields(
            @HeaderMap Map<String, String> headers,
            @Query("fields") String fields,
            @Query("value") Boolean value
    );

    @GET("/api/v1/participants/{id}?fields=all")
    Call<ResponseData<Participant>> getParticipant(
            @HeaderMap Map<String, String> headers,
            @Path("id") String id
    );

    @GET("/api/v1/cards?events=all")
    Call<ResponseData<MainCard[]>> getActivities(
            @HeaderMap Map<String, String> headers
    );
}
