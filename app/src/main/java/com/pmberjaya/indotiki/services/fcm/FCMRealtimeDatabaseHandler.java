package com.pmberjaya.indotiki.services.fcm;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.bookingData.bookingComplete.BookingCompleteDetail;
import com.pmberjaya.indotiki.app.bookingData.bookingProgress.BookingInProgressDetail;
import com.pmberjaya.indotiki.app.chat.ChatActivity;
import com.pmberjaya.indotiki.app.event.EventDetailPromo;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.base.BaseGenericInterface;
import com.pmberjaya.indotiki.callbacks.APIErrorCallback;
import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.controllers.BookingController;
import com.pmberjaya.indotiki.dao.LocationSessionManager;
import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.models.event.EventPromoData;
import com.pmberjaya.indotiki.models.parcelables.BookingDataParcelable;
import com.pmberjaya.indotiki.services.TimeService;
import com.pmberjaya.indotiki.utilities.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;

public class FCMRealtimeDatabaseHandler extends Service {
    private Firebase firebase;
    int booking_nottif_id = 100;
    private GeoFire geoFire;
    private int runSearchOtherDriver = 0;

    SessionManager session;
    private String quantity;
    private String itemsData;
    private String price_per_item;
    private String note;
    private String item_id;

    @Override
    public void onCreate() {
        Firebase.setAndroidContext(getApplicationContext());
    }
    //When the service is started
    int i =0;
    Handler handler;
    Runnable runnable;
    String userId;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("Langkah 1","Langkah 1");
        session = new SessionManager(this);
        if(session!=null){
            userId = session.getUserDetails().get(SessionManager.KEY_ID);
            Log.d("Langkah 2","Langkah 2");
            String id = session.getRegistrationId();
            //Creating a firebase object
            firebase = new Firebase(Config.FIREBASE_APP + id);
            firebase.authWithCustomToken(Config.FINAL_TOKEN, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticationError(FirebaseError error) {
                    System.err.println("LoginActivity Failed! " + error.getMessage());
                    Log.d("Langkah 3","Langkah 3 Error");
                }
                @Override
                public void onAuthenticated(AuthData authData) {
                    System.out.println("LoginActivity Succeeded!");
                    Log.d("Langkah 3","Langkah 3");
                }
            });

            if(firebase!=null){
                Log.d("Langkah 4","Langkah 4");
                firebase.addValueEventListener(firebaseListener);
            }
        }
        //Adding a valueevent listener to firebase
        //this will help us to  track the value changes on firebase
        //Getting the firebase id from sharedpreferences
        return START_STICKY;
    }

