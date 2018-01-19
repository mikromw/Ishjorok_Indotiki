package com.pmberjaya.indotiki.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pmberjaya.indotiki.dao.SessionManager;
import com.pmberjaya.indotiki.services.fcm.FCMRealtimeDatabaseHandler;

/**
 * Created by edwin on 27/12/2016.
 */

public class BootCompleteIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

//            ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
//            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//                if (serviceName.equals(service.service.getClassName())) {
            SessionManager session = new SessionManager(context);
            boolean isRiderOnline = session.isLogin();
            if(session.getRegistrationId()!=null&&isRiderOnline){
                Intent pushIntent = new Intent(context, FCMRealtimeDatabaseHandler.class);
                context.startService(pushIntent);
                Log.d("TURN ON!!","please");
            }
//                }
//            }
        }
    }
}