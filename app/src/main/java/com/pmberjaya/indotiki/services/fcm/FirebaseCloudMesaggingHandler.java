package com.pmberjaya.indotiki.services.fcm;

/**
 * Created by edwin on 10/01/2017.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.app.event.EventListPromoFragment;
import com.pmberjaya.indotiki.dao.SessionManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirebaseCloudMesaggingHandler extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    SessionManager session;
    Bitmap bitmap;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat
        String test = remoteMessage.getData().get("test");
        session = new SessionManager(this);

        bitmap = getBitmapfromUrl(remoteMessage.getData().get("image"));

        if (bitmap == null || bitmap.equals("")) {
            createNotificationText(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        } else {
            createNotificationImage(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), bitmap);
        }

    }

    private Bitmap getBitmapfromUrl(String image) {
        try {
            URL url = new URL(image);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    private void createNotificationImage(String messageTitle, String messageBody, Bitmap imageUrl) {
        Intent intent = new Intent( this , EventListPromoFragment.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(imageUrl))
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }

    private void createNotificationText(String messageTitle, String messageBody) {
        Intent intent = new Intent( this , EventListPromoFragment.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
}