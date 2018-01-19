package com.pmberjaya.indotiki.services.fcm;


import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.main.MainActivityTab;
import com.pmberjaya.indotiki.dao.SessionManager;

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
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class FCMAuthLoginHandler extends Service {
    private Firebase firebase;
    public DataSnapshot dataSnapshot;
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public IBinder mBinder = new myLocalBinder();
    SessionManager session;
    public class myLocalBinder extends Binder {
        public FCMAuthLoginHandler getService() {
            return FCMAuthLoginHandler.this;
        }
    }
    public void deleteFirebaseDB() {
        if (dataSnapshot.getValue()!= null) {
            dataSnapshot.getRef().setValue(null);
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        Firebase.setAndroidContext(getApplicationContext());
        session = new SessionManager(this);
        if(session!=null){
            String id = session.getRegistrationId();

            //Creating a firebase object
            firebase = new Firebase(Config.FIREBASE_APP + id);
            firebase.authWithCustomToken(Config.FINAL_TOKEN, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticationError(FirebaseError error) {
                    System.err.println("LoginActivity Failed! " + error.getMessage());
                }
                @Override
                public void onAuthenticated(AuthData authData) {
                    System.out.println("LoginActivity Succeeded!");
                }
            });

            if(firebase!=null){
                firebase.addValueEventListener(firebaseListener);
            }
        }
        return mBinder;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public ValueEventListener firebaseListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            if (snapshot.getValue() == null)
                return;
            String item_type = snapshot.child("item_type").getValue().toString();
            if (item_type.equals("none"))
                return;
            else if(item_type.equals("LoginActivity")){
                dataSnapshot = snapshot;
//                String message = snapshot.child("title").getValue().toString();
                Intent intent = new Intent(Constants.BROADCAST_REALTIME_DB_RECEIVER);
                intent.putExtra("itemType",item_type);
//                intent.putExtra("message",message);
                sendBroadcast(intent);
            }
            else if(item_type.equals("access_denied")){
                dataSnapshot = snapshot;
//                String message = snapshot.child("title").getValue().toString();
                Intent intent = new Intent(Constants.BROADCAST_REALTIME_DB_RECEIVER);
                intent.putExtra("itemType",item_type);
//                intent.putExtra("message",message);
                sendBroadcast(intent);
            }
        }
        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("The read failed: ", firebaseError.getMessage());
        }
    };
    private void generateNotification(String title, String body, String notifId) {
        Context context = getBaseContext();
        Intent intent = new Intent(this, MainActivityTab.class);
        intent.putExtra("tab",1);
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
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification pnNotif = notificationBuilder.build();
        pnNotif.defaults |= Notification.DEFAULT_SOUND;
        pnNotif.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(Integer.parseInt(notifId) /* ID of notification */, pnNotif);
    }

    @Override
    public void onDestroy() {
        if(firebase!=null) {
            firebase.removeEventListener(firebaseListener);
        }
    }


}