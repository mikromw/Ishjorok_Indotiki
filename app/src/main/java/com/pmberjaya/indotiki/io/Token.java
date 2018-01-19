package com.pmberjaya.indotiki.io;

import android.content.Context;
import android.util.Log;

import com.pmberjaya.indotiki.controllers.BaseController;
import com.pmberjaya.indotiki.dao.SessionKey;

import java.util.HashMap;

/**
 * Created by edwin on 3/30/2016.
 */
public class Token extends BaseController {
    private static Token _instance;
    private static SessionKey key;
    private static Context context;
    private static String token;
    protected Token(Context paramContext)
    {
        super(paramContext);
        this.context = paramContext;

    }

    public static Token getInstance(Context paramContext)
    {
        if (_instance == null) {
            _instance = new Token(paramContext);
            key= new SessionKey(paramContext);
            HashMap<String,String> apikey = new HashMap<String,String>();
            apikey= key.getKey();
            token = apikey.get(SessionKey.KEY);
            Log.d("TOKEN APINYAA",""+token);
        }
        return _instance;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }
}

