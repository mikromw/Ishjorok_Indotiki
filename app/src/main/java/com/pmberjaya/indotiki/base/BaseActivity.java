package com.pmberjaya.indotiki.base;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pmberjaya.indotiki.app.bookingData.bookingComplete.BookingCompleteDetail;
import com.pmberjaya.indotiki.app.bookingData.bookingProgress.BookingInProgressDetail;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.app.bookingNew.SearchDriverNew;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.services.TimeService;
import com.pmberjaya.indotiki.services.fcm.FCMRealtimeDatabaseHandler;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.io.ApiInterface;
import com.pmberjaya.indotiki.io.ErrorUtils;
import com.pmberjaya.indotiki.io.RestClient;
import com.pmberjaya.indotiki.models.bookingData.ChangePriceData;
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable;
import com.pmberjaya.indotiki.models.parcelables.ReceiveBroadCastParcelable;
import com.pmberjaya.indotiki.utilities.PicassoLoader;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by edwin on 09/06/2016.
 */
public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private FirebaseAnalytics mFirebaseAnalytics;
    private SessionManager sessionManager;
    private DBController dbController;
    private ProgressDialog progressDialog;
    private boolean runDeniedAccess = false;
    AlertDialog aDialog;
    private boolean isDialogShowing =false;
    private int currentRepeat = 0;
    BookingDataParcelable bookingDataParcelable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        dbController = DBController.getInstance(this);

    }

    protected void showProgress(String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            dismissProgress();
        mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), msg);
    }

    protected void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public interface BroadcastReceiverInterface{
        void onReceiveBroadCast(ReceiveBroadCastParcelable receiveBroadCastParcelable);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    protected void showInvalidApiKeyAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    protected void initToolbar(Toolbar toolbar, String title, View.OnClickListener onBackPressedListener){
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            if(onBackPressedListener==null) {
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
            }else{
                toolbar.setNavigationOnClickListener(onBackPressedListener);
            }
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    protected void showAlertLogin(String msg){
        isDialogShowing = true;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Perhatian");

        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // On pressing Settings button
        alertDialog.setPositiveButton("Izinkan", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                AllowAccess();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Tolak", null);
        aDialog = alertDialog.create();
        aDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = aDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        DeniedAccess();
                    }
                });
            }
        });
        aDialog.setCancelable(false);
        aDialog.show();
    }
    protected void showAlertDenied(String msg){
        isDialogShowing = true;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Perhatian");

        // Setting Dialog Message
        alertDialog.setMessage(msg);
        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                AllowAccess();
                dialog.cancel();
            }
        });

        aDialog = alertDialog.create();
        aDialog.setCancelable(false);
        aDialog.show();
    }

    private void AllowAccess(){
        sessionManager.logoutUser();
        stopService(new Intent(BaseActivity.this, FCMRealtimeDatabaseHandler.class));
        Intent intent = new Intent(BaseActivity.this,MainActivityTab.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void DeniedAccess(){
            runDeniedAccess = true;
            progressDialog = ProgressDialog.show(BaseActivity.this,"","Loading...");
            String api = Utility.getInstance().getTokenApi(BaseActivity.this);
            ApiInterface service = RestClient.getClient(api);
            Call<BaseCallback> call = service.deniedAccess(fcmParameters());
            call.enqueue(new Callback<BaseCallback>() {
                @Override
                public void onResponse(Call<BaseCallback> call, Response<BaseCallback> response) {
                    if (response.isSuccessful()) {
                        runDeniedAccess = false;
                        BaseCallback Data = response.body();
                        if(sessionManager==null) {
                            sessionManager = new SessionManager(BaseActivity.this);
                        }
                        sessionManager.setOffKeyAnotherLogin();
                        aDialog.dismiss();

                    } else {
                        APIErrorCallback error = ErrorUtils.parseError(response);
                        Toast.makeText(BaseActivity.this, error.getError() + ", " + getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
                @Override
                public void onFailure(Call<BaseCallback> call, Throwable t) {
                    APIErrorCallback error = new APIErrorCallback();
                    String error_msg = t.getMessage();
                    if(error_msg!=null) {
                        Toast.makeText(BaseActivity.this, error_msg+ ", " + getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(BaseActivity.this,getResources().getString(R.string.error)+ ", " + getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });
    }

    public Map<String, String> fcmParameters() {
        String fcm_id = sessionManager.getRegistrationId();
        Map<String,String> data = sessionManager.getUserDetails();
        String user_id = data.get(sessionManager.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("fcm_id", fcm_id);
        params.put("user_id",user_id);
        return  params;
    }
    String requestId;
    String requestType;
    String transportation;
    private BroadcastReceiverInterface broadCastReceiverInterface;
    public void setBroadCastReceiverInterface(BroadcastReceiverInterface broadCastReceiverInterface){
        this.broadCastReceiverInterface = broadCastReceiverInterface;
    }
    public BroadcastReceiver realtimeDBReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                ReceiveBroadCastParcelable receiveBroadCastParcelable = new ReceiveBroadCastParcelable();
                String item_type = bundle.getString("itemType");
                if(item_type.equals("transport_confirm")||item_type.equals("courier_confirm")||item_type.equals("food_confirm")
                        ||item_type.equals("taxi_confirm") || item_type.equals("car_confirm")|| item_type.equals("mart_confirm")){
                    String id = bundle.getString("requestId");
                    String request_type = bundle.getString("requestType");
                    Intent intentToBooking = new Intent(context, BookingInProgressDetail.class);
                    intentToBooking.setAction(Constants.SEARCH_DRIVER);
                    intentToBooking.putExtra("requestType", request_type);
                    intentToBooking.putExtra("requestId", id);
                    intentToBooking.putExtra("confirm",item_type);
                    intentToBooking.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentToBooking);
                    finish();
                }
                else if(item_type.equals("booking_complete")){
                    String id = bundle.getString("requestId");
                    String request_type = bundle.getString("requestType");
                    String status = bundle.getString("status");
                    Intent intentToBooking = new Intent(context, BookingCompleteDetail.class);
                    intentToBooking.putExtra("requestType", request_type);
                    intentToBooking.putExtra("requestId", id);
                    intentToBooking.putExtra("activity","broadcast");
                    DBController dbController = DBController.getInstance(context);
                    dbController.deleteChatHistory(id,request_type);
                    ArrayList<String> idArray = new ArrayList<>();
                    idArray.add(id);
                    dbController.deleteBookingProgress(idArray);
                    PicassoLoader.deleteImageFromDir(BaseActivity.this, request_type,id);
                    if(status.equals("driver_cancel")) {
                        Toast.makeText(context, getResources().getString(R.string.booking_cancelled), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, getResources().getString(R.string.booking_completed), Toast.LENGTH_SHORT).show();
                    }
                    intentToBooking.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(intentToBooking);
                    finish();
                    receiveBroadCastParcelable.itemType = "booking_complete";
                    if(broadCastReceiverInterface!=null) {
                        broadCastReceiverInterface.onReceiveBroadCast(receiveBroadCastParcelable);
                    }
                }
                else if(item_type.equals("pick_up")){
                    receiveBroadCastParcelable.itemType = "pick_up";
                    if(broadCastReceiverInterface!=null) {
                        broadCastReceiverInterface.onReceiveBroadCast(receiveBroadCastParcelable);
                    }
                    String id = bundle.getString("requestId");
                    String request_type = bundle.getString("requestType");
                }
                else if(item_type.equals("login")){
                    showAlertLogin(getResources().getString(R.string.another_login));
                }
                else if(item_type.equals("access_denied")){
                    showAlertDenied(getResources().getString(R.string.denied_login));
                }
                else if(item_type.equals("search_driver_again")){
//                    bookingDataParcelable = bundle.getParcelable("bookingDataParcelable");
//                    requestType = bookingDataParcelable.requestType;
//                    transportation = bookingDataParcelable.transportation;
//                    Utility.getInstance().showSimpleAlertDialog(BaseActivity.this, getResources().getString(R.string.attention), getResources().getString(R.string.driver_cancel_search_again),
//                            getResources().getString(R.string.yes),positiveSearchDriverAgain ,getResources().getString(R.string.no), negativeSearchDriverAgain);
                }else if(item_type.equals("change_food_price")){
                    receiveBroadCastParcelable.itemType = "change_food_price";
                    receiveBroadCastParcelable.changePriceData = new ChangePriceData();
                    receiveBroadCastParcelable.changePriceData.requestId =  bundle.getString("requestId");
                    receiveBroadCastParcelable.changePriceData.requestType =  bundle.getString("requestType");
                    receiveBroadCastParcelable.changePriceData.oldPrice =  bundle.getString("oldPrice");
                    receiveBroadCastParcelable.changePriceData.newPrice =  bundle.getString("newPrice");
                    receiveBroadCastParcelable.changePriceData.cashPaid =  bundle.getString("cashPaid");
                    receiveBroadCastParcelable.changePriceData.depositPaid =  bundle.getString("depositPaid");
                    receiveBroadCastParcelable.changePriceData.receiptImagePath =  bundle.getString("receiptImagePath");
                    if(broadCastReceiverInterface!=null) {
                        broadCastReceiverInterface.onReceiveBroadCast(receiveBroadCastParcelable);
                    }
                }else if(item_type.equals("change_mart_price")){
                    receiveBroadCastParcelable.itemType = "change_mart_price";
                    receiveBroadCastParcelable.changePriceData = new ChangePriceData();
                    receiveBroadCastParcelable.changePriceData.requestId =  bundle.getString("requestId");
                    receiveBroadCastParcelable.changePriceData.requestType =  bundle.getString("requestType");
                    receiveBroadCastParcelable.changePriceData.oldPrice =  bundle.getString("oldPrice");
                    receiveBroadCastParcelable.changePriceData.newPrice =  bundle.getString("newPrice");
                    receiveBroadCastParcelable.changePriceData.cashPaid =  bundle.getString("cashPaid");
                    receiveBroadCastParcelable.changePriceData.depositPaid =  bundle.getString( "depositPaid");
                    receiveBroadCastParcelable.changePriceData.receiptImagePath =  bundle.getString("receiptImagePath");
                    if(broadCastReceiverInterface!=null) {
                        broadCastReceiverInterface.onReceiveBroadCast(receiveBroadCastParcelable);
                    }
                }
            }
            else{
                Toast.makeText(context, "GCM data is null", Toast.LENGTH_SHORT).show();
            }
        }
    };

//    private DialogInterface.OnClickListener positiveSearchDriverAgain = new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialogInterface, int i) {
//            dialogInterface.dismiss();
//            if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(requestType)&&requestType.equals(Constants.TRANSPORT)) {
//                postRequestTransport();
//            }else if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(requestType)&&requestType.equals(Constants.COURIER)) {
//                postRequestCourier();
//            }else if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(requestType)&&requestType.equals(Constants.FOOD)) {
//                postRequestFood();
//            }
//        }
//    };
//    private DialogInterface.OnClickListener negativeSearchDriverAgain = new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialogInterface, int i) {
//
//            Intent intentToBooking = new Intent(BaseActivity.this, BookingCompleteDetail.class);
//            intentToBooking.putExtra("requestType", bookingDataParcelable.requestType);
//            intentToBooking.putExtra("requestId", bookingDataParcelable.id);
//            intentToBooking.putExtra("activity","broadcast");
//            DBController dbController = DBController.getInstance(BaseActivity.this);
//            dbController.deleteChatHistory(bookingDataParcelable.id,bookingDataParcelable.requestType);
//            ArrayList<String> idArray = new ArrayList<>();
//            idArray.add(bookingDataParcelable.id);
//            dbController.deleteBookingProgress(idArray);
//            PicassoLoader.deleteImageFromDir(BaseActivity.this, bookingDataParcelable.requestType,bookingDataParcelable.id);
//            Toast.makeText(BaseActivity.this, getResources().getString(R.string.booking_completed), Toast.LENGTH_SHORT).show();
//            intentToBooking.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_NEW_TASK );
//            startActivity(intentToBooking);
//            finish();
//            dialogInterface.dismiss();
//        }
//    };

    public void sendNotificationToDriver(){
        sessionManager.setTimerRepeatData(requestId, requestType, System.currentTimeMillis());
        Intent service = new Intent(BaseActivity.this, TimeService.class);
        service.putExtra("request_id",requestId);
        service.putExtra("request_type",requestType);
        startService(service);
    }
    public void intentToSearchDriverActivity(){
        Intent intent = new Intent(BaseActivity.this,SearchDriverNew.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("request_id", requestId);
        intent.putExtra("request_type", requestType);
        intent.putExtra("transportation", transportation);
        startActivity(intent);
    }

    public void initFirebase(){
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public void setmFirebaseAnalytics(String id, String name, String content_type, String event){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);//success or failure
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, content_type);//subevent
        mFirebaseAnalytics.logEvent(event, bundle);
    }

    public void setCustomFirebaseAnalytics(String type, String transportation, String payment , String event){
        Bundle bundle = new Bundle();
        bundle.putString("type", type);//success or failure
        bundle.putString("transportation", transportation);
        bundle.putString("payment", payment);//subevent
        mFirebaseAnalytics.logEvent(event, bundle);
    }
    public void setmFirebaseProperties(String label, String value){
        mFirebaseAnalytics.setUserProperty(label, value);
    }

    public void startFcmRealtimeDbHandlerService(){
        Intent intent = new Intent(this, FCMRealtimeDatabaseHandler.class);
        if(sessionManager.getRegistrationId()!=null){
            startService(intent);
        }
    }
    private String serviceName = Constants.REALTIME_DB_SERVICE;
    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                Log.d("SERVICE RUN","RUNNNNNN");
                return true;
            }
        }
        Log.d("SERVICE NOT RUN","NOT RUNNNNNN");
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(runDeniedAccess == true){
            DeniedAccess();
        }
        registerReceiver(realtimeDBReceiver, new IntentFilter(Constants.BROADCAST_REALTIME_DB_RECEIVER));
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(realtimeDBReceiver);
    }
    private String timeServiceName = Constants.TIME_SERVICE;
    private boolean isTimeServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (timeServiceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public void startTimeService(){
        Intent service = new Intent(this, TimeService.class);
        HashMap<String,String> bookingData = sessionManager.getTimerRepeatData();
        String requestId = bookingData.get(sessionManager.KEY_REQUESTID);
        String requestType = bookingData.get(sessionManager.KEY_REQUESTTYPE);
        service.putExtra("request_id",requestId);
        service.putExtra("request_type",requestType);
        service.putExtra("current_repeat",currentRepeat);
        startService(service);
    }
    @Override
    protected void onStart() {
        super.onStart();
        sessionManager = new SessionManager(BaseActivity.this);
        initFirebase();
        Firebase.setAndroidContext(this);
        if(!isServiceRunning()){
            startFcmRealtimeDbHandlerService();
        }
        String request_id= sessionManager.getTimerRepeatData().get(SessionManager.KEY_REQUESTID);
        if(request_id!=null){
            if(!isTimeServiceRunning()){
                startTimeService();
            }
        }
        if(sessionManager!=null&&sessionManager.isAnotherLogin()){
            if(sessionManager.getAnotherLoginType().equals("confirm")){
                if(!isDialogShowing) {
                    showAlertLogin(getResources().getString(R.string.another_login));
                }
            }else if(sessionManager.getAnotherLoginType().equals("access_denied")){
                if(!isDialogShowing) {
                    showAlertDenied(getResources().getString(R.string.denied_login));
                }
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }




    public Map<String, String> courierParams() {
        String channel = Utility.getInstance().shuffle();
        Map<String, String> params = new HashMap<String, String>();
        if(bookingDataParcelable.fromPlace==null){
            bookingDataParcelable.fromPlace="";
        }
        if(bookingDataParcelable.toPlace==null){
            bookingDataParcelable.toPlace="";
        }
        String userId = sessionManager.getUserDetails().get(SessionManager.KEY_ID);
        params.put("user_id", userId);
        params.put("transportation", bookingDataParcelable.transportation);
        params.put("from_place", bookingDataParcelable.fromPlace);
        params.put("from", bookingDataParcelable.from);
        params.put("to_place", bookingDataParcelable.toPlace);
        params.put("to", bookingDataParcelable.to);
        if(bookingDataParcelable.bookingCourierDatas.location_detail_sender!=null) {
            params.put("location_detail_sender", bookingDataParcelable.bookingCourierDatas.location_detail_sender);
        }else{
            params.put("location_detail_sender", "");
        }
        if(bookingDataParcelable.bookingCourierDatas.location_detail_receiver!=null) {
            params.put("location_detail_receiver", bookingDataParcelable.bookingCourierDatas.location_detail_receiver);
        }else{
            params.put("location_detail_receiver", "");
        }
        params.put("distance", bookingDataParcelable.distance);
        if(bookingDataParcelable.tip!=null) {
            params.put("tip", bookingDataParcelable.tip);
        }else{
            params.put("tip", "");
        }

        params.put("start_lat",String.valueOf(bookingDataParcelable.latFrom));
        params.put("start_lng",String.valueOf(bookingDataParcelable.lngFrom));
        params.put("end_lat",String.valueOf(bookingDataParcelable.latTo));
        params.put("end_lng",String.valueOf(bookingDataParcelable.lngTo));
        params.put("name_sender", bookingDataParcelable.bookingCourierDatas.name_sender);
        params.put("phone_sender", bookingDataParcelable.bookingCourierDatas.phone_sender);
        params.put("name_receiver", bookingDataParcelable.bookingCourierDatas.name_receiver);
        params.put("phone_receiver", bookingDataParcelable.bookingCourierDatas.phone_receiver);
        params.put("item_delivered", bookingDataParcelable.bookingCourierDatas.item);
        if (bookingDataParcelable.payment != null) {
            params.put("payment", bookingDataParcelable.payment);
        }else{
            params.put("payment", "");
        }
        if (bookingDataParcelable.bookingCourierDatas.item_photo != null) {
            params.put("item_photo", bookingDataParcelable.bookingCourierDatas.item_photo);
        }else{
            params.put("item_photo", "");
        }
        params.put("channel", channel);
        if(bookingDataParcelable.promoCode!=null) {
            params.put("code_promo", bookingDataParcelable.promoCode);
        }else{
            params.put("code_promo", "");
        }
        if (bookingDataParcelable.district != null) {
            params.put("district_id", bookingDataParcelable.district);
        }else{
            params.put("district_id", "");
        }
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
        return params;
    }
    public static int RATE_DRIVER = 1;


//    public boolean setBookingDataViewInterface(){
//        return
//    }

}
