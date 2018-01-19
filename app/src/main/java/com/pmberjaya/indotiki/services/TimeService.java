package com.pmberjaya.indotiki.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.bookingData.BookingCancelCallback;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.base.BaseInterface;
import com.pmberjaya.indotiki.interfaces.bookingData.BookingCancelInterface;
import com.pmberjaya.indotiki.models.others.CheckStatusBookingData;
import com.pmberjaya.indotiki.utilities.Utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by willy on 8/24/2016.
 */
public class TimeService extends Service  {
    private String requestType;
    private String requestId;
    public static CountDownTimer countDownTimer;
    private SessionManager sessionManager;
    private int maxRepeat=  Config.CURRENT_REPEAT;
    private int repeatTimes=1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        sessionManager = new SessionManager(this);
        HashMap<String, String> requestData = sessionManager.getTimerRepeatData();
        requestId = requestData.get(sessionManager.KEY_REQUESTID);
        requestType = requestData.get(sessionManager.KEY_REQUESTTYPE);
        long requestTime = sessionManager.getRequestTime();
        long timeNow = System.currentTimeMillis();
        long current_timer = Config.BID_DURATION-((timeNow-requestTime)/1000);
        if(current_timer<=0){
            getCheckStatusBooking();
        }else{
            ResponseRequestRepeat(requestId, requestType);
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(Config.TIMER_BID*1000,500) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("Tick",">"+millisUntilFinished);
            }

            @Override
            public void onFinish() {
                 if(sessionManager.getTimerRepeatData().get(SessionManager.KEY_REQUESTID)!=null) {
                    if (repeatTimes > maxRepeat) {
                        getCheckStatusBooking();
                    } else {
                        ResponseRequestRepeat(requestId, requestType);
                    }
                }else {
                    stopSelf();
                }
            }
        }.start();
    }

    public void getCheckStatusBooking()
    {
            String api = Utility.getInstance().getTokenApi(TimeService.this);
            BookingController.getInstance(TimeService.this).getCheckStatusBooking(requestId,requestType,api, checkStatusBookingInterface);
            return;
    }

    BaseGenericInterface checkStatusBookingInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            int sukses = baseGenericCallback.getSukses();
            if (sukses==2){
                CheckStatusBookingData data = (CheckStatusBookingData) baseGenericCallback.getData();
                if(data!=null) {
                    String status = data.getStatus();
                    if (status.equals("waiting")) {
                        ResponseRequestCancel(requestId, requestType, "1");
                        sessionManager.deleteTimer();
                    } else {
                        generateNotification(getResources().getString(R.string.booking_accepted_notification),getResources().getString(R.string.view_details),"400");
                    }
                }
                else {
                    ResponseRequestCancel(requestId, requestType, "1");
                    sessionManager.deleteTimer();
                }
            }
            else{
                ResponseRequestCancel(requestId, requestType, "1");
                sessionManager.deleteTimer();
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Log.d("Unauthorized", "Jalannn");
                    SessionManager session = new SessionManager(TimeService.this);
                    session.logoutUser();
                    Utility.getInstance().showInvalidApiKeyAlert(TimeService.this, getResources().getString(R.string.relogin));
                } else {
                    getCheckStatusBooking();
                }
            }
        }
    };

