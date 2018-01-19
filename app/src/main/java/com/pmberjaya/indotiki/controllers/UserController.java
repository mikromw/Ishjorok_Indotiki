package com.pmberjaya.indotiki.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.account.CompletingDataCallback;
import com.pmberjaya.indotiki.callbacks.deposit.DepositCallback;
import com.pmberjaya.indotiki.callbacks.deposit.DepositNewConfirmationCallback;
import com.pmberjaya.indotiki.callbacks.account.EditProfilCallback;
import com.pmberjaya.indotiki.callbacks.main.KeyCallback;
import com.pmberjaya.indotiki.callbacks.account.LoginCallback;
import com.pmberjaya.indotiki.callbacks.account.RegisterFirebaseCallback;
import com.pmberjaya.indotiki.callbacks.bookingNew.UploadPhotoCallback;
import com.pmberjaya.indotiki.callbacks.account.UploadPhotoTempCallback;
import com.pmberjaya.indotiki.callbacks.account.UploadProfilePhotoCallback;
import com.pmberjaya.indotiki.callbacks.account.VerificationCallback;
import com.pmberjaya.indotiki.callbacks.account.YahooProfileCallback;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.interfaces.account.APIKeyInterface;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.base.BaseInterface;
import com.pmberjaya.indotiki.interfaces.account.CompletingProfileInterface;
import com.pmberjaya.indotiki.interfaces.account.EditProfileInterface;
import com.pmberjaya.indotiki.interfaces.deposit.DepositLogInterface;
import com.pmberjaya.indotiki.interfaces.account.LoginInterface;
import com.pmberjaya.indotiki.interfaces.account.RegisterFirebaseInterface;
import com.pmberjaya.indotiki.interfaces.bookingNew.UploadImageInterface;
import com.pmberjaya.indotiki.interfaces.account.UploadImageProfileInterface;
import com.pmberjaya.indotiki.interfaces.account.UploadImageTempInterface;
import com.pmberjaya.indotiki.interfaces.account.VerificationSmsInterface;
import com.pmberjaya.indotiki.interfaces.account.YahooProfileInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.interfaces.deposit.DepositNewConfirmationInterface;
import com.pmberjaya.indotiki.io.ApiInterface;
import com.pmberjaya.indotiki.io.ErrorUtils;
import com.pmberjaya.indotiki.io.ProgressRequestBody;
import com.pmberjaya.indotiki.io.RestClient;
import com.pmberjaya.indotiki.io.RestImage;
import com.pmberjaya.indotiki.io.RestOauth;
import com.pmberjaya.indotiki.io.YahooClient;
import com.pmberjaya.indotiki.models.account.DeviceDataModel;
import com.pmberjaya.indotiki.models.account.UserModel;
import com.pmberjaya.indotiki.models.deposit.DepositData.CheckDepositData;
import com.pmberjaya.indotiki.models.deposit.DepositData.DepositTopUpListData;
import com.pmberjaya.indotiki.models.event.EventNewModel;
import com.pmberjaya.indotiki.models.event.EventPromoModel;
import com.pmberjaya.indotiki.models.help.HelpServiceData;
import com.pmberjaya.indotiki.models.help.HelpSubCategoryData;
import com.pmberjaya.indotiki.models.main.BannerData;
import com.pmberjaya.indotiki.models.deposit.DepositConfirmationData.DepositConfirmationData;
import com.pmberjaya.indotiki.models.deposit.DepositConfirmationData.DepositConfirmationModel;
import com.pmberjaya.indotiki.models.main.DistrictCentralData;
import com.pmberjaya.indotiki.models.main.MainMenuItemData;
import com.pmberjaya.indotiki.models.promo.PromoListModel;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserController extends BaseController {
    private static UserController _instance;
    private Context context;
    private BaseCallback baseCallback;
    protected UserController(Context paramContext)
    {
        super(paramContext);
        this.context = paramContext;
    }

    public static UserController getInstance(Context paramContext)
    {
        if(_instance==null) {
            _instance = new UserController(paramContext);
        }
        return _instance;
    }
    public void getDepositTopUpList(String action, String api, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<List<DepositTopUpListData>>> call = service.getDepositTopUpList(action);
        call.enqueue(new Callback<BaseGenericCallback<List<DepositTopUpListData>>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<List<DepositTopUpListData>>> call, Response<BaseGenericCallback<List<DepositTopUpListData>>> response) {
			/*	Log.d("rawData", new Gson().toJson(response.body())+"");
				Log.d("raw123",""+response.raw());*/
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<List<DepositTopUpListData>> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseGenericCallback<List<DepositTopUpListData>>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }

    public void getDepositLogData(Map<String,String> params, String api, final DepositLogInterface depositLogInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<DepositCallback> call = service.getDepositLog(params);
        call.enqueue(new Callback<DepositCallback>() {
            @Override
            public void onResponse(Call<DepositCallback> call, Response<DepositCallback> response) {
			/*	Log.d("rawData", new Gson().toJson(response.body())+"");
				Log.d("raw123",""+response.raw());*/
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    DepositCallback Data = response.body();
                    depositLogInterface.onSuccessGetDepositLog(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    depositLogInterface.onErrorGetDepositLog(error);
                }
            }

            @Override
            public void onFailure(Call<DepositCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                depositLogInterface.onErrorGetDepositLog(error);
            }
        });
    }
    public void getDepositConfirmationDetail(String transaction_id, String api, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<DepositConfirmationData>> call = service.getDepositConfirmationDetail(transaction_id);
        call.enqueue(new Callback<BaseGenericCallback<DepositConfirmationData>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<DepositConfirmationData>> call, Response<BaseGenericCallback<DepositConfirmationData>> response) {
				/*Log.d("booking", "raww = " + response.raw());
				Log.d("booking", "getBookingTransportListDetail = " + new Gson().toJson(response.body()));*/
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                }else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseGenericCallback<DepositConfirmationData>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }

    public void getDepositConfirmationData(Map<String,String> params, String api, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<DepositConfirmationModel>> call = service.getDepositDriverConfirmation(params);
        call.enqueue(new Callback<BaseGenericCallback<DepositConfirmationModel>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<DepositConfirmationModel>> call, Response<BaseGenericCallback<DepositConfirmationModel>> response) {
				/*Log.d("booking", "raww = " + response.raw());
				Log.d("booking", "getBookingTransportListDetail = " + new Gson().toJson(response.body()));*/
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<DepositConfirmationModel> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                }else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseGenericCallback<DepositConfirmationModel>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }
    public void getHelpService(String api, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<HelpServiceData>> call = service.getHelpService();
        call.enqueue(new Callback<BaseGenericCallback<HelpServiceData>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<HelpServiceData>> call, Response<BaseGenericCallback<HelpServiceData>> response) {
                if (response.isSuccessful()) {
                    BaseGenericCallback<HelpServiceData> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                }
                else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }
            @Override
            public void onFailure(Call<BaseGenericCallback<HelpServiceData>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }

    public void postCurrentCity(String district_id, String api, final BaseInterface baseInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseCallback> call = service.postCurrentCity(district_id);
        call.enqueue(new Callback<BaseCallback>() {
            @Override
            public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseCallback Data = response.body();
                    baseInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseInterface.onError(error);
            }
        });
    }
    public void postDepositConfirmationData(Map<String,String> params, String api, final DepositNewConfirmationInterface depositNewConfirmationInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<DepositNewConfirmationCallback> call = service.postDepositConfirmationData(params);
        call.enqueue(new Callback<DepositNewConfirmationCallback>() {
            @Override
            public void onResponse(Call<DepositNewConfirmationCallback> call, Response<DepositNewConfirmationCallback> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    DepositNewConfirmationCallback Data = response.body();
                    depositNewConfirmationInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    depositNewConfirmationInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<DepositNewConfirmationCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                depositNewConfirmationInterface.onError(error);
            }
        });
    }



    public void getDistrictCentral(String api, Map<String,String> params, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<DistrictCentralData>> call = service.getDistrictCentral(params);
        call.enqueue(new Callback<BaseGenericCallback<DistrictCentralData>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<DistrictCentralData>> call, Response<BaseGenericCallback<DistrictCentralData>> response) {
                //				Log.d("event", "raww = " + response.raw());
                //				Log.d("event", "getevent = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<DistrictCentralData> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                }
                else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }
            @Override
            public void onFailure(Call<BaseGenericCallback<DistrictCentralData>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }
    public void getEventNewList(String api,  Map<String,String> params, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<EventNewModel>> call = service.getEventNew(params);
        call.enqueue(new Callback<BaseGenericCallback<EventNewModel>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<EventNewModel>> call, Response<BaseGenericCallback<EventNewModel>> response) {
//				Log.d("event", "raww = " + response.raw());
//				Log.d("event", "getevent = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<EventNewModel> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                }
                else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }
            @Override
            public void onFailure(Call<BaseGenericCallback<EventNewModel>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }
    public Map<String,String> eventNewParams(String district){
        Map<String, String> params = new HashMap<String, String>();
        params.put("district", district);
        return  params;
    }
    public void getEventPromoList(String api, Map<String, String> params, final BaseGenericInterface baseGenericInterface){
                ApiInterface service = RestClient.getClient(api);
                Call<BaseGenericCallback<EventPromoModel>> call = service.getEventPromo(params);
                call.enqueue(new Callback<BaseGenericCallback<EventPromoModel>>() {
                    @Override
            public void onResponse(Call<BaseGenericCallback<EventPromoModel>> call, Response<BaseGenericCallback<EventPromoModel>> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<EventPromoModel> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                }
                else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }
            @Override
            public void onFailure(Call<BaseGenericCallback<EventPromoModel>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }
    public Map<String,String> eventPromoParams(String district){
        Map<String, String> params = new HashMap<String, String>();
        params.put("district", district);
        params.put("app", Constants.APP_MEMBER);
        return  params;
    }
    public void getEventOtherList(String api,  Map<String,String> params, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<EventNewModel>> call = service.getEventOther(params);
        call.enqueue(new Callback<BaseGenericCallback<EventNewModel>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<EventNewModel>> call, Response<BaseGenericCallback<EventNewModel>> response) {
//				Log.d("event", "raww = " + response.raw());
//				Log.d("event", "getevent = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<EventNewModel> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                }
                else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }
            @Override
            public void onFailure(Call<BaseGenericCallback<EventNewModel>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }
    public Map<String,String> eventOtherParams(String district){
        Map<String, String> params = new HashMap<String, String>();
        params.put("district", district);
        return  params;
    }
//
//    public void getHotImageEvent(String api, final BaseGenericInterface baseGenericInterface){
//        ApiInterface service = RestClient.getClient(api);
//        Call<BaseGenericCallback<HotBannerModel>> call = service.getHotImageEvent();
//        call.enqueue(new Callback<BaseGenericCallback<HotBannerModel>>() {
//            @Override
//            public void onResponse(Call<BaseGenericCallback<HotBannerModel>> call, Response<BaseGenericCallback<HotBannerModel>> response) {
////				Log.d("event", "raww = " + response.raw());
////				Log.d("event", "getevent = " + new Gson().toJson(response.body()));
//                if (response.isSuccessful()) {
//                    // request successful (status code 200, 201)
//                    BaseGenericCallback<HotBannerModel> Data = response.body();
//                    baseGenericInterface.onSuccess(Data);
//                }
//                else {
//                    APIErrorCallback error = ErrorUtils.parseError(response);
//                    baseGenericInterface.onError(error);
//                }
//            }
//            @Override
//            public void onFailure(Call<BaseGenericCallback<HotBannerModel>> call, Throwable t) {
//                APIErrorCallback error =new APIErrorCallback();
//                String error_msg = t.getMessage();
//                if(error_msg!=null) {
//                    error.setError(error_msg+"");
//                }
//                else{
//                    error.setError(context.getResources().getString(R.string.error));
//                }
//                baseGenericInterface.onError(error);
//            }
//        });
//    }

    LoginCallback loginCallback;
    APIErrorCallback loginErrorCallback;

    public void doLoginPhone(String phone, String api, final LoginInterface loginInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<LoginCallback> call = service.doLoginPhone(phone);
        call.enqueue(new Callback<LoginCallback>() {
            @Override
            public void onResponse(Call<LoginCallback> call, Response<LoginCallback> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    loginCallback = response.body();
                    loginInterface.onSuccessLogin(loginCallback);
                } else {
                    loginErrorCallback = ErrorUtils.parseError(response);
                    loginInterface.onErrorLogin(loginErrorCallback);
                }
            }

            @Override
            public void onFailure(Call<LoginCallback> call, Throwable t) {
                loginErrorCallback =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    loginErrorCallback.setError(error_msg+"");
                }
                else{
                    loginErrorCallback.setError(context.getResources().getString(R.string.error));
                }
                loginInterface.onErrorLogin(loginErrorCallback);
            }
        });
    }
    public LoginCallback getSendVerificationCallback(){
        return loginCallback;
    }
    public void setSendVerificationCallback(LoginCallback loginCallback){
        this.loginCallback = loginCallback;
    }
    public APIErrorCallback getSendVerificationErrorCallback(){
        return loginErrorCallback;
    }
    public void setSendVerificationSmsErrorCallback(APIErrorCallback loginErrorCallback){
        this.loginErrorCallback = loginErrorCallback;
    }

    public void putKey(final APIKeyInterface apiKeyInterface){
        try {
            ApiInterface service = RestOauth.getClient();
            Call<KeyCallback> call = service.put();
            call.enqueue(new Callback<KeyCallback>() {
                @Override
                public void onResponse(Call<KeyCallback> call, Response<KeyCallback> response) {
                    if (response.isSuccessful()) {
                        // request successful (status code 200, 201)
                        if (android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }
                        KeyCallback key = response.body();
                        apiKeyInterface.onSuccessGetAPIKey(key);


                    } else {
                        APIErrorCallback error = ErrorUtils.parseError(response);
                        apiKeyInterface.onErrorGetAPIKey(error);
                    }
                }

                @Override
                public void onFailure(Call<KeyCallback> call, Throwable t) {
                    APIErrorCallback error =new APIErrorCallback();
                    String error_msg = t.getMessage();
                    if(error_msg!=null) {
                        error.setError(error_msg+"");
                    }
                    else{
                        error.setError(context.getResources().getString(R.string.error));
                    }
                    apiKeyInterface.onErrorGetAPIKey(error);
                }
            });
        }catch (Exception ex){
            Toast.makeText(context, "Error because : "+ex.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }
	/*@Produce public void logoutuser(String api, final Activity a, final ProgressDialog dialog){

		try {
			ApiInterface service = RestClient.getClient(api);
			Call<LogoutKeyCallback> call = service.logout();
			Log.d("logout", "WOOOWOWOWOOWOWO");
			Log.d("logout service", "" + service);
			Log.d("logout call", "" + call);
			call.enqueue(new Callback<LogoutKeyCallback>() {
				@Override
				public void onResponse(Response<LogoutKeyCallback> response, Retrofit retrofit) {
					Log.d("logout", "response = " + new Gson().toJson(response));
					if (response.isSuccessful()) {
						// request successful (status code 200, 201)
						if (android.os.Build.VERSION.SDK_INT > 9) {
							StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
							StrictMode.setThreadPolicy(policy);
						}
						if (response.body().getStatus() == true) {
							session.logoutUser();
							dialog.dismiss();
							Toast.makeText(a.getApplicationContext(), a.getResources().getString(R.string.logout_berhasil), Toast.LENGTH_LONG).show();
							Intent i = new Intent(a.getApplicationContext(), LayarUtama.class);
							// Closing all the Activities
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							// Staring LoginActivity Activity
							a.startActivity(i);
							a.finish();
						} else {
							dialog.dismiss();
							Toast.makeText(context, a.getResources().getString(R.string.tidakdapatdilanjutkan), Toast.LENGTH_SHORT).show();
						}
					} else {
						dialog.dismiss();
						if (response.code() == 500) {
							Toast.makeText(context, a.getResources().getString(R.string.text_database_error), Toast.LENGTH_SHORT).show();
						} else {
							APIErrorCallback error = ErrorUtils.parseError(response);
							// … and use it to show error information

							// … or just log the issue like we’re doing :)
							Log.d("error message", error.message());

							if (error.message().equals("Unauthorized")) {
								Log.d("Unauthorized", "Jalannn");
								DialogHome.getInstance(context).dialogkey(a);

							} else {
								DialogHome.getInstance(context).dialoghome(a);
							}
						}
					}
				}

				@Override
				public void onFailure(Throwable t) {
				}
			});
		}catch (Exception ex){
			Toast.makeText(context, a.getResources().getString(R.string.text_calback_error), Toast.LENGTH_SHORT).show();
		}
	}*/

    RegisterFirebaseCallback registerGcmCallback;
    APIErrorCallback registerGcmErrorCallback;

    public void postRegisterFcm(Map<String, String> fcmParameters, String api, final RegisterFirebaseInterface registerFirebaseInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<RegisterFirebaseCallback> call = service.postRegisterFCM(fcmParameters);
        call.enqueue(new Callback<RegisterFirebaseCallback>() {
            @Override
            public void onResponse(Call<RegisterFirebaseCallback> call, Response<RegisterFirebaseCallback> response) {
//				Log.d("sts",""+response.raw());
//				Log.d("asdasdasdasd", "response = " + new Gson().toJson(response));
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    registerGcmCallback = response.body();
                    registerFirebaseInterface.onSuccessRegisterFirebase(registerGcmCallback);
                } else {
                    registerGcmErrorCallback = ErrorUtils.parseError(response);
                    registerFirebaseInterface.onErrorRegisterFirebase(registerGcmErrorCallback);
                }
            }

            @Override
            public void onFailure(Call<RegisterFirebaseCallback> call, Throwable t) {
                registerGcmErrorCallback =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    registerGcmErrorCallback.setError(error_msg+"");
                }
                else{
                    registerGcmErrorCallback.setError(context.getResources().getString(R.string.error));
                }
                registerFirebaseInterface.onErrorRegisterFirebase(registerGcmErrorCallback);
            }
        });
    }
    public RegisterFirebaseCallback getRegisterGcmCallback(){
        return registerGcmCallback;
    }
    public void setRegisterGcmCallback(RegisterFirebaseCallback registerGcmCallback){
        this.registerGcmCallback = registerGcmCallback;
    }
    public APIErrorCallback getRegisterGcmErrorCallback(){
        return registerGcmErrorCallback;
    }
    public void setRegisterGcmErrorCallback(APIErrorCallback registerGcmErrorCallback){
        this.registerGcmErrorCallback = registerGcmErrorCallback;
    }

    public Map<String,String> registerFcmParams(DeviceDataModel deviceDataModel, String fcmId, String district_id){
        Map<String, String> params = new HashMap<String, String>();
        params.put("device_name", deviceDataModel.deviceName+"");
        params.put("imei", deviceDataModel.imei+"");
        params.put("fcm_id", fcmId);
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(deviceDataModel.simCard)) {
            params.put("sim_card_name", deviceDataModel.simCard);
        }else{
            params.put("sim_card_name", "");
        }
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(deviceDataModel.phoneNumber)) {
            params.put("phone", deviceDataModel.phoneNumber);
        }else{
            params.put("phone","");
        }
        params.put("district_id", district_id);
        return  params;
    }

    VerificationCallback verificationCallback;
    APIErrorCallback verificationErrorCallback;

    public VerificationCallback getVerificationCallback(){
        return verificationCallback;
    }
    public void setVerificationCallback(VerificationCallback verificationCallback){
        this.verificationCallback = verificationCallback;
    }
    public APIErrorCallback getVerificationErrorCallback(){
        return verificationErrorCallback;
    }
    public void setVerificationErrorCallback(APIErrorCallback verificationErrorCallback){
        this.verificationErrorCallback = verificationErrorCallback;
    }
    public void postVerificationProfile(String token, Map<String,String> params, String api, final VerificationSmsInterface verificationSmsInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<VerificationCallback> call = service.postVerificationProfile(token, params);
        call.enqueue(new Callback<VerificationCallback>() {
            @Override
            public void onResponse(Call<VerificationCallback> call, Response<VerificationCallback> response) {
//				Log.d("sts",""+response.raw());
//				Log.d("asdasdasdasd", "response = " + new Gson().toJson(response));
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    VerificationCallback Data = response.body();
                    verificationSmsInterface.onSuccessVerificationSms(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    verificationSmsInterface.onErrorVerificationSms(error);
                }
            }

            @Override
            public void onFailure(Call<VerificationCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                verificationSmsInterface.onErrorVerificationSms(error);
            }
        });
    }
    public void postVerificationLogin(String token, String verification_code, String api, final VerificationSmsInterface verificationSmsInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<VerificationCallback> call = service.postVerificationLoginRegister(token, verification_code);
        call.enqueue(new Callback<VerificationCallback>() {
            @Override
            public void onResponse(Call<VerificationCallback> call, Response<VerificationCallback> response) {
//				Log.d("sts",""+response.raw());
//				Log.d("asdasdasdasd", "response = " + new Gson().toJson(response));
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    VerificationCallback Data = response.body();
                    verificationSmsInterface.onSuccessVerificationSms(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    verificationSmsInterface.onErrorVerificationSms(error);
                }
            }

            @Override
            public void onFailure(Call<VerificationCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                verificationSmsInterface.onErrorVerificationSms(error);
            }
        });
    }

    private BaseCallback sendBackVerificationSmsCallback;
    private APIErrorCallback sendBackVerificationSmsErrorCallback;

    public void postSendBackVerification(String token, final ProgressDialog progressBarNya, String api, final BaseInterface baseInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseCallback> call = service.postSendBackVerification(token);
        call.enqueue(new Callback<BaseCallback>() {
            @Override
            public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
                progressBarNya.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    sendBackVerificationSmsCallback = response.body();
                    baseInterface.onSuccess(sendBackVerificationSmsCallback);
                } else {
                    sendBackVerificationSmsErrorCallback = ErrorUtils.parseError(response);
                    baseInterface.onError(sendBackVerificationSmsErrorCallback);
                }
            }
            @Override
            public void onFailure(Call<BaseCallback> call, Throwable t) {
                sendBackVerificationSmsErrorCallback =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    sendBackVerificationSmsErrorCallback.setError(error_msg+"");
                }
                else{
                    sendBackVerificationSmsErrorCallback.setError(context.getResources().getString(R.string.error));
                }
                baseInterface.onSuccess(sendBackVerificationSmsCallback);
            }
        });
    }

    public BaseCallback getSendBackVerificationCallback(){
        return sendBackVerificationSmsCallback;
    }
    public void setSendBackVerificationCallback(BaseCallback sendBackVerificationSmsCallback){
        this.sendBackVerificationSmsCallback = sendBackVerificationSmsCallback;
    }
    public APIErrorCallback getSendBackVerificationErrorCallback(){
        return sendBackVerificationSmsErrorCallback;
    }
    public void setSendBackVerificationErrorCallback(APIErrorCallback sendBackVerificationSmsErrorCallback){
        this.sendBackVerificationSmsErrorCallback = sendBackVerificationSmsErrorCallback;
    }
    public void postSendBackVerificationProfile(String token, Map<String,String> params , final ProgressDialog progressBarNya, String api, final BaseInterface baseInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseCallback> call = service.postSendBackVerificationEditProfil(token, params);
        call.enqueue(new Callback<BaseCallback>() {
            @Override
            public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
                progressBarNya.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    baseCallback = response.body();
                    baseInterface.onSuccess(baseCallback);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseInterface.onError(error);
                }
            }
            @Override
            public void onFailure(Call<BaseCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseInterface.onError(error);
            }
        });
    }
    public void doUploadProfilePhoto(Map<String, ProgressRequestBody> imageData, String api, final UploadImageProfileInterface uploadImageProfileInterface) {
        Log.d("API", "" + api);
        ApiInterface service = RestImage.getClient(api);
        Call<UploadProfilePhotoCallback> call = service.doUploadProfilePhoto(imageData);
        call.enqueue(new Callback<UploadProfilePhotoCallback>() {
            @Override
            public void onResponse(Call<UploadProfilePhotoCallback> call, Response<UploadProfilePhotoCallback> response) {
//				Log.d("sts2",""+response.raw());
//				Log.d("sts2123",""+new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    UploadProfilePhotoCallback Data = response.body();
                    uploadImageProfileInterface.onSuccessUploadImageProfile(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    uploadImageProfileInterface.onErrorUploadImageProfile(error);
                }
            }

            @Override
            public void onFailure(Call<UploadProfilePhotoCallback> call, Throwable t) {
                APIErrorCallback error = new APIErrorCallback();
                String error_msg = t.getMessage();
                if (error_msg != null) {
                    error.setError(error_msg + "");
                } else {
                    error.setError(context.getResources().getString(R.string.error));
                }
                uploadImageProfileInterface.onErrorUploadImageProfile(error);
            }
        });
    }


    public void doDeleteProfilePhoto(String api, final BaseInterface baseInterface) {
        Log.d("API", "" + api);
        ApiInterface service = RestImage.getClient(api);
        Call<BaseCallback> call = service.doDeleteProfilePhoto();
        call.enqueue(new Callback<BaseCallback>() {
            @Override
            public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
//				Log.d("sts2",""+response.raw());
//				Log.d("sts2123",""+new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    BaseCallback Data = response.body();
                    baseInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseInterface.onError(error);
                }
            }
            @Override
            public void onFailure(Call<BaseCallback> call, Throwable t) {
                APIErrorCallback error = new APIErrorCallback();
                String error_msg = t.getMessage();
                if (error_msg != null) {
                    error.setError(error_msg + "");
                } else {
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseInterface.onError(error);
            }
        });
    }

    public void doUploadPhotoTemp(Map<String, ProgressRequestBody> imageData, String api, final UploadImageTempInterface uploadImageTempInterface) {
        Log.d("API", "" + api);
        ApiInterface service = RestImage.getClient(api);
        Call<UploadPhotoTempCallback> call = service.doUploadPhotoTemp(imageData);
        call.enqueue(new Callback<UploadPhotoTempCallback>() {
            @Override
            public void onResponse(Call<UploadPhotoTempCallback> call, Response<UploadPhotoTempCallback> response) {
//				Log.d("sts2",""+response.raw());
//				Log.d("sts2123",""+new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    UploadPhotoTempCallback Data = response.body();
                    uploadImageTempInterface.onSuccessUploadImageTemp(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    uploadImageTempInterface.onErrorUploadImageTemp(error);
                }
            }

            @Override
            public void onFailure(Call<UploadPhotoTempCallback> call, Throwable t) {
                APIErrorCallback error = new APIErrorCallback();
                String error_msg = t.getMessage();
                if (error_msg != null) {
                    error.setError(error_msg + "");
                } else {
                    error.setError(context.getResources().getString(R.string.error));
                }
                uploadImageTempInterface.onErrorUploadImageTemp(error);
            }
        });
    }
    public void getBanner(Map<String, String> params, String api, final BaseGenericInterface baseGenericInterface) {
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<List<BannerData>>> call = service.getBanner(params);
        call.enqueue(new Callback<BaseGenericCallback<List<BannerData>>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<List<BannerData>>> call, Response<BaseGenericCallback<List<BannerData>>> response) {
                if (response.isSuccessful()) {
                    BaseGenericCallback<List<BannerData>> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }
            @Override
            public void onFailure(Call<BaseGenericCallback<List<BannerData>>> call, Throwable t) {
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
//
//    public void postLocationMember(Map<String,String> params, String api){
//        ApiInterface service = RestClient.getClient(api);
//        Call<BaseCallback> call = service.postLocationMember(params);
//
//        call.enqueue(new Callback<BaseCallback>() {
//            @Override
//            public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
////				Log.d("booking", "getBookingTransportListDetail = " + new Gson().toJson(response.body()));
////				Log.d("sts", "" + response.raw());
//                if (response.isSuccessful()) {
//                    // request successful (status code 200, 201)
//                    BaseCallback Data = response.body();
//                    Data.setCallback("postLocationMember");
//                    BusProvider.getInstance().post(Data);
//                } else {
//                    APIErrorCallback error = ErrorUtils.parseError(response);
//                    error.setCallback("postLocationMember");
//                    BusProvider.getInstance().post(error);
//                }
//            }
//            @Override
//            public void onFailure(Call<BaseCallback> call, Throwable t) {
//                APIErrorCallback error =new APIErrorCallback();
//                error.setCallback("postLocationMember");
//                String error_msg = t.getMessage();
//                if(error_msg!=null) {
//                    error.setError(error_msg+"");
//                }
//                else{
//                    error.setError(context.getResources().getString(R.string.error));
//                }
//                BusProvider.getInstance().post(error);
//            }
//        });
//    }

    public void postEditProfil(Map<String,String> params, final ProgressDialog progressBarNya, String api, final EditProfileInterface editProfileInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<EditProfilCallback> call = service.postEditProfil(params);

        call.enqueue(new Callback<EditProfilCallback>() {
            @Override
            public void onResponse(Call<EditProfilCallback> call, Response<EditProfilCallback> response) {
                progressBarNya.dismiss();
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    EditProfilCallback baseCallback = response.body();
                    editProfileInterface.onSuccessEditProfile(baseCallback);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    editProfileInterface.onErrorEditProfile(error);
                }
            }
            @Override
            public void onFailure(Call<EditProfilCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                editProfileInterface.onErrorEditProfile(error);
            }
        });
    }

    public void getUserData(String api, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<UserModel>> call = service.getUserData();
        call.enqueue(new Callback<BaseGenericCallback<UserModel>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<UserModel>> call, Response<BaseGenericCallback<UserModel>> response) {
//				Log.d("booking", "raww = " + response.raw());
//				Log.d("booking", "getBookingTransportListDetail = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<UserModel> getUserDataCallback = response.body();
                    baseGenericInterface.onSuccess(getUserDataCallback);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseGenericCallback<UserModel>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }

    public void getMainMenuItem(String district_id, String api, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<ArrayList<MainMenuItemData>>> call = service.getMainMenuItem(district_id);

        call.enqueue(new Callback<BaseGenericCallback<ArrayList<MainMenuItemData>>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<ArrayList<MainMenuItemData>>> call, Response<BaseGenericCallback<ArrayList<MainMenuItemData>>> response) {
//				Log.d("booking", "raww = " + response.raw());
//				Log.d("booking", "getBookingTransportListDetail = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<ArrayList<MainMenuItemData>> data = response.body();
                    baseGenericInterface.onSuccess(data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseGenericCallback<ArrayList<MainMenuItemData>>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }

    public void sendReferral(String api, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<String>> call = service.sendReferral();
        call.enqueue(new Callback<BaseGenericCallback<String>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<String>> call, Response<BaseGenericCallback<String>> response) {
                if (response.isSuccessful()) {
                    BaseGenericCallback<String> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                }else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseGenericCallback<String>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }

    public void postFeedback(Map<String,String> params, final ProgressDialog progressBarNya, String api, final BaseInterface baseInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseCallback> call = service.postFeedback(params);
        call.enqueue(new Callback<BaseCallback>() {
            @Override
            public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
                progressBarNya.dismiss();
//				Log.d("feedback", "raww = " + response.raw());
//				Log.d("feedback", "getfeedback = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Log.d("CHECKRETROFIT",">");
                    // request successful (status code 200, 201)
                    BaseCallback Data = response.body();
                    baseInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseInterface.onError(error);
            }

        });
    }

    public void deniedAccess(Map<String,String> params, String api, final BaseInterface baseInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseCallback> call = service.deniedAccess(params);
        call.enqueue(new Callback<BaseCallback>() {
            @Override
            public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
                if (response.isSuccessful()) {
                    BaseCallback Data = response.body();
                    baseInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseCallback> call, Throwable t) {
                APIErrorCallback error = new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseInterface.onError(error);
            }
        });
    }

    public void postLoginGoogle(UserModel userModel, String api, final LoginInterface loginInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<LoginCallback> call = service.postLoginGoogle(
                userModel.getAvatar(),
                userModel.getEmail(),
                userModel.getFullname(),
                userModel.getSocialId()
        );
        call.enqueue(new Callback<LoginCallback>() {
            @Override
            public void onResponse(Call<LoginCallback> call, Response<LoginCallback> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    LoginCallback Data = response.body();
                    loginInterface.onSuccessLogin(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    loginInterface.onErrorLogin(error);
                }
            }

            @Override
            public void onFailure(Call<LoginCallback> call, Throwable t) {
                APIErrorCallback error = new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                loginInterface.onErrorLogin(error);
            }
        });
    }

    public void postLoginFacebook(UserModel userModel, String api, final LoginInterface loginInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<LoginCallback> call = service.postLoginFacebook(
                userModel.getAvatar(),
                userModel.getEmail(),
                userModel.getFullname(),
                userModel.getSocialId()
        );
        call.enqueue(new Callback<LoginCallback>() {
            @Override
            public void onResponse(Call<LoginCallback> call, Response<LoginCallback> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    LoginCallback Data = response.body();
                    loginInterface.onSuccessLogin(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    loginInterface.onErrorLogin(error);
                }
            }

            @Override
            public void onFailure(Call<LoginCallback> call, Throwable t) {
                APIErrorCallback error = new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                loginInterface.onErrorLogin(error);
            }
        });
    }
    public void getYahooProfile(String accessToken, final YahooProfileInterface yahooProfileInterface){
        ApiInterface service = YahooClient.getClient(accessToken);
        Call<YahooProfileCallback> call = service.getYahooProfile();
        call.enqueue(new Callback<YahooProfileCallback>() {
            @Override
            public void onResponse(Call<YahooProfileCallback> call, Response<YahooProfileCallback> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    YahooProfileCallback Data = response.body();
                    yahooProfileInterface.onSuccessYahooProfile(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    yahooProfileInterface.onErrorYahooProfile(error);
                }
            }

            @Override
            public void onFailure(Call<YahooProfileCallback> call, Throwable t) {
                APIErrorCallback error = new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                yahooProfileInterface.onErrorYahooProfile(error);
            }
        });
    }
    public void postLoginYahoo(UserModel userModel, String api, final LoginInterface loginInterface){
        ApiInterface service = RestClient.getClient(api);
        Map<String,String> data = new HashMap<String,String>();
        Call<LoginCallback> call = service.postLoginYahoo(
                userModel.getAvatar(),
                userModel.getEmail(),
                userModel.getFullname(),
                userModel.getSocialId(),
                userModel.getPhone()
        );
        call.enqueue(new Callback<LoginCallback>() {
            @Override
            public void onResponse(Call<LoginCallback> call, Response<LoginCallback> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    LoginCallback Data = response.body();
                    loginInterface.onSuccessLogin(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    loginInterface.onErrorLogin(error);
                }
            }

            @Override
            public void onFailure(Call<LoginCallback> call, Throwable t) {
                APIErrorCallback error = new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                loginInterface.onErrorLogin(error);
            }
        });
    }

    public void postBonusReferal(String imei, String code, String api, final BaseInterface baseInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseCallback> call = service.postBonusReferal(imei, code);
        call.enqueue(new Callback<BaseCallback>() {
            @Override
            public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseCallback Data = response.body();
                    baseInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseCallback> call, Throwable t) {
                APIErrorCallback error = new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseInterface.onError(error);
            }
        });
    }

    public void getCheckDeposit(String api, final BaseGenericInterface baseGenericInterface) {
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<CheckDepositData>> call = service.getCheckDeposit();
        call.enqueue(new Callback<BaseGenericCallback<CheckDepositData>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<CheckDepositData>> call, Response<BaseGenericCallback<CheckDepositData>> response) {

                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<CheckDepositData> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseGenericCallback<CheckDepositData>> call, Throwable t) {
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

    public void postCompletingData(HashMap<String,String> map , String api, final CompletingProfileInterface completingProfileInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<CompletingDataCallback> call = service.postCompletingData(map);
        call.enqueue(new Callback<CompletingDataCallback>() {
            @Override
            public void onResponse(Call<CompletingDataCallback> call, Response<CompletingDataCallback> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    CompletingDataCallback Data = response.body();
                    completingProfileInterface.onSuccessCompletingData(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    completingProfileInterface.onErrorCompletingData(error);
                }
            }

            @Override
            public void onFailure(Call<CompletingDataCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                completingProfileInterface.onErrorCompletingData(error);
            }
        });
    }

    public void doUploadPaymentEvidencePhoto(String request_id, Map<String, ProgressRequestBody> imageData, String api, final UploadImageInterface uploadImageInterface){
        ApiInterface service = RestImage.getClient(api);
        Call<UploadPhotoCallback> call = service.doUploadPaymentEvidencePhoto(request_id, imageData);

        call.enqueue(new Callback<UploadPhotoCallback>() {
            @Override
            public void onResponse(Call<UploadPhotoCallback> call, Response<UploadPhotoCallback> response) {
                if (response.isSuccessful()) {
                    UploadPhotoCallback Data = response.body();
                    uploadImageInterface.onSuccessUploadImage(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    uploadImageInterface.onErrorUploadImage(error);
                }
            }

            @Override
            public void onFailure(Call<UploadPhotoCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                uploadImageInterface.onErrorUploadImage(error);
            }
        });
    }
    public void sendDepositConfirmStatus(String id, String api, final BaseInterface baseInterface){
        ApiInterface service = RestImage.getClient(api);
        Call<BaseCallback> call = service.sendStatusConfirmDeposit(id);

        call.enqueue(new Callback<BaseCallback>() {
            @Override
            public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
                if (response.isSuccessful()) {
                    BaseCallback Data = response.body();
                    baseInterface.onSuccess(Data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseInterface.onError(error);
            }
        });
    }
    public void sendVouchercode(String imei, String code_promo, String api, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback> call = service.postVoucherCode(imei, code_promo);
        call.enqueue(new Callback<BaseGenericCallback>() {
            @Override
            public void onResponse(Call<BaseGenericCallback> call, Response<BaseGenericCallback> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback data = response.body();
                    baseGenericInterface.onSuccess(data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseGenericCallback> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }
    public HashMap<String,String> promoListParams(String request_type){
        HashMap<String, String> map = new HashMap<>();
        map.put("request_type",request_type);
        return map;
    }
    public void getPromoCodeList(String api,HashMap<String,String> map, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<PromoListModel>> call = service.getPromoCodeList(map);
        call.enqueue(new Callback<BaseGenericCallback<PromoListModel>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<PromoListModel>> call, Response<BaseGenericCallback<PromoListModel>> response) {
                if (response.isSuccessful()) {
                    // request successful (status code 200, 201)
                    BaseGenericCallback<PromoListModel> data = response.body();
                    baseGenericInterface.onSuccess(data);
                } else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }

            @Override
            public void onFailure(Call<BaseGenericCallback<PromoListModel>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }
    public void getSearchHelp(String subject, String api, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<List<HelpSubCategoryData>>> call = service.getSearchHelpData(subject);
        call.enqueue(new Callback<BaseGenericCallback<List<HelpSubCategoryData>>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<List<HelpSubCategoryData>>> call, Response<BaseGenericCallback<List<HelpSubCategoryData>>> response) {
                if (response.isSuccessful()) {
                    BaseGenericCallback<List<HelpSubCategoryData>> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                }
                else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }
            @Override
            public void onFailure(Call<BaseGenericCallback<List<HelpSubCategoryData>>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }
    public void getHelpServiceSelected(String id,String api, final BaseGenericInterface baseGenericInterface){
        ApiInterface service = RestClient.getClient(api);
        Call<BaseGenericCallback<List<HelpSubCategoryData>>> call = service.getHelpServiceSelectedData(id);
        call.enqueue(new Callback<BaseGenericCallback<List<HelpSubCategoryData>>>() {
            @Override
            public void onResponse(Call<BaseGenericCallback<List<HelpSubCategoryData>>> call, Response<BaseGenericCallback<List<HelpSubCategoryData>>> response) {
                if (response.isSuccessful()) {
                    BaseGenericCallback<List<HelpSubCategoryData>> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
                }else {
                    APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
                }
            }
            @Override
            public void onFailure(Call<BaseGenericCallback<List<HelpSubCategoryData>>> call, Throwable t) {
                APIErrorCallback error =new APIErrorCallback();
                String error_msg = t.getMessage();
                if(error_msg!=null) {
                    error.setError(error_msg+"");
                }
                else{
                    error.setError(context.getResources().getString(R.string.error));
                }
                baseGenericInterface.onError(error);
            }
        });
    }
}
