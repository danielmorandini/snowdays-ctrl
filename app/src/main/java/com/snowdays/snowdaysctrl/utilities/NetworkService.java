package com.snowdays.snowdaysctrl.utilities;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.snowdays.snowdaysctrl.BuildConfig;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by danielmorandini on 29/12/2016.
 */

public class NetworkService {
    public static Retrofit mRetrofit;

    public static NetworkInterface getInstance() {
        if (mRetrofit == null) {

            // okhttp
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

            // logging
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                clientBuilder.addInterceptor(logging);
            }

            // gson
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                    .registerTypeAdapter(Date.class, new DateDeserializer())
                    .create();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl("https://www.snowdays.it")
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return mRetrofit.create(NetworkInterface.class);
    }

    private static class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            String date = element.getAsString();

            SimpleDateFormat longDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            longDateFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));

            try {
                return longDateFormatter.parse(date);
            } catch (ParseException eLong) {
                SimpleDateFormat shortDateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

                try {
                    return shortDateFormatter.parse(date);
                } catch (ParseException eShort) {
                    return null;
                }
            }
        }
    }
}
