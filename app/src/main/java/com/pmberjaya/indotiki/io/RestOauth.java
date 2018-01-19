package com.pmberjaya.indotiki.io;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;

import com.pmberjaya.indotiki.config.Config;


public class RestOauth {

    private static String Base_url = Config.APP_API_URL;
    private static ApiInterface gitApiInterface ;
    public static ApiInterface getClient() {
        if (gitApiInterface == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            String credentials = "admin" + ":" + "1234";
                            // create Base64 encodet string
                            final String basic =
                                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                            Request original = chain.request();
                            // Customize the request
                            Request request = original.newBuilder()
                                    .header("Content-Type", "application/x-www-form-urlencoded")
                                    .header("Authorization", basic)
                                    .method(original.method(), original.body())
                                    .build();
                            Log.d("REQUESTTTT",""+request);
                            Log.d("basic",""+basic);
                            Response response = chain.proceed(request);
                            return response;
                        }
                    }).build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Base_url)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gitApiInterface = client.create(ApiInterface.class);
        }
        return gitApiInterface ;
    }

}
