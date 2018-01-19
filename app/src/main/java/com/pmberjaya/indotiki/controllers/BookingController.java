package com.pmberjaya.indotiki.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.bookingData.BookingCancelCallback;
import com.pmberjaya.indotiki.callbacks.bookingNew.TimeFeeCallback;
import com.pmberjaya.indotiki.callbacks.chat.UploadChatImageCallback;
import com.pmberjaya.indotiki.callbacks.bookingNew.UploadPhotoCallback;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.base.BaseInterface;
import com.pmberjaya.indotiki.interfaces.bookingData.BookingCancelInterface;
import com.pmberjaya.indotiki.interfaces.bookingData.BookingCompleteInterface;
import com.pmberjaya.indotiki.interfaces.bookingData.BookingReasonCancelInterface;
import com.pmberjaya.indotiki.interfaces.bookingData.DriverPositionInterface;
import com.pmberjaya.indotiki.interfaces.main.MainMenuInterface;
import com.pmberjaya.indotiki.interfaces.bookingNew.TimeFeeInterface;
import com.pmberjaya.indotiki.interfaces.chat.UploadChatImageInterface;
import com.pmberjaya.indotiki.interfaces.bookingNew.UploadImageInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.io.ApiInterface;
import com.pmberjaya.indotiki.io.ErrorUtils;
import com.pmberjaya.indotiki.io.ProgressRequestBody;
import com.pmberjaya.indotiki.io.RestClient;
import com.pmberjaya.indotiki.io.RestImage;
import com.pmberjaya.indotiki.io.Token;
import com.pmberjaya.indotiki.models.account.DeviceDataModel;
import com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberData.BookingCompleteModel;
import com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberDetailData;
import com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberData.BookingInProgressModel;
import com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberDetailData;
import com.pmberjaya.indotiki.models.bookingData.BookingReasonCancelData;
import com.pmberjaya.indotiki.models.bookingData.BookingThisTripAgainData;
import com.pmberjaya.indotiki.models.others.CheckStatusBookingData;
import com.pmberjaya.indotiki.models.bookingNew.DriverPositionListData.DriverPositionListModel;
import com.pmberjaya.indotiki.models.bookingData.DriverPositionMapData;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable;
import com.pmberjaya.indotiki.utilities.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingController extends BaseController{
	private static BookingController _instance;
	private Context context;
	private String message;
	private SessionManager session;
	private DBController db_controller;
	private String api;
	private BookingReasonCancelInterface bookingReasonCancelInterface;
	private UploadImageInterface uploadImageInterface;
	private MainMenuInterface mainMenuInterface;
	private BookingCompleteInterface bookingCompleteInterface;

	protected BookingController(Context paramContext)
	{
		super(paramContext);
		this.context = paramContext;
		session= new SessionManager(paramContext);
		api = Token.getInstance(context).getToken();
		Log.d("api", "api = " +api);
	}

	public static BookingController getInstance(Context paramContext)
	{
		if (_instance == null) {
			_instance = new BookingController(paramContext);
		}
		return _instance;
	}

	public void setInterface(BookingReasonCancelInterface bookingReasonCancelInterface){
		this.bookingReasonCancelInterface = bookingReasonCancelInterface;
	}

	public void sendChatNotification(Map<String,String> params, String api){
		ApiInterface service = RestClient.getClient(api);
		Call<BaseCallback> call = service.sendChatNotification(params);
		call.enqueue(new Callback<BaseCallback>() {
			@Override
			public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
					BaseCallback Data = response.body();
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
				}
			}

			@Override
			public void onFailure(Call<BaseCallback> call, Throwable t) {
				APIErrorCallback error =new APIErrorCallback();
			}
		});
	}
	public void doUploadChatImage(Map<String, ProgressRequestBody> imageData , String channel, String api, final UploadChatImageInterface uploadChatImageInterface){
		Log.d("API",""+api);
		ApiInterface service = RestImage.getClient(api);
		Call<UploadChatImageCallback> call = service.doUploadChatImage(imageData, channel);
		call.enqueue(new Callback<UploadChatImageCallback>() {
			@Override
			public void onResponse(Call<UploadChatImageCallback> call, Response<UploadChatImageCallback> response) {
//				Log.d("sts2",""+response.raw());
//				Log.d("sts2123",""+new Gson().toJson(response.body()));
				if (response.isSuccessful()) {
					UploadChatImageCallback Data = response.body();
					uploadChatImageInterface.onSuccessUploadImage(Data);
					//progressDialog.dismiss();
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
					uploadChatImageInterface.onErrorUploadImage(error);
				}
			}

			@Override
			public void onFailure(Call<UploadChatImageCallback> call, Throwable t) {
				APIErrorCallback error =new APIErrorCallback();
				String error_msg = t.getMessage();
				if(error_msg!=null) {
					error.setError(error_msg+"");
				}
				else{
					error.setError(context.getResources().getString(R.string.error));
				}
				uploadChatImageInterface.onErrorUploadImage(error);
			}
		});
	}

	public void getBookingReasonCancel(String api){
		ApiInterface service = RestImage.getClient(api);
		Call<BaseGenericCallback<List<BookingReasonCancelData>>> call = service.getBookingReasonCancel();
		call.enqueue(new Callback<BaseGenericCallback<List<BookingReasonCancelData>>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<List<BookingReasonCancelData>>> call, Response<BaseGenericCallback<List<BookingReasonCancelData>>> response) {
				if (response.isSuccessful()) {
					BaseGenericCallback<List<BookingReasonCancelData>> Data = response.body();
					bookingReasonCancelInterface.onSuccessGetBookingReasonCancel(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
					bookingReasonCancelInterface.onErrorGetBookingReasonCancel(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<List<BookingReasonCancelData>>> call, Throwable t) {
				APIErrorCallback error =new APIErrorCallback();
				String error_msg = t.getMessage();
				if(error_msg!=null) {
					error.setError(error_msg+"");
				}
				else{
					error.setError(context.getResources().getString(R.string.error));
				}
				bookingReasonCancelInterface.onErrorGetBookingReasonCancel(error);
			}
		});
	}


	public void postUpdateBookingRatingDriver(Map<String,String> params, final ProgressDialog progressBarNya, String api, final BaseInterface baseInterface){
		ApiInterface service = RestClient.getClient(api);
		Call<BaseCallback> call = service.postUpdateBookingRatingDriver(params);
		call.enqueue(new Callback<BaseCallback>() {
			@Override
			public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
				progressBarNya.dismiss();
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

	public void setMainMenuInterface(MainMenuInterface mainMenuInterface) {
		this.mainMenuInterface = mainMenuInterface;
	}

	public void setBookingCompleteInterface(BookingCompleteInterface bookingCompleteInterface) {
		this.bookingCompleteInterface = bookingCompleteInterface;
	}

	public void setUploadImageInterface(UploadImageInterface uploadImageInterface) {
		this.uploadImageInterface = uploadImageInterface;
	}

	public void getBookingInProgressMember(String[] filterArray, String api, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseGenericCallback<BookingInProgressModel>> call = service.getBookingInProgressMember(filterArray);
		call.enqueue(new Callback<BaseGenericCallback<BookingInProgressModel>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<BookingInProgressModel>> call, Response<BaseGenericCallback<BookingInProgressModel>> response) {
//				Log.d("sts",""+response.raw());
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
					BaseGenericCallback<BookingInProgressModel> Data = response.body();
					baseGenericInterface.onSuccess(Data);

				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
					baseGenericInterface.onError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<BookingInProgressModel>> call, Throwable t) {
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

	public void getBookingInProgressDetail(String requestId, String requestType, String api, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseGenericCallback<BookingInProgressMemberDetailData>> call = service.getBookingInProgressDetail(requestId, requestType);
		call.enqueue(new Callback<BaseGenericCallback<BookingInProgressMemberDetailData>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<BookingInProgressMemberDetailData>> call, Response<BaseGenericCallback<BookingInProgressMemberDetailData>> response) {
//				Log.d("sts",""+response.raw());
//				Log.d("sts2",""+new Gson().toJson(response.body()));
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
					BaseGenericCallback<BookingInProgressMemberDetailData> bookingInProgressData = response.body();
					baseGenericInterface.onSuccess(bookingInProgressData);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
					baseGenericInterface.onError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<BookingInProgressMemberDetailData>> call, Throwable t) {
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

	public void getDriverPositionMap(String driverId, String api, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseGenericCallback<DriverPositionMapData>> call = service.getDriverPositionMap(driverId);
		call.enqueue(new Callback<BaseGenericCallback<DriverPositionMapData>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<DriverPositionMapData>> call, Response<BaseGenericCallback<DriverPositionMapData>> response) {
				if (response.isSuccessful()) {
                    BaseGenericCallback<DriverPositionMapData> data = response.body();
                    baseGenericInterface.onSuccess(data);

				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<DriverPositionMapData>> call, Throwable t) {
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


	public void getTimeFee(Map<String, String> parameters, String api, final TimeFeeInterface timeFeeInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<TimeFeeCallback> call = service.getTimeFee(parameters);
		call.enqueue(new Callback<TimeFeeCallback>() {
			@Override
			public void onResponse(Call<TimeFeeCallback> call, Response<TimeFeeCallback> response) {
				if (response.isSuccessful()) {
					TimeFeeCallback Data = response.body();
                    timeFeeInterface.onSuccessGetTimeFee(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
                    timeFeeInterface.onErrorGetTimefee(error);
				}
			}

			@Override
			public void onFailure(Call<TimeFeeCallback> call, Throwable t) {
				APIErrorCallback error = new APIErrorCallback();
				String error_msg = t.getMessage();
				if (error_msg != null) {
					error.setError(error_msg + "");
					if(!error_msg.equals("Canceled")){
                        timeFeeInterface.onErrorGetTimefee(error);
					}
				} else {
					error.setError(context.getResources().getString(R.string.error));
                    timeFeeInterface.onErrorGetTimefee(error);
				}
			}
		});
        timeFeeInterface.callCancel(call);
		//
	}

	public Map<String, String> timeFeeParams(BookingDataParcelable bookingDataParcelable) {
		Map<String, String> params = new HashMap<String, String>();
		if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.promoCode)) {
			params.put("code_promo", bookingDataParcelable.promoCode);
		}else{
			params.put("code_promo", "");
		}
		if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.tip)) {
            params.put("tip", bookingDataParcelable.tip);
        }else{
            params.put("tip", "");
        }
		params.put("transport", bookingDataParcelable.transportation);
		params.put("request_type", bookingDataParcelable.requestType);
		params.put("district_id", bookingDataParcelable.district);
		params.put("payment", bookingDataParcelable.payment);
		if(bookingDataParcelable.requestType.equals("mart")){
			params.put("total_item_price", String.valueOf(bookingDataParcelable.bookingMartDatas.itemCost));
		}else {
			params.put("total_item_price", String.valueOf(bookingDataParcelable.bookingFoodDatas.foodCost));
		}
		params.put("from_lat", String.valueOf(bookingDataParcelable.latFrom));
		params.put("from_lng", String.valueOf(bookingDataParcelable.lngFrom));
		params.put("to_lat", String.valueOf(bookingDataParcelable.latTo));
		params.put("to_lng", String.valueOf(bookingDataParcelable.lngTo));
		return params;
	}

	public void postRequestResponseRepeat(Map<String, String> parameters, String api, final BaseInterface baseInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseCallback> call = service.postRequestResponseRepeat(parameters);
		call.enqueue(new Callback<BaseCallback>() {
			@Override
			public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
//					Log.d("sts",""+response.raw());
//					Log.d("sts2",""+new Gson().toJson(response.body()));
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

	public void postRequestResponseCancel(Map<String, String> parameters, String api, final BookingCancelInterface bookingCancelInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BookingCancelCallback> call = service.postRequestResponseCancel(parameters);
		call.enqueue(new Callback<BookingCancelCallback>() {
			@Override
			public void onResponse(Call<BookingCancelCallback> call, Response<BookingCancelCallback> response) {
				if (response.isSuccessful()) {
					BookingCancelCallback Data = response.body();
                    bookingCancelInterface.onSuccessCancelBooking(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
                    bookingCancelInterface.onErrorCancelBooking(error);
				}
			}

			@Override
			public void onFailure(Call<BookingCancelCallback> call, Throwable t) {
				APIErrorCallback error = new APIErrorCallback();
				String error_msg = t.getMessage();
				if (error_msg != null) {
					error.setError(error_msg + "");
				} else {
					error.setError(context.getResources().getString(R.string.error));
				}
                bookingCancelInterface.onErrorCancelBooking(error);
			}
		});
	}

	public void getCheckStatusBooking(String requestId, String requestType, String api, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);
		Log.d("API", ">" + api);
		Call<BaseGenericCallback<CheckStatusBookingData>> call = service.getCheckStatusBooking(requestId, requestType);
		call.enqueue(new Callback<BaseGenericCallback<CheckStatusBookingData>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<CheckStatusBookingData>> call, Response<BaseGenericCallback<CheckStatusBookingData>> response) {
//				Log.d("sts",""+response.raw());
//				Log.d("sts2",""+new Gson().toJson(response.body()));
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
					BaseGenericCallback<CheckStatusBookingData> Data = response.body();
					baseGenericInterface.onSuccess(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
					baseGenericInterface.onError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<CheckStatusBookingData>> call, Throwable t) {
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

	public void getBookingCompleteMemberRefresh(String[] filterArray, String api) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseGenericCallback<BookingCompleteModel>> call = service.getBookingCompleteMember(filterArray);
		call.enqueue(new Callback<BaseGenericCallback<BookingCompleteModel>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<BookingCompleteModel>> call, Response<BaseGenericCallback<BookingCompleteModel>> response) {
//				Log.d("sts2",""+response.raw());
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
					BaseGenericCallback<BookingCompleteModel> Data = response.body();
					bookingCompleteInterface.onGetBookingCompletedSuccess(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
					bookingCompleteInterface.onGetBookingCompletedError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<BookingCompleteModel>> call, Throwable t) {
				APIErrorCallback error = new APIErrorCallback();
				String error_msg = t.getMessage();
				if (error_msg != null) {
					error.setError(error_msg + "");
				} else {
					error.setError(context.getResources().getString(R.string.error));
				}
				bookingCompleteInterface.onGetBookingCompletedError(error);
			}

		});
	}

	public void getBookingInProgressMemberRefresh(String[] filterArray, String api, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseGenericCallback<BookingInProgressModel>> call = service.getBookingInProgressMember(filterArray);
		call.enqueue(new Callback<BaseGenericCallback<BookingInProgressModel>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<BookingInProgressModel>> call, Response<BaseGenericCallback<BookingInProgressModel>> response) {
//				Log.d("sts",""+response.raw());
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
					BaseGenericCallback<BookingInProgressModel> Data = response.body();
                    baseGenericInterface.onSuccess(Data);

				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<BookingInProgressModel>> call, Throwable t) {
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

	public void getBookingCompleteMember(String[] filterArray, String api, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseGenericCallback<BookingCompleteModel>> call = service.getBookingCompleteMember(filterArray);
		call.enqueue(new Callback<BaseGenericCallback<BookingCompleteModel>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<BookingCompleteModel>> call, Response<BaseGenericCallback<BookingCompleteModel>> response) {
//				Log.d("sts2",""+response.raw());
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
					BaseGenericCallback<BookingCompleteModel> Data = response.body();
					baseGenericInterface.onSuccess(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
					baseGenericInterface.onError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<BookingCompleteModel>> call, Throwable t) {
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

	public void getPositionDriverList(Map<String, String> params, String api, final DriverPositionInterface driverPositionInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseGenericCallback<DriverPositionListModel>> call = service.getPositionDriverList(params);
		call.enqueue(new Callback<BaseGenericCallback<DriverPositionListModel>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<DriverPositionListModel>> call, Response<BaseGenericCallback<DriverPositionListModel>> response) {
//				Log.d("sts2",""+response.raw());
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
					BaseGenericCallback<DriverPositionListModel> Data = response.body();
					driverPositionInterface.onSuccessGetDriverPosition(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
					driverPositionInterface.onErrorGetDriverPosition(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<DriverPositionListModel>> call, Throwable t) {
				APIErrorCallback error = new APIErrorCallback();
				String error_msg = t.getMessage();
				if (error_msg != null) {
					error.setError(error_msg + "");
				} else {
					error.setError(context.getResources().getString(R.string.error));
				}
				driverPositionInterface.onErrorGetDriverPosition(error);
			}
		});
	}
	public Map<String, String> positionDriverParameters(BookingDataParcelable bookingDataParcelable) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("transportation", bookingDataParcelable.transportation);
		params.put("lat", String.valueOf(bookingDataParcelable.latFrom));
		params.put("lng", String.valueOf(bookingDataParcelable.lngFrom));
		return params;
	}
	public void bookingAgain(String request_id, final String request_type, String api, final ProgressDialog pDialog, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseGenericCallback<BookingThisTripAgainData>> call = service.postBookingAgainMember(request_id, request_type);
		call.enqueue(new Callback<BaseGenericCallback<BookingThisTripAgainData>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<BookingThisTripAgainData>> call, Response<BaseGenericCallback<BookingThisTripAgainData>> response) {
				pDialog.dismiss();
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
					BaseGenericCallback<BookingThisTripAgainData> Data = response.body();
					Data.setRequest_type(request_type);
                    baseGenericInterface.onSuccess(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<BookingThisTripAgainData>> call, Throwable t) {
				pDialog.dismiss();
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

	public void requestTransport(Map<String, String> transportParams, String api, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);

		Call<BaseGenericCallback<String>> call = service.postRequestTransport(transportParams);
		call.enqueue(new Callback<BaseGenericCallback<String>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<String>> call, Response<BaseGenericCallback<String>> response) {
//				Log.d("sts2",""+response.raw());
//				Log.d("sts3",""+new Gson().toJson(response.body().toString()));
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)

                    BaseGenericCallback<String> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<String>> call, Throwable t) {
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

	public Map<String, String> requestTransportParams(BookingDataParcelable bookingDataParcelable) {
		String channel = Utility.getInstance().shuffle();
		DeviceDataModel deviceDataModel = Utility.getInstance().getDeviceData(getContext());
		String imei_member = deviceDataModel.imei;
		if (bookingDataParcelable.fromPlace == null) {
			bookingDataParcelable.fromPlace = "";
		}
		if (bookingDataParcelable.toPlace == null) {
			bookingDataParcelable.toPlace = "";
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("transportation", bookingDataParcelable.transportation);
		params.put("from_place", bookingDataParcelable.fromPlace);
		params.put("from", bookingDataParcelable.from);
		params.put("to_place", bookingDataParcelable.toPlace);
		params.put("to", bookingDataParcelable.to);
		params.put("detail", bookingDataParcelable.bookingTransportDatas.locationDetail);
		params.put("distance", bookingDataParcelable.distance);
		if(bookingDataParcelable.tip!=null){
			params.put("tip",bookingDataParcelable.tip);
		}else{
			params.put("tip","");
		}
		params.put("start_lat", String.valueOf(bookingDataParcelable.latFrom));
		params.put("start_lng", String.valueOf(bookingDataParcelable.lngFrom));
		params.put("end_lat", String.valueOf(bookingDataParcelable.latTo));
		params.put("end_lng", String.valueOf(bookingDataParcelable.lngTo));
		params.put("payment", bookingDataParcelable.payment);
		params.put("channel", channel);
		if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.promoCode)) {
			params.put("code_promo", bookingDataParcelable.promoCode);
		}else{
			params.put("code_promo", "");
		}
		params.put("district_id", bookingDataParcelable.district);

		if (bookingDataParcelable.originalPrice != null) {
			params.put("original_price", bookingDataParcelable.originalPrice);
		} else {
			params.put("original_price", bookingDataParcelable.price);
		}
		params.put("price", bookingDataParcelable.price);
		if (bookingDataParcelable.promoPrice != null) {
			params.put("promo_price", bookingDataParcelable.promoPrice);
		} else {
			params.put("promo_price", "");
		}
		if (bookingDataParcelable.categoryVoucher != null) {
			params.put("promo_category_voucher", bookingDataParcelable.categoryVoucher);
		} else {
			params.put("promo_category_voucher", "");
		}
		params.put("deposit_paid", bookingDataParcelable.depositPaid);
		params.put("cash_paid", bookingDataParcelable.cashPaid);
		params.put("imei_member", imei_member);
		return params;
	}
	public void requestCourier(Map<String, String> params, String api, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseGenericCallback<String>> call = service.postRequestCourier(params);
		call.enqueue(new Callback<BaseGenericCallback<String>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<String>> call, Response<BaseGenericCallback<String>> response) {
				if (response.isSuccessful()) {
                    BaseGenericCallback<String> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
                    baseGenericInterface.onError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<String>> call, Throwable t) {
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

	public Map<String, String> requestCourierParams(BookingDataParcelable bookingDataParcelable) {
        String channel = Utility.getInstance().shuffle();
        DeviceDataModel deviceDataModel = Utility.getInstance().getDeviceData(getContext());
        String imei_member = deviceDataModel.imei;
        Map<String, String> params = new HashMap<String, String>();
        if(bookingDataParcelable.fromPlace==null){
            bookingDataParcelable.fromPlace="";
        }
        if(bookingDataParcelable.toPlace==null){
            bookingDataParcelable.toPlace="";
        }
        params.put("transportation", bookingDataParcelable.transportation);
        params.put("from_place", bookingDataParcelable.fromPlace);
        params.put("from", bookingDataParcelable.from);
        params.put("to_place", bookingDataParcelable.toPlace);
        params.put("to", bookingDataParcelable.to);
        params.put("location_detail_sender", bookingDataParcelable.bookingCourierDatas.location_detail_sender);
        params.put("location_detail_receiver", bookingDataParcelable.bookingCourierDatas.location_detail_receiver);
        params.put("distance", bookingDataParcelable.distance);
        if(bookingDataParcelable.tip!=null){
            params.put("tip",bookingDataParcelable.tip);
        }else{
            params.put("tip","");
        }
        params.put("start_lat", String.valueOf(bookingDataParcelable.latFrom));
        params.put("start_lng", String.valueOf(bookingDataParcelable.lngFrom));
        params.put("end_lat", String.valueOf(bookingDataParcelable.latTo));
        params.put("end_lng", String.valueOf(bookingDataParcelable.lngTo));
        params.put("name_sender", bookingDataParcelable.bookingCourierDatas.name_sender);
        params.put("phone_sender", bookingDataParcelable.bookingCourierDatas.phone_sender);
        params.put("name_receiver", bookingDataParcelable.bookingCourierDatas.name_receiver);
        params.put("phone_receiver", bookingDataParcelable.bookingCourierDatas.phone_receiver);
        params.put("item_delivered", bookingDataParcelable.bookingCourierDatas.item);
        params.put("payment", bookingDataParcelable.payment);
        params.put("item_photo", bookingDataParcelable.bookingCourierDatas.item_photo);
        params.put("channel", channel);
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(bookingDataParcelable.promoCode)) {
            params.put("code_promo", bookingDataParcelable.promoCode);
        }else{
            params.put("code_promo", "");
        }
        params.put("district_id",bookingDataParcelable.district);
        if(bookingDataParcelable.originalPrice!=null) {
            params.put("original_price", bookingDataParcelable.originalPrice);
        }else{
            params.put("original_price", bookingDataParcelable.price);
        }
        params.put("price", bookingDataParcelable.price);
        if(bookingDataParcelable.categoryVoucher!=null) {
            params.put("promo_category_voucher", bookingDataParcelable.categoryVoucher);
        }else{
            params.put("promo_category_voucher", "");
        }
        if(bookingDataParcelable.promoPrice!=null) {
            params.put("promo_price", bookingDataParcelable.promoPrice);
        }else{
            params.put("promo_price", "");
        }
        params.put("deposit_paid",bookingDataParcelable.depositPaid);
        params.put("cash_paid",bookingDataParcelable.cashPaid);
        params.put("imei_member", imei_member);
        return  params;
	}
	public void requestFood(String restaurant_id, Map<String, String> params, String api, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseGenericCallback<String>> call = service.postRequestFood(restaurant_id, params);
		call.enqueue(new Callback<BaseGenericCallback<String>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<String>> call, Response<BaseGenericCallback<String>> response) {
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
                    BaseGenericCallback<String> Data = response.body();
					baseGenericInterface.onSuccess(Data);
				} else {
					Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<String>> call, Throwable t) {
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

	public void doUploadPhotoCourier(Map<String, ProgressRequestBody> imageData, String api) {
		ApiInterface service = RestImage.getClient(api);
		Call<UploadPhotoCallback> call = service.doUploadPhotoCourier(imageData);

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
				APIErrorCallback error = new APIErrorCallback();
				String error_msg = t.getMessage();
				if (error_msg != null) {
					error.setError(error_msg + "");
				} else {
					error.setError(context.getResources().getString(R.string.error));
				}
				uploadImageInterface.onErrorUploadImage(error);
			}
		});
	}


	public void getBookingCompleteDetail(String request_id, String request_type, String api, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseGenericCallback<BookingCompleteMemberDetailData>> call = service.getBookingCompleteMemberDetail(request_id, request_type);
		call.enqueue(new Callback<BaseGenericCallback<BookingCompleteMemberDetailData>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<BookingCompleteMemberDetailData>> call, Response<BaseGenericCallback<BookingCompleteMemberDetailData>> response) {
//				Log.d("sts2",""+response.raw());
//				Log.d("sts2123",""+new Gson().toJson(response.body()));
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
					BaseGenericCallback<BookingCompleteMemberDetailData> Data = response.body();
                    baseGenericInterface.onSuccess(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
					baseGenericInterface.onError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<BookingCompleteMemberDetailData>> call, Throwable t) {
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


	public void getBookingCancelData(String request_id, final String request_type, String api, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseGenericCallback<BookingThisTripAgainData>> call = service.postBookingAgainMember(request_id, request_type);
		call.enqueue(new Callback<BaseGenericCallback<BookingThisTripAgainData>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<BookingThisTripAgainData>> call, Response<BaseGenericCallback<BookingThisTripAgainData>> response) {
				if (response.isSuccessful()) {
					// request successful (status code 200, 201)
					BaseGenericCallback<BookingThisTripAgainData> Data = response.body();
					Data.setRequest_type(request_type);
					baseGenericInterface.onSuccess(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
					baseGenericInterface.onError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<BookingThisTripAgainData>> call, Throwable t) {
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

	public void sendReceipt(String request_id, String request_type, String api, final BaseInterface baseInterface) {
		ApiInterface service = RestClient.getClient(api);
		Call<BaseCallback> call = service.postEmailReceipt(request_id,request_type);
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
				if (error_msg != null) {
					error.setError(error_msg + "");
				} else {
					error.setError(context.getResources().getString(R.string.error));
				}
                baseInterface.onError(error);
			}
		});
	}

	public void requestMart(Map<String, String> martParams, String api, final BaseGenericInterface baseGenericInterface) {
		ApiInterface service = RestClient.getClient(api);

		Call<BaseGenericCallback<String>> call = service.postRequestMart(martParams);
		call.enqueue(new Callback<BaseGenericCallback<String>>() {
			@Override
			public void onResponse(Call<BaseGenericCallback<String>> call, Response<BaseGenericCallback<String>> response) {
				if (response.isSuccessful()) {
					BaseGenericCallback<String> Data = response.body();
					baseGenericInterface.onSuccess(Data);
				} else {
					APIErrorCallback error = ErrorUtils.parseError(response);
					baseGenericInterface.onError(error);
				}
			}

			@Override
			public void onFailure(Call<BaseGenericCallback<String>> call, Throwable t) {
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
