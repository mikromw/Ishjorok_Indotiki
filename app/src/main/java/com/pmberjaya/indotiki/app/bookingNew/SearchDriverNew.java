package com.pmberjaya.indotiki.app.bookingNew;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.base.BaseActivity;
import com.pmberjaya.indotiki.app.bookingData.bookingProgress.BookingInProgressDetail;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.bookingData.BookingCancelCallback;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.interfaces.bookingData.BookingCancelInterface;
import com.pmberjaya.indotiki.models.others.CheckStatusBookingData;
import com.pmberjaya.indotiki.services.TimeService;
import com.pmberjaya.indotiki.utilities.Utility;
import com.pmberjaya.indotiki.views.CancelOrderAlertDialog;
import com.pmberjaya.indotiki.views.NoDriverCustomDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by willy on 11/21/2016.
 */

public class SearchDriverNew extends BaseActivity implements NoDriverCustomDialog.onSubmitListener, CancelOrderAlertDialog.onSubmitListener {
    // density 0.75 if it's LDPI
    // density 1.0 if it's MDPI
    // density 1.5 if it's HDPI
    // density 2.0 if it's XHDPI
    // density 3.0 if it's XXHDPI
    // density 4.0 if it's XXXHDPI
    private int screenWidth;
    private int screenHeight;
    private ImageView road;
    ImageView iv_driver;
    RelativeLayout iv_driver_layout;
    double leftMarginWidth;
    double topMarginHeight;
    SessionManager session;
    private String userId;
    private static final String TAG_REQUEST_ID= "requestId";
    private static final String TAG_REQUEST_TYPE= "requestType";
    private static final String TAG_DRIVER_ID= "driverId";
    private ProgressDialog pDialog;
    int Konfirmasi;
    String requestType;
    String requestId;
    public static Runnable runnable;
    private ImageButton cancel_booking;
    private int totalTimeCountInMilliseconds = 60000;
    private CountDownTimer countDownTimer;
    private boolean timeout = false;
    private ImageButton refresh;
    private ProgressBar progressBarRefresh;
    private ImageView iv_refresh;
    private RelativeLayout cancel_booking_layout;
//    private String transportation;
    private String forceCancel ="1";
    private boolean runResponseRequestCancel=false;
    private String timer;
    private String isSearching;
    private boolean runGetCheckStatusBooking = false;
    private String min_price;
    private String max_price;
    private LinearLayout bt_cancel_order;
    private BookingController bookingMemberController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_new_search_driver);
//        BookingController.getInstance(this).setCheckStatusBookingInterface(this);
        session = new SessionManager(this);
        HashMap<String, String> userdata = session.getUserDetails();
        userId = userdata.get(session.KEY_ID);
        Intent intent = getIntent();
        requestId = intent.getStringExtra("request_id");
        requestType = intent.getStringExtra("request_type");
//        transportation = intent.getStringExtra("transportation");
        isSearching = intent.getStringExtra("searching");
        bookingMemberController = BookingController.getInstance(this);
        if (isSearching != null && isSearching.equals("true")) {
            String request_id= session.getTimerRepeatData().get(SessionManager.KEY_REQUESTID);
            if (request_id == null) {
                ResponseRequestCancel(requestId, requestType, forceCancel);
            }
        }
        //checkBookingAcceptedDriver();
        cancel_booking = (ImageButton) findViewById(R.id.cancel_booking);
        refresh = (ImageButton) findViewById(R.id.refresh);
        progressBarRefresh = (ProgressBar) findViewById(R.id.progressBarRefresh);
        iv_refresh = (ImageView) findViewById(R.id.ic_refresh);
        cancel_booking_layout = (RelativeLayout) findViewById(R.id.cancel_booking_layout);
        cancel_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runResponseRequestCancel = true;
                ResponseRequestCancel(requestId, requestType, forceCancel);
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_refresh.setVisibility(View.GONE);
                progressBarRefresh.setVisibility(View.VISIBLE);
                getCheckStatusBooking();
            }
        });
