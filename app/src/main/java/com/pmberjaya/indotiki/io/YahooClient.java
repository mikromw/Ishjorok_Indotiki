package com.pmberjaya.indotiki.io;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pmberjaya.indotiki.config.Config;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class YahooClient {
    private static String Base_url = Config.APP_API_URL;
    private static ApiInterface gitApiInterface;

    public static ApiInterface getClient(final String accessToken) {
        if (gitApiInterface == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient clientWith1mTimeout = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            // create Base64 encodet string
                            final String basic =
                                    "Bearer  " + accessToken;
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .header("Content-Type", "application/x-www-form-urlencoded")
                                    .header("Authorization", basic)
                                    .method(original.method(), original.body())
                                    .build();
                            Log.d("request", "" + request);
                            Log.d("Access token", "" + accessToken);
                            Response response = chain.proceed(request);
                            Log.d("RESPONSE",response+"");
                            return response;
                        }
                    })
                    .build();
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Base_url)
                    .client(clientWith1mTimeout)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            gitApiInterface = client.create(ApiInterface.class);
        }
        return gitApiInterface;
    }

}