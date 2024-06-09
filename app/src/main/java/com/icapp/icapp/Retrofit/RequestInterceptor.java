package com.icapp.icapp.Retrofit;

import android.content.Context;

import androidx.annotation.NonNull;

import com.icapp.icapp.Helpers.SharedPrefs;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    SharedPrefs sharedPrefs;

    public RequestInterceptor(Context context) {
        sharedPrefs = new SharedPrefs(context);
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest;
        newRequest = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
//                .addHeader("accept-encoding", "gzip")
//                .addHeader("Authorization", "Bearer " + sharedPrefs.get(R.string.token, ""))
                .build();
        return chain.proceed(newRequest);
    }
}