//        int screenSize = getResources().getConfiguration().screenLayout &
//                Configuration.SCREENLAYOUT_SIZE_MASK;
        Float density = getResources().getDisplayMetrics().density;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;
        road = (ImageView) findViewById(R.id.road);
        iv_driver = (ImageView) findViewById(R.id.iv_driver);
        iv_driver_layout = (RelativeLayout) findViewById(R.id.iv_driver_layout);
        bt_cancel_order = (LinearLayout) findViewById(R.id.bt_cancel_order);
        rotateRoad();
        double imageWidth = screenWidth * 2.4;
        double imageHeight = screenWidth * 2.4;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) imageWidth, (int) imageHeight); //WRAP_CONTENT param can be FILL_PARENT

        if (density <= 0.75) { // LDPI
            leftMarginWidth = screenWidth * 0.7;
            topMarginHeight = screenHeight * 0.28;
        } else if (density <= 1.0) { // MDPI
            leftMarginWidth = screenWidth * 0.7;
            topMarginHeight = screenHeight * 0.28;
        } else if (density <= 1.5) { // HDPI
            leftMarginWidth = screenWidth * 0.7;
            topMarginHeight = screenHeight * 0.3;
        } else if (density <= 2.0) { // XHDPI
            leftMarginWidth = screenWidth * 0.7;
            topMarginHeight = screenHeight * 0.32;
        } else if (density <= 3.0) { // XXHDPI
            leftMarginWidth = screenWidth * 0.7;
            topMarginHeight = screenHeight * 0.31;
        } else if (density <= 4.0) { // XXXHDPI
            leftMarginWidth = screenWidth * 0.7;
            topMarginHeight = screenHeight * 0.31;
        }

        params.leftMargin = -(int) leftMarginWidth; //XCOORD
        params.topMargin = (int) topMarginHeight; //YCOORD
        road.setLayoutParams(params);
        road.requestLayout();
        road.setAdjustViewBounds(true);

        RelativeLayout.LayoutParams driverParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        driverParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        double driverTopMarginHeight = imageWidth * 0.165;
        driverParams.setMargins(0, (int) driverTopMarginHeight, 0, 0);
        if (requestType.equals(Constants.TRANSPORT)||requestType.equals(Constants.COURIER)||requestType.equals(Constants.FOOD)) {
            iv_driver.setImageResource(R.drawable.ic_search_driver_motorcycle);
        } else if (requestType.equals(Constants.TAXI)) {
            iv_driver.setImageResource(R.drawable.ic_search_driver_taxi);
        }/*else if(requestType.equals(Constants.CAR)) {
            iv_driver.setImageResource(R.drawable.car_side);
        }*/
        iv_driver_layout.setLayoutParams(driverParams);
        bt_cancel_order.setOnClickListener(cancelOrder);
    }

    public View.OnClickListener cancelOrder = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ResponseRequestCancel(requestId, requestType, forceCancel);
        }
    };

    private void rotateRoad(){
        RotateAnimation anim = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(10000);
        road.startAnimation(anim);
    }



    public void ResponseRequestCancel(String requestId, String requestType, String isForceCancel)
    {
        pDialog = new ProgressDialog(SearchDriverNew.this);
        pDialog.setMessage("Sedang Memproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        String api = Utility.getInstance().getTokenApi(SearchDriverNew.this);
        BookingController.getInstance(SearchDriverNew.this).postRequestResponseCancel(responseParameters(requestId,requestType,isForceCancel),api, bookingCancelInterface);
        return;
    }
    public void getCheckStatusBooking()
    {
            String api = Utility.getInstance().getTokenApi(SearchDriverNew.this);
            BookingController.getInstance(SearchDriverNew.this).getCheckStatusBooking(requestId,requestType,api, checkStatusBookingInterface);
            return;
    }
    BookingCancelInterface bookingCancelInterface = new BookingCancelInterface() {
        @Override
        public void onSuccessCancelBooking(BookingCancelCallback bookingCancelCallback) {
            runResponseRequestCancel = false;
//            countDownTimer.cancel();
            int sukses = bookingCancelCallback.getSukses();
            String pesan =null;
            if(timeout) {
                pesan = getResources().getString(R.string.no_driver);
            }
            else{
                pesan = getResources().getString(R.string.order_has_been_cancelled);
            }
            final String channel = bookingCancelCallback.getData();
            pDialog.dismiss();
            session.deleteTimer();
            if(sukses ==2){
                if(channel==null){
                    showNoDriverAlertDialog();
                }
                else {
                    Intent intentToBooking = new Intent(SearchDriverNew.this, BookingInProgressDetail.class);
                    intentToBooking.setAction(Constants.SEARCH_DRIVER);
                    intentToBooking.putExtra("requestType", requestType);
                    intentToBooking.putExtra("requestId", requestId);
                    intentToBooking.putExtra("min_price",min_price);
                    intentToBooking.putExtra("max_price",max_price);
                    intentToBooking.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentToBooking);
                    finish();
                }
                Toast.makeText(SearchDriverNew.this, pesan, Toast.LENGTH_LONG).show();
            }
            else{
                pesan="Pesanan ini sudah diselesaikan atau dibatalkan supir";
                Toast.makeText(SearchDriverNew.this, pesan, Toast.LENGTH_LONG).show();
                intentToBookingList();
                finish();
            }
        }

        @Override
        public void onErrorCancelBooking(APIErrorCallback apiErrorCallback) {
            pDialog.dismiss();
            if(apiErrorCallback.getError()!=null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Log.d("Unauthorized", "Jalannn");
                    SessionManager session = new SessionManager(SearchDriverNew.this);
                    session.logoutUser();
                    Utility.getInstance().showInvalidApiKeyAlert(SearchDriverNew.this, getResources().getString(R.string.relogin));
                }else if(apiErrorCallback.getError().equals("Error: Internal Server Error")){
                    Toast.makeText(SearchDriverNew.this,getResources().getString(R.string.oops_something_went_wrong), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SearchDriverNew.this,getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    BaseGenericInterface checkStatusBookingInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            runGetCheckStatusBooking = false;
            int sukses = baseGenericCallback.getSukses();
            if (sukses == 2) {
                CheckStatusBookingData data = (CheckStatusBookingData) baseGenericCallback.getData();
                if (data != null) {
                    String status = data.getStatus();
                    final String channel = data.getChannel();
                    if (status.equals("waiting")) {
                        iv_refresh.setVisibility(View.VISIBLE);
                        progressBarRefresh.setVisibility(View.GONE);
                    } else {
                        session.deleteTimer();
                        iv_refresh.setVisibility(View.GONE);
                        progressBarRefresh.setVisibility(View.GONE);
                        Intent intentToBooking = new Intent(SearchDriverNew.this, BookingInProgressDetail.class);
                        intentToBooking.setAction(Constants.SEARCH_DRIVER);
                        intentToBooking.putExtra("requestType", requestType);
                        intentToBooking.putExtra("requestId", requestId);
                        intentToBooking.putExtra("confirm", status);
                        intentToBooking.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentToBooking);
                        finish();
                    }
                } else {
                    showNoDriverAlertDialog();
                }
            } else {
                Toast.makeText(SearchDriverNew.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Log.d("Unauthorized", "Jalannn");
                    SessionManager session = new SessionManager(SearchDriverNew.this);
                    session.logoutUser();
                    Utility.getInstance().showInvalidApiKeyAlert(SearchDriverNew.this, getResources().getString(R.string.relogin));
                } else {
                    getCheckStatusBooking();
                }
            }
        }
    };

    private void showNoDriverAlertDialog() {
        NoDriverCustomDialog noDriverCustomDialog = new NoDriverCustomDialog();
        noDriverCustomDialog.mListener = SearchDriverNew.this;
        noDriverCustomDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        noDriverCustomDialog.show(getSupportFragmentManager(), "DialogFragment");
        noDriverCustomDialog.setCancelable(false);
    }
    private void showCancelOrderAlertDialog() {
        CancelOrderAlertDialog cancelOrderCustomDialog = new CancelOrderAlertDialog();
        cancelOrderCustomDialog.mListener = SearchDriverNew.this;
        cancelOrderCustomDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        cancelOrderCustomDialog.show(getSupportFragmentManager(), "DialogFragment");
        cancelOrderCustomDialog.setCancelable(false);
    }

    public void intentToBookingList(){
        if(isSearching!=null&&isSearching.equals("true")){
            Intent intentToBooking = new Intent(SearchDriverNew.this, MainActivityTab.class);
            intentToBooking.putExtra("tab","1");
            intentToBooking.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentToBooking);
        }
    }


    public Map<String, String> responseParameters(String requestId, String requestType, String isForceCancel) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", requestId);
        params.put("request_type", requestType);
        params.put("is_force_cancel", isForceCancel);
        return  params;
    }
    int currentLooping = 1;
    public BroadcastReceiver timerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String type = bundle.getString("type");
            if(type!=null&&type.equals("cancel_gone")){
                cancel_booking_layout.setVisibility(View.GONE);
            }
            else if(type!=null&&type.equals("cancel_visible")){
                cancel_booking_layout.setVisibility(View.VISIBLE);
            }
            else if(type!=null&&type.equals("timeout")){
                session.deleteTimer();
                String status = bundle.getString("status");
                String channel = bundle.getString("channel");
                if (status!=null&&status.equals("true")){
                    if(channel==null){
                        showNoDriverAlertDialog();
                    } else {
                        Intent intentToBooking = new Intent(SearchDriverNew.this, BookingInProgressDetail.class);
                        intentToBooking.setAction(Constants.SEARCH_DRIVER);
                        intentToBooking.putExtra("requestType", requestType);
                        intentToBooking.putExtra("requestId", requestId);
                        intentToBooking.putExtra("min_price",min_price);
                        intentToBooking.putExtra("max_price",max_price);
                        intentToBooking.putExtra("confirm", status);
                        intentToBooking.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentToBooking);
                        finish();
                    }
                }
            }
        }
    };

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
    }
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(timerReceiver,new IntentFilter(Constants.BROADCAST_TIMER));
        if(runGetCheckStatusBooking==true) {
            getCheckStatusBooking();
        }
        if(runResponseRequestCancel==true){
            ResponseRequestCancel(requestId,requestType,forceCancel);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        runGetCheckStatusBooking = true;
        unregisterReceiver(timerReceiver);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
//        countDownTimer.cancel();
    }
    @Override
    public void setOnSubmitListener(String arg) {
        if(pDialog!=null){
            pDialog.dismiss();
        }
        session.deleteTimer();
        if(requestType.equals("courier")){
            Intent intentToCourier = new Intent();
            SearchDriverNew.this.setResult(SearchDriverNew.this.RESULT_OK, intentToCourier);
        }
        intentToBookingList();
        finish();
    }
}
//    @Override
//    public void onSuccessCheckDataBooking(BaseGenericCallback<CheckStatusBookingData> checkStatusBookingDataBaseGenericCallback) {
//        if (checkStatusBookingDataBaseGenericCallback.getCallback().equals("getCheckStatusBooking")) {
//            runGetCheckStatusBooking = false;
//            int sukses = checkStatusBookingDataBaseGenericCallback.getSukses();
//            if (sukses==2){
//                CheckStatusBookingData data = checkStatusBookingDataBaseGenericCallback.getData();
//                if(data!=null) {
//                    String status = data.getStatus();
//                    final String channel = data.getChannel();
//                    if (status.equals("waiting")) {
//                        iv_refresh.setVisibility(View.VISIBLE);
//                        progressBarRefresh.setVisibility(View.GONE);
//                    } else {
//                        session.deleteTimer();
//                        iv_refresh.setVisibility(View.GONE);
//                        progressBarRefresh.setVisibility(View.GONE);
//                        Intent intentToBooking = new Intent(SearchDriverNew.this, BookingInProgressDetail.class);
//                        intentToBooking.putExtra("requestType", requestType);
//                        intentToBooking.putExtra("requestId", requestId);
//                        intentToBooking.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intentToBooking);
//                        finish();
//                    }
//                }
//                else {
//                    showNoDriverAlertDialog();
//                }
//            }
//            else{
//                Toast.makeText(SearchDriverNew.this, "Error", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    @Override
//    public void onErrorCheckDataBooking(APIErrorCallback apiErrorCallback) {
//        if (apiErrorCallback.getCallback().equals("getCheckStatusBooking")) {
//            iv_refresh.setVisibility(View.VISIBLE);
//            progressBarRefresh.setVisibility(View.GONE);
//            if (apiErrorCallback.getError() != null) {
//                if (apiErrorCallback.getError().equals("Invalid API key ")) {
//                    Log.d("Unauthorized", "Jalannn");
//                    SessionManager session = new SessionManager(this);
//                    session.logoutUser();
//                    Utility.getInstance().showInvalidApiKeyAlert(this, getResources().getString(R.string.relogin));
//                } else {
//                    Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

