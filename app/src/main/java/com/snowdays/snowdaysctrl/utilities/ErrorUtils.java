package com.snowdays.snowdaysctrl.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snowdays.snowdaysctrl.models.APIErrorResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by danielmorandini on 10/01/2017.
 */

public class ErrorUtils {

    public static APIErrorResponse parseError(Response<?> response) {
        Converter<ResponseBody, APIErrorResponse> converter =

                NetworkService.mRetrofit
                        .responseBodyConverter(APIErrorResponse.class, new Annotation[0]);

        APIErrorResponse error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIErrorResponse();
        }

        return error;
    }
}
