package com.pmberjaya.indotiki.io;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import android.util.Log;

import com.pmberjaya.indotiki.config.Config;


public class RestPhp {
    private static String baseUrl = Config.APP_API_URL;

    private static ApiInterface gitApiInterface ;
    public static ApiInterface getClient() {
        if (gitApiInterface == null) {
            OkHttpClient clientWith1mTimeout = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            // Customize the request
//                            Request request = original.newBuilder()
//                                    .header("Accept", "application/json")
//                                    .header("Authorization", "auth-token")
//                                    .method(original.method(), original.body())
//                                    .build();
                            Log.d("URL",""+original);
                            Response response = chain.proceed(original);
                            return response;
                        }
                    })
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS).build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(clientWith1mTimeout)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gitApiInterface = client.create(ApiInterface.class);
        }
        return gitApiInterface ;
    }

}