//    public void checkBookingPassenger(){
//        gps = new GPSTracker(FCMRealtimeDatabaseHandler.this);
//        if(gps.canGetLocation()){
//            latitude = gps.getLatitude();
//            longitude = gps.getLongitude();
//            Log.d("latitude,longitude",""+latitude+", "+longitude);
//        }
//        else{
//            // can't get location
//            // GPS or Network is not enabled
//            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
//        }
//        session = new SessionManager(this);
//        startHandler();
//        HashMap<String,String> dataUser =session.getUserDetails();
//        // check if GPS enabled
//
//
//    }
//    public void startHandler(){
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//	    	      /* do what you need to do */
//                postLocationMember();
//	    	      /* and here comes the "trick" */
//                handler.postDelayed(this, 30000);
//            }
//        };
//        handler.postDelayed(runnable, 30000);
//    }
//    private void postLocationMember()
//    {
//        if (Utility.getInstance().isInternetOn(FCMRealtimeDatabaseHandler.this))
//        {
//            String api = Utility.getInstance().getTokenApi(FCMRealtimeDatabaseHandler.this);
//            UserController.getInstance(FCMRealtimeDatabaseHandler.this).postLocationMember(locationParameters(),api);
//            return;
//        }
//        else{
//
//            Toast.makeText(FCMRealtimeDatabaseHandler.this, getResources().getString(R.string.active_your_internet), Toast.LENGTH_SHORT).show();
//        }
//    }
//    public Map<String, String> locationParameters() {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("lat", String.valueOf(latitude));
//        params.put("lng", String.valueOf(longitude));
//        return  params;
//
//    }
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            generateEvent(bitmap, promo_event_title, promo_event_alert);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };
    private String promo_event_title;
    private String promo_event_alert;
    private String promo_event_image_url;
    private String promo_event_id;
    public ValueEventListener firebaseListener = new ValueEventListener() {
        //This method is called whenever we change the value in firebase
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            Log.d("SERVICE DATA CHANGE","RUNNING");
            //Getting the value from firebase
            //We stored none as a i nitial value
            if (snapshot.getValue() == null)
                return;
            if (snapshot.child("item_type").getValue() != null) {
                String item_type = snapshot.child("item_type").getValue().toString();
                //So if the value is none we will not create any notification
                if (item_type.equals("none"))
                    return;
                //If the value is anything other than none that means a notification has arrived
                //calling the method to show notification
                //String msg is containing the msg that has to be shown with the notification
                if (item_type.equals("demo")) {
                    generateNotification("Demo Sukses", "Demo", "0");
//                    Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
                    snapshot.getRef().setValue(null);

                } else if (item_type.equals("promo")) {
                    promo_event_title = checkIfSnapshotIsNull(snapshot,"title");
                    promo_event_alert =  checkIfSnapshotIsNull(snapshot,"alert");
                    if (snapshot.child("image_url").getValue() != null) {
                        promo_event_image_url = snapshot.child("image_url").getValue().toString();
                        Picasso.with(FCMRealtimeDatabaseHandler.this).load(promo_event_image_url).into(target);
                    } else {
                        generateEvent(null, promo_event_title, promo_event_alert);
                    }
                    if (snapshot.child("event_id").getValue() != null) {
                        promo_event_id = snapshot.child("event_id").getValue().toString();
                    } else {
                        promo_event_id = null;
                    }
                    snapshot.getRef().setValue(null);
                }else if (item_type.equals("transport_confirm")) {
                    String title =  checkIfSnapshotIsNull(snapshot,"title");
                    String alert =  checkIfSnapshotIsNull(snapshot,"alert");
                    String requestId =  checkIfSnapshotIsNull(snapshot,"request_id");
                    String channel =  checkIfSnapshotIsNull(snapshot,"channel");
                    //                generateNotification("Booking Message",getResources().getString(R.string.booking_accepted),String.valueOf(booking_nottif_id++));
                    generateBookingNotification(title, alert, String.valueOf(booking_nottif_id++), requestId, "transport");
                    Intent intent = new Intent("com.pmberjaya.indotiki.gcm");
                    intent.putExtra("itemType", item_type);
                    intent.putExtra("requestId", requestId);
                    intent.putExtra("requestType", "transport");
                    intent.putExtra("channel", channel);
                    sendBroadcast(intent);
                    snapshot.getRef().setValue(null);
                    session.deleteTimer();
                    if (TimeService.countDownTimer != null) {
                        TimeService.countDownTimer.cancel();
                    }
                    stopService(new Intent(FCMRealtimeDatabaseHandler.this, TimeService.class));
                }else if(item_type.equals("car_confirm")) {
                    String title =  checkIfSnapshotIsNull(snapshot,"title");
                    String alert =  checkIfSnapshotIsNull(snapshot,"alert");
                    String requestId =  checkIfSnapshotIsNull(snapshot,"request_id");
                    String channel =  checkIfSnapshotIsNull(snapshot,"channel");
                    generateBookingNotification(title, alert, String.valueOf(booking_nottif_id++), requestId, "car");
                    Intent intent = new Intent(Constants.BROADCAST_REALTIME_DB_RECEIVER);
                    intent.putExtra("itemType", item_type);
                    intent.putExtra("requestId", requestId);
                    intent.putExtra("requestType", "car");
                    intent.putExtra("channel", channel);
                    sendBroadcast(intent);
                    snapshot.getRef().setValue(null);
                    session.deleteTimer();
                    if (TimeService.countDownTimer != null) {
                        TimeService.countDownTimer.cancel();
                    }
                    stopService(new Intent(FCMRealtimeDatabaseHandler.this, TimeService.class));
                }
                else if(item_type.equals("change_food_price")) {
                    String receiptImagePath =  checkIfSnapshotIsNull(snapshot,"receipt_image_path");
                    String oldPrice =  checkIfSnapshotIsNull(snapshot,"old_price");
                    String newPrice =  checkIfSnapshotIsNull(snapshot,"new_price");
                    String cash_paid =  checkIfSnapshotIsNull(snapshot,"cash_paid");
                    String deposit_paid =  checkIfSnapshotIsNull(snapshot,"deposit_paid");
                    String requestId =  checkIfSnapshotIsNull(snapshot,"request_id");
                    String requestType =  checkIfSnapshotIsNull(snapshot,"request_type");
                    String title =  checkIfSnapshotIsNull(snapshot,"title");
                    String alert =  checkIfSnapshotIsNull(snapshot,"alert");
                    generateChangePriceNotification(title, alert, String.valueOf(booking_nottif_id++), requestId,requestType);
                    Intent intent = new Intent(Constants.BROADCAST_REALTIME_DB_RECEIVER);
                    intent.putExtra("itemType", item_type);
                    intent.putExtra("requestId", requestId);
                    intent.putExtra("requestType", requestType);
                    intent.putExtra("oldPrice", oldPrice);
                    intent.putExtra("newPrice", newPrice);
                    intent.putExtra("cashPaid", cash_paid);
                    intent.putExtra("depositPaid", deposit_paid);
                    intent.putExtra("receiptImagePath", receiptImagePath);
                    sendBroadcast(intent);
                    snapshot.getRef().setValue(null);
                }
                else if (item_type.equals("courier_confirm")) {
                    String title =  checkIfSnapshotIsNull(snapshot,"title");
                    String alert =  checkIfSnapshotIsNull(snapshot,"alert");
                    String requestId =  checkIfSnapshotIsNull(snapshot,"request_id");
                    String channel =  checkIfSnapshotIsNull(snapshot,"channel");
                    generateBookingNotification(title, alert, String.valueOf(booking_nottif_id++), requestId, "courier");
                    Intent intent = new Intent(Constants.BROADCAST_REALTIME_DB_RECEIVER);
                    intent.putExtra("itemType", item_type);
                    intent.putExtra("requestId", requestId);
                    intent.putExtra("requestType", "courier");
                    intent.putExtra("channel", channel);
                    sendBroadcast(intent);
                    snapshot.getRef().setValue(null);
                    session.deleteTimer();
                    if (TimeService.countDownTimer != null) {
                        TimeService.countDownTimer.cancel();
                    }
                    stopService(new Intent(FCMRealtimeDatabaseHandler.this, TimeService.class));
                } else if (item_type.equals("food_confirm")) {
                    String title =  checkIfSnapshotIsNull(snapshot,"title");
                    String alert =  checkIfSnapshotIsNull(snapshot,"alert");
                    String requestId = checkIfSnapshotIsNull(snapshot,"request_id");
                    String channel =  checkIfSnapshotIsNull(snapshot,"channel");
                    generateBookingNotification(title, alert, String.valueOf(booking_nottif_id++), requestId, "food");
                    Intent intent = new Intent("com.pmberjaya.indotiki.gcm");
                    intent.putExtra("itemType", item_type);
                    intent.putExtra("requestId", requestId);
                    intent.putExtra("requestType", "food");
                    intent.putExtra("channel", channel);
                    sendBroadcast(intent);
                    snapshot.getRef().setValue(null);
                    session.deleteTimer();
                    if (TimeService.countDownTimer != null) {
                        TimeService.countDownTimer.cancel();
                    }
                    stopService(new Intent(FCMRealtimeDatabaseHandler.this, TimeService.class));
                } else if (item_type.equals("taxi_confirm")) {
                    String title =  checkIfSnapshotIsNull(snapshot,"title");
                    String alert =  checkIfSnapshotIsNull(snapshot,"alert");
                    String requestId =  checkIfSnapshotIsNull(snapshot,"request_id");
                    String channel =  checkIfSnapshotIsNull(snapshot,"channel");
                    generateBookingNotification(title, alert, String.valueOf(booking_nottif_id++), requestId, "taxi");
                    Intent intent = new Intent(Constants.BROADCAST_REALTIME_DB_RECEIVER);
                    intent.putExtra("itemType", item_type);
                    intent.putExtra("requestId", requestId);
                    intent.putExtra("requestType", "taxi");
                    intent.putExtra("channel", channel);
                    sendBroadcast(intent);
                    snapshot.getRef().setValue(null);
                    session.deleteTimer();
                    if (TimeService.countDownTimer != null) {
                        TimeService.countDownTimer.cancel();
                    }
                       stopService(new Intent(FCMRealtimeDatabaseHandler.this, TimeService.class));
                }  else if (item_type.equals("mart_confirm")) {
                    String title = checkIfSnapshotIsNull(snapshot, "title");
                    String alert = checkIfSnapshotIsNull(snapshot, "alert");
                    String requestId = checkIfSnapshotIsNull(snapshot, "request_id");
                    String channel = checkIfSnapshotIsNull(snapshot, "channel");
                    generateBookingNotification(title, alert, String.valueOf(booking_nottif_id++), requestId, "mart");
                    Intent intent = new Intent(Constants.BROADCAST_REALTIME_DB_RECEIVER);
                    intent.putExtra("itemType", item_type);
                    intent.putExtra("requestId", requestId);
                    intent.putExtra("requestType", "mart");
                    intent.putExtra("channel", channel);
                    sendBroadcast(intent);
                    snapshot.getRef().setValue(null);
                    session.deleteTimer();
                    if (TimeService.countDownTimer != null) {
                        TimeService.countDownTimer.cancel();
                    }
                    stopService(new Intent(FCMRealtimeDatabaseHandler.this, TimeService.class));
                } else if(item_type.equals("change_mart_price")) {
                    String receiptImagePath = checkIfSnapshotIsNull(snapshot, "receipt_image_path");
                    String oldPrice = checkIfSnapshotIsNull(snapshot, "old_price");
                    String newPrice = checkIfSnapshotIsNull(snapshot, "new_price");
                    String cash_paid = checkIfSnapshotIsNull(snapshot, "cash_paid");
                    String deposit_paid = checkIfSnapshotIsNull(snapshot, "deposit_paid");
                    String requestId = checkIfSnapshotIsNull(snapshot, "request_id");
                    String requestType = checkIfSnapshotIsNull(snapshot, "request_type");
                    String title = checkIfSnapshotIsNull(snapshot, "title");
                    String alert = checkIfSnapshotIsNull(snapshot, "alert");
                    generateChangePriceNotification(title, alert, String.valueOf(booking_nottif_id++), requestId, requestType);
                    Intent intent = new Intent(Constants.BROADCAST_REALTIME_DB_RECEIVER);
                    intent.putExtra("itemType", item_type);
                    intent.putExtra("requestId", requestId);
                    intent.putExtra("requestType", requestType);
                    intent.putExtra("oldPrice", oldPrice);
                    intent.putExtra("newPrice", newPrice);
                    intent.putExtra("cashPaid", cash_paid);
                    intent.putExtra("depositPaid", deposit_paid);
                    intent.putExtra("receiptImagePath", receiptImagePath);
                    sendBroadcast(intent);
                    snapshot.getRef().setValue(null);
                }else if (item_type.equals("booking_complete")) {
                    String title =  checkIfSnapshotIsNull(snapshot,"title");
                    String alert =  checkIfSnapshotIsNull(snapshot,"alert");
                    String requestId =  checkIfSnapshotIsNull(snapshot,"request_id");
                    String requestType =  checkIfSnapshotIsNull(snapshot,"request_type");
                    String status =  checkIfSnapshotIsNull(snapshot,"status");

                    snapshot.getRef().setValue(null);
                    generateNotificationComplete(title, alert, requestId, requestType);
                    Intent intent = new Intent("com.pmberjaya.indotiki.gcm");
                    intent.putExtra("itemType", item_type);
                    intent.putExtra("requestId", requestId);
                    intent.putExtra("requestType", requestType);
                    intent.putExtra("status", status);
                    sendBroadcast(intent);
                    if(status.equals("driver_cancel")){
                        getBookingCancelData(requestId, requestType);
                    }else{
                        generateNotificationComplete(title, alert, requestId, requestType);
                        Intent i = new Intent("com.pmberjaya.indotiki.gcm");
                        i.putExtra("itemType", item_type);
                        i.putExtra("requestId", requestId);
                        i.putExtra("requestType", requestType);
                        i.putExtra("status", status);
                        sendBroadcast(i);
                    }
                } else if (item_type.equals("chat")) {
                    if (!session.isInChat()) {
                        sendNotificationChat(snapshot);
                    }
                    snapshot.getRef().setValue(null);
                } else if (item_type.equals("pick_up")) {
                    String title =  checkIfSnapshotIsNull(snapshot,"title");
                    String alert =  checkIfSnapshotIsNull(snapshot,"alert");
                    String requestId =  checkIfSnapshotIsNull(snapshot,"request_id");
                    String requestType =  checkIfSnapshotIsNull(snapshot, "request_type");
                    generateBookingNotification(title, alert, String.valueOf(booking_nottif_id++),requestId,requestType);
                    Intent intent = new Intent("com.pmberjaya.indotiki.gcm");
                    intent.putExtra("itemType", item_type);
                    intent.putExtra("requestId", requestId);
                    intent.putExtra("requestType", requestType);
                    sendBroadcast(intent);
                    snapshot.getRef().setValue(null);
                }/* else if (item_type.equals("referral")) {
                    String message =  checkIfSnapshotIsNull(snapshot,"message");
                    generateNotificationReferral(getResources().getString(R.string.referral), message, String.valueOf(booking_nottif_id++));
                    snapshot.getRef().setValue(null);
                } */else if (item_type.equals("login")) {
                    session.setKeyAnotherLogin("confirm");
                    snapshot.getRef().setValue(null);
                    //                String message = snapshot.child("Message").getValue().toString();
                    Intent intent = new Intent(Constants.BROADCAST_REALTIME_DB_RECEIVER);
                    intent.putExtra("itemType", item_type);
                    //                intent.putExtra("message",message);
                    sendBroadcast(intent);
                } else if (item_type.equals("access_denied")) {
                    session.setKeyAnotherLogin("access_denied");
                    snapshot.getRef().setValue(null);
                    //                String message = snapshot.child("Message").getValue().toString();
                    Intent intent = new Intent(Constants.BROADCAST_REALTIME_DB_RECEIVER);
                    intent.putExtra("itemType", item_type);
                    //                intent.putExtra("message",message);
                    sendBroadcast(intent);
                }
            }
        }
        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("The read failed: ", firebaseError.getMessage());
        }
    };
    private void generateChangePriceNotification(String title, String body, String notifId, String requestId, String requestType) {
        Context context = getBaseContext();

        Intent intent = new Intent(this, BookingInProgressDetail.class);
        intent.putExtra("requestId",requestId);
        intent.putExtra("requestType",requestType);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        if(session.isLogin()) {
            notificationBuilder.setContentIntent(pendingIntent);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification pnNotif = notificationBuilder.build();
        pnNotif.defaults |= Notification.DEFAULT_SOUND;
        pnNotif.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(Integer.parseInt(notifId) /* ID of notification */, pnNotif);
    }
    private void generateBookingNotification(String title, String body, String notifId, String requestId, String requestType) {
        Context context = getBaseContext();
        Intent intent = new Intent(this, BookingInProgressDetail.class);
        intent.putExtra("requestId",requestId);
        intent.putExtra("requestType",requestType);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        if(session.isLogin()) {
            notificationBuilder.setContentIntent(pendingIntent);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification pnNotif = notificationBuilder.build();
        pnNotif.defaults |= Notification.DEFAULT_SOUND;
        pnNotif.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(Integer.parseInt(notifId) /* ID of notification */, pnNotif);
    }

    private void generateNotification(String title, String body, String notifId) {
        Context context = getBaseContext();

        Intent intent = new Intent(this, MainActivityTab.class);
            intent.putExtra("tab","1");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        if(session.isLogin()) {
            notificationBuilder.setContentIntent(pendingIntent);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification pnNotif = notificationBuilder.build();
        pnNotif.defaults |= Notification.DEFAULT_SOUND;
        pnNotif.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(Integer.parseInt(notifId) /* ID of notification */, pnNotif);
    }
    private void generateNotificationReferral(String title, String body, String notifId) {
        Context context = getBaseContext();
        Intent intent = new Intent(this, MainActivityTab.class);
        intent.putExtra("tab",1);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification pnNotif = notificationBuilder.build();
        pnNotif.defaults |= Notification.DEFAULT_SOUND;
        pnNotif.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(Integer.parseInt(notifId) /* ID of notification */, pnNotif);
    }

    private void generateNotificationSearchDriver(String request_id, String request_type, String transportation) {
        Context context = getBaseContext();
        SessionManager sessionManager = new SessionManager(FCMRealtimeDatabaseHandler.this);
        sessionManager.setTimerRepeatData(request_id, request_type, System.currentTimeMillis());
//				startService(new Intent(BookingTransportConfirm.this, TimeService.class));
        Intent intent = new Intent("searchOtherDriver");
        intent.setClass(this, MainActivityTab.class);
        intent.putExtra("request_id", request_id);
        intent.putExtra("request_type", request_type);
        intent.putExtra("transportation", transportation);
        intent.putExtra("tab", 0);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =   new NotificationCompat.Builder(context)
                .setLargeIcon(icon)
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle(getResources().getString(R.string.driver_cancel_title))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getResources().getString(R.string.search_new_driver_for_you)))
                .setContentText(getResources().getString(R.string.search_new_driver_for_you))
                .setSound(defaultSoundUri)
                .setTicker(getResources().getString(R.string.driver_cancel_title))
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification pnNotif = notificationBuilder.build();
        pnNotif.defaults |= Notification.DEFAULT_SOUND;
        pnNotif.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(2958 /* ID of notification */, pnNotif);
    }

    private void generateNotificationComplete(String title,String alert, String request_id, String request_type) {
        Context context = getBaseContext();
        Intent intent = new Intent(this, BookingCompleteDetail.class);
        intent.putExtra("requestId", request_id);
        intent.putExtra("requestType", request_type);
        intent.putExtra("activity", "broadcast");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                        .bigText(alert))
                .setContentText(alert)
                .setSound(defaultSoundUri)
                .setTicker(title)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true);
        if(session.isLogin()) {
            notificationBuilder.setContentIntent(pendingIntent);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification pnNotif = notificationBuilder.build();
        pnNotif.defaults |= Notification.DEFAULT_SOUND;
        pnNotif.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(2 /* ID of notification */, pnNotif);
    }

    private void generateEvent(Bitmap bitmap, String title, String message){
        Context context = getBaseContext();

        EventPromoData eventDataParcelable = new EventPromoData();
        eventDataParcelable.setId(promo_event_id);
        eventDataParcelable.setTitle(title);
        eventDataParcelable.setDescription(message);
        eventDataParcelable.setAvatar_app(promo_event_image_url);
        Intent intent = new Intent(this, EventDetailPromo.class);
        intent.putExtra("parcelable",eventDataParcelable);
//        intent.putExtra("eventId", request_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Style notificationStyle = null;
        if(bitmap!=null) {
            NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap);
            s.setSummaryText(message);
            notificationStyle = s;
        }
        else{
            NotificationCompat.BigTextStyle t  = new NotificationCompat.BigTextStyle().bigText(message);
            notificationStyle = t;
        }

        NotificationCompat.Builder notificationBuilder =   new NotificationCompat.Builder(context)
                .setLargeIcon(icon)
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(title))
                .setSound(defaultSoundUri)
                .setStyle(notificationStyle)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);
        if(session.isLogin()) {
            notificationBuilder.setContentIntent(pendingIntent);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification pnNotif = notificationBuilder.build();
        pnNotif.defaults |= Notification.DEFAULT_SOUND;
        pnNotif.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(2 /* ID of notification */, pnNotif);
    }
    private void sendNotificationChat(DataSnapshot snapshot) {
        String user_id = checkIfSnapshotIsNull(snapshot,Constants.GCM_USER_ID);
        String user = checkIfSnapshotIsNull(snapshot,Constants.GCM_USER_FROM);
        String phone = checkIfSnapshotIsNull(snapshot,Constants.GCM_USER_PHONE_FROM);
        String avatar = checkIfSnapshotIsNull(snapshot,Constants.GCM_USER_AVATAR_FROM);
        String message = checkIfSnapshotIsNull(snapshot,Constants.GCM_MESSAGE_FROM);
        if(message==null||message.equals("")){
            message = getResources().getString(R.string.picture_received);
        }
        String request_id = checkIfSnapshotIsNull(snapshot,Constants.GCM_REQUEST_ID);
        String request_type = checkIfSnapshotIsNull(snapshot,Constants.GCM_REQUEST_TYPE);
        String channel =checkIfSnapshotIsNull(snapshot, Constants.GCM_CHANNEL);
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, ChatActivity.class);

        intent.putExtra(Constants.GCM_REQUEST_ID, request_id);
        intent.putExtra(Constants.GCM_USER_ID, user_id);
        intent.putExtra(Constants.GCM_REQUEST_TYPE, request_type);
        intent.putExtra(Constants.GCM_CHANNEL, channel);
        intent.putExtra(Constants.GCM_USER_FROM, user);
        intent.putExtra(Constants.GCM_USER_PHONE_FROM, phone);
        intent.putExtra(Constants.GCM_USER_AVATAR_FROM, avatar);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_launcher);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(icon)
                        .setSmallIcon(android.R.drawable.star_on)
                        .setContentTitle(user)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setContentText(message)
                        .setTicker(user + "\n" + message)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true);
        if(session.isLogin()) {
            mBuilder.setContentIntent(contentIntent);
        }
        Notification pnNotif = mBuilder.build();
        pnNotif.defaults |= Notification.DEFAULT_SOUND;
        pnNotif.defaults |= Notification.DEFAULT_VIBRATE;
        pnNotif.defaults |= Notification.DEFAULT_LIGHTS;
        mNotificationManager.notify(0, pnNotif);
        //mNotificationManager.notify(Integer.parseInt(request_id), pnNotif);  // Set notification ID
    }

    @Override
    public void onDestroy() {
        if(target!=null) {
            Picasso.with(this).cancelRequest(target);
        }
        if(firebase!=null) {
            firebase.removeEventListener(firebaseListener);
        }
    }

    BookingDataParcelable bookingDataParcelable;

    private void getBookingCancelData(String request_id, String request_type) {
        LocationSessionManager locationSessionManager = new LocationSessionManager(this);
        bookingDataParcelable = new BookingDataParcelable();
        bookingDataParcelable.id= request_id;
        bookingDataParcelable.requestType= request_type;
        bookingDataParcelable.district= locationSessionManager.getUserDistrictIdCentral();
        String api = Utility.getInstance().getTokenApi(FCMRealtimeDatabaseHandler.this);
        BookingController.getInstance(FCMRealtimeDatabaseHandler.this).getBookingCancelData(request_id, request_type, api, getBookingCancelDataInterface);
        return;
    }
    public Map<String, String> parameters(String request_id, String request_type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("request_id", request_id);
        params.put("request_type", request_type);
        return params;
    }

    BaseGenericInterface getBookingCancelDataInterface = new BaseGenericInterface() {
        @Override
        public <T> void onSuccess(BaseGenericCallback<T> baseGenericCallback) {
            int check = baseGenericCallback.getSukses();
            String pesan = baseGenericCallback.getPesan();
            if(check==2){
//                    bookingDataParcelable = bookingDataParcelable.buildBookingDataParcelable((BaseGenericCallback<BookingThisTripAgainData>) baseGenericCallback);
                generateNotificationSearchDriver(bookingDataParcelable.id, bookingDataParcelable.requestType, bookingDataParcelable.transportation);
                Intent intent = new Intent("com.pmberjaya.indotiki.gcm");
                intent.putExtra("requestId", bookingDataParcelable.id);
                intent.putExtra("requestType",  bookingDataParcelable.requestType);
                intent.putExtra("transportation",  bookingDataParcelable.transportation);
                intent.putExtra("itemType",  "search_driver_again");
                 sendBroadcast(intent);
            }
        }

        @Override
        public void onError(APIErrorCallback apiErrorCallback) {
            if (apiErrorCallback.getError() != null) {
                if (apiErrorCallback.getError().equals("Invalid API key ")) {
                    Log.d("Unauthorized", "Jalannn");
                    SessionManager session = new SessionManager(FCMRealtimeDatabaseHandler.this);
                    session.logoutUser();
                    Utility.getInstance().showInvalidApiKeyAlert(FCMRealtimeDatabaseHandler.this, getResources().getString(R.string.relogin));
                } else {
                    runSearchOtherDriver += 1;
                    if (runSearchOtherDriver < 3) {
                        getBookingCancelData(bookingDataParcelable.id, bookingDataParcelable.requestType);
                    } else {
                        Toast.makeText(FCMRealtimeDatabaseHandler.this, "Maaf saat ini rider mungkin sedang sibuk, silahkan coba kembali lagi nanti", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    @Override
    public void onTaskRemoved(Intent rootIntent) {



        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public String checkIfSnapshotIsNull(DataSnapshot snapshot, String tagName){
        String data = null;
        if(snapshot.child(tagName).getValue() != null) {
            data = snapshot.child(tagName).getValue().toString();
        }
        return data;
    }


}