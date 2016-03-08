package com.bima.dokterpribadimu.data.remote.base;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.util.Log;

import com.bima.dokterpribadimu.BuildConfig;
import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.NetUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

import static okhttp3.logging.HttpLoggingInterceptor.Level;

/**
 * Created by apradanas on 2/3/16.
 */
public abstract class BaseApi<T> {

    private static final String BASE_URL = BuildConfig.BASE_URL;

    public static int MAX_IDLE_CONNECTIONS = 30;
    public static int KEEP_ALIVE_DURATION_MS = 3 * 60 * 1000;

    private final Retrofit retrofit;

    public BaseApi() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpLoggingInterceptor loggingInterceptor = getLoggingInterceptor();
        loggingInterceptor.setLevel(getLogLevel());

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(getNetworkInterceptor())
                .addInterceptor(loggingInterceptor)
                .connectionPool(
                        new ConnectionPool(
                                MAX_IDLE_CONNECTIONS,
                                KEEP_ALIVE_DURATION_MS,
                                TimeUnit.MILLISECONDS))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    private HttpLoggingInterceptor getLoggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(getTag(), message);
            }
        });
    }

    private Level getLogLevel() {
        return BuildConfig.DEBUG ? Level.BODY : Level.NONE;
    }

    private Interceptor getNetworkInterceptor() {
        final Context context = DokterPribadimuApplication.getInstance().getApplicationContext();

        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // intercept network connection
                if (!NetUtils.isNetworkAvailable(context)) {
                    throw new IOException(Constants.NETWORK_IS_UNREACHABLE);
                }

                Request.Builder builder = chain.request().newBuilder();
                return chain.proceed(builder.build());
            }
        };
    }

    private String getTag() {
        return getServiceClass().getSimpleName();
    }

    public T createService() {
        return (T) retrofit.create(getServiceClass());
    }

    protected abstract Class getServiceClass();
}
