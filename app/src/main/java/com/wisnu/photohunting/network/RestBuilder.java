package com.wisnu.photohunting.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestBuilder {
    private RestBuilder() {
    }

    /**
     * Creating retrofit getInstance
     */
    private static Retrofit retrofit = setupBuilder()
            .client(displayLogging())
            .build();

    /**
     * Create new Retrofit.Builder instance with certain configuration
     */
    private static Retrofit.Builder setupBuilder() {
        return new Retrofit.Builder().baseUrl(BaseURL.MAIN_POINT)
                .addConverterFactory(GsonConverterFactory.create());
    }

    /**
     * Add logging functionality
     **/
    private static OkHttpClient displayLogging() {
        return new OkHttpClient.Builder()
                .addInterceptor(
                        new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    /**
     * Establish new connection
     */
    public static <E> E buildService(Class<E> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
