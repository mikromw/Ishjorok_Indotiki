package com.pmberjaya.indotiki.controllers;

import android.content.Context;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.io.ApiInterface;
import com.pmberjaya.indotiki.io.ErrorUtils;
import com.pmberjaya.indotiki.io.RestClient;
import com.pmberjaya.indotiki.models.pln.TokenListrik.History.HistoryPlnModel;
import com.pmberjaya.indotiki.models.pln.TokenListrik.PurchasePln.PurchasePlnModel;
import com.pmberjaya.indotiki.models.pln.TokenListrik.TokenListrikModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gilbert on 12/4/2017.
 */

public class PlnController extends BaseController {

    private static PlnController _instance;
    private Context context;


    protected PlnController(Context paramContext) {
        super(paramContext);
        this.context = paramContext;
    }

    public static PlnController getInstance(Context paramContext) {
        if (_instance == null) {
            _instance = new PlnController(paramContext);
        }
        return _instance;
    }

    public void postPlnTokenList(HashMap<String, String> params, String api, final BaseGenericInterface baseGenericInterface) {
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<TokenListrikModel>> call = service.postPlnTokenList(params);
        call.enqueue(new Callback<BaseGenericCallback<TokenListrikModel>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<TokenListrikModel>> call, Response<BaseGenericCallback<TokenListrikModel>> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<TokenListrikModel> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseGenericCallback<TokenListrikModel>> call, Throwable t) {
                APIErrorCallback error = new APIErrorCallback();
                String error_msg = t.getMessage();
                if (error_msg != null) {
                    error.setError(error_msg + "");
                } else {
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }

    public void postPurchaseTokenPln(HashMap<String, String> params, String api, final BaseGenericInterface baseGenericInterface) {
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<PurchasePlnModel>> call = service.postPurchaseTokenPln(params);
        call.enqueue(new Callback<BaseGenericCallback<PurchasePlnModel>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<PurchasePlnModel>> call, Response<BaseGenericCallback<PurchasePlnModel>> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<PurchasePlnModel> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseGenericCallback<PurchasePlnModel>> call, Throwable t) {
                APIErrorCallback error = new APIErrorCallback();
                String error_msg = t.getMessage();
                if (error_msg != null) {
                    error.setError(error_msg + "");
                } else {
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }

    public void getHistoryPln(String api, final BaseGenericInterface baseGenericInterface) {
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<HistoryPlnModel>> call = service.getHistoryPln();
        call.enqueue(new Callback<BaseGenericCallback<HistoryPlnModel>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<HistoryPlnModel>> call, Response<BaseGenericCallback<HistoryPlnModel>> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<HistoryPlnModel> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseGenericCallback<HistoryPlnModel>> call, Throwable t) {
                APIErrorCallback error = new APIErrorCallback();
                String error_msg = t.getMessage();
                if (error_msg != null) {
                    error.setError(error_msg + "");
                } else {
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }
}