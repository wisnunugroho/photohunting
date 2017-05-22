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
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    /**
     * Establish new connection
     */
    public static <E> E buildService(Class<E> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