//    @Override
//    public void onSuccessCheckDataBooking(BaseGenericCallback<CheckStatusBookingData> checkStatusBookingDataBaseGenericCallback) {
//        if (checkStatusBookingDataBaseGenericCallback.getCallback().equals("getCheckStatusBookingNotif")) {
//            int sukses = checkStatusBookingDataBaseGenericCallback.getSukses();
//            if (sukses==2){
//                CheckStatusBookingData data = checkStatusBookingDataBaseGenericCallback.getData();
//                if(data!=null) {
//                    String status = data.getStatus();
//                    if (status.equals("waiting")) {
//                        ResponseRequestCancel(requestId, requestType, "1");
//                        sessionManager.deleteTimer();
//                    } else {
//                        generateNotification(getResources().getString(R.string.booking_accepted_notification),getResources().getString(R.string.view_details),"400");
//                    }
//                }
//                else {
//                    ResponseRequestCancel(requestId, requestType, "1");
//                    sessionManager.deleteTimer();
//                }
//            }
//            else{
//                ResponseRequestCancel(requestId, requestType, "1");
//                sessionManager.deleteTimer();
//            }
//        }
//    }
//
//    @Override
//    public void onErrorCheckDataBooking(APIErrorCallback apiErrorCallback) {
//        if (apiErrorCallback.getCallback().equals("getCheckStatusBooking")) {
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

    public void ResponseRequestCancel(String requestId,String requestType, String isForceCancel)
    {
            Log.d("CONTROLLER","JALAN");
            String api = Utility.getInstance().getTokenApi(TimeService.this);
            BookingController.getInstance(TimeService.this).postRequestResponseCancel(responseParameters(requestId,requestType,isForceCancel),api, requestCancelInterface);
            Log.d("param",">"+responseParameters(requestId,requestType,isForceCancel));
            return;
    }
    public Map<String, String> responseParameters(String requestId, String requestType, String isForceCancel) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", requestId);
        params.put("request_type", requestType);
        params.put("is_force_cancel", isForceCancel);
        return  params;
    }
    BookingCancelInterface requestCancelInterface = new BookingCancelInterface() {

        @Override
        public void onSuccessCancelBooking(BookingCancelCallback bookingCancelCallback) {
            int sukses = bookingCancelCallback.getSukses();
            String channel = bookingCancelCallback.getData();
            String pesan;
            if(sukses ==2){
                Intent intent = new Intent(Constants.BROADCAST_TIMER);
                intent.putExtra("type","timeout");
                intent.putExtra("status","true");
                intent.putExtra("channel",channel);
                sendBroadcast(intent);
                sessionManager.deleteTimer();
                generateNotification(getResources().getString(R.string.order_has_been_cancelled),getResources().getString(R.string.no_driver_sorry),"400");
            }
            else{
                Intent intent = new Intent(Constants.BROADCAST_TIMER);
                intent.putExtra("type","timeout");
                intent.putExtra("status","false");
                intent.putExtra("channel",channel);
                sendBroadcast(intent);
            }
            stopSelf();
        }

        @Override
        public void onErrorCancelBooking(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Log.d("Unauthorized", "Jalannn");
                    SessionManager session = new SessionManager(TimeService.this);
                    session.logoutUser();
                    Utility.getInstance().showInvalidApiKeyAlert(TimeService.this, getResources().getString(R.string.relogin));
                } else {
                    ResponseRequestCancel(requestId,requestType,"1");
                }
            }
        }
    };
    public void ResponseRequestRepeat(String requestId,String requestType)
    {
            String api = Utility.getInstance().getTokenApi(TimeService.this);
            BookingController.getInstance(TimeService.this).postRequestResponseRepeat(responseRepeatParameters(requestId,requestType),api, requestRepeatInterface);
            return;
    }

    public Map<String, String> responseRepeatParameters(String requestId, String requestType) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", requestId);
        params.put("request_type", requestType);
        params.put("times", String.valueOf(repeatTimes));
        return  params;
    }

    BaseInterface requestRepeatInterface = new BaseInterface() {
        @Override
        public void onSuccess(BaseCallback bookingRepeatCallback) {
            int sukses = bookingRepeatCallback.getSukses();
            if(sukses ==2){
                repeatTimes ++;
                if(countDownTimer!=null) {
                    countDownTimer.cancel();
                }
                startTimer();
            }
            else{
                ResponseRequestRepeat(requestId,requestType);
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError().equals("Invalid API key ")) {
                Log.d("Unauthorized", "Jalannn");
                SessionManager session = new SessionManager(TimeService.this);
                session.logoutUser();
                Utility.getInstance().showInvalidApiKeyAlert(TimeService.this, getResources().getString(R.string.relogin));
            } else {
//                    ResponseRequestRepeat(requestId,requestType);
            }
        }
    };

    private void generateNotification(String title, String body, String notifId) {
        Context context = getBaseContext();

        Intent intent = new Intent(this, MainActivityTab.class);
        intent.putExtra("tab","1");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =   new NotificationCompat.Builder(context)
                .setLargeIcon(icon)
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setContentText(body)
                .setSound(defaultSoundUri)
                .setTicker(title)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);
        if(sessionManager.isLogin()) {
            notificationBuilder.setContentIntent(pendingIntent);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification pnNotif = notificationBuilder.build();
        pnNotif.defaults |= Notification.DEFAULT_SOUND;
        pnNotif.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(Integer.parseInt(notifId) /* ID of notification */, pnNotif);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("destroy","destroy");
        if(countDownTimer!=null) {
            countDownTimer.cancel();
        }
    }

}
