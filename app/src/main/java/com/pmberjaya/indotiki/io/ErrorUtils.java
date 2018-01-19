package com.pmberjaya.indotiki.io;

import com.google.gson.JsonSyntaxException;
import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;

import java.io.IOException;
import java.lang.annotation.Annotation;


import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by edwin on 4/23/2016.
 */

public class ErrorUtils {

    private static String Base_url = Config.APP_API_URL;
    private static APIErrorCallback error;
    public static APIErrorCallback parseError(Response<?> response) {
        if(response.code()==404){
            error = new APIErrorCallback();
            error.setError("Error: Not Found");
            return error;
        }
        else if(response.code()==500){
            error =  new APIErrorCallback();
            error.setError("Error: Internal Server Error");
            return error;
        }
        else if(response.code()==403){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Converter<ResponseBody, APIErrorCallback> converter = retrofit.responseBodyConverter(APIErrorCallback.class, new Annotation[0]);
            if(converter!=null) {
                try {
                    error = converter.convert(response.errorBody());
                }catch(JsonSyntaxException e){
                    error =  new APIErrorCallback();
                    error.setError("Error: Forbidden");
                    return error;
                    // throw new Exception(e); //checked exception
                }catch(IOException e){
                    error =  new APIErrorCallback();
                    error.setError("Error: Forbidden");
                    return error;
                }
                return error;
            }else {
                error =  new APIErrorCallback();
                error.setError("Error: Forbidden");
                return error;
            }
        }
        else {

            error = new APIErrorCallback();
            error.setError("Error "+response.code()+" : There is a problem.");
            return error;
        }
    }
}